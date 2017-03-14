package view.tree;

/**
 * Created by neukamm on 09.11.16.
 */

import Logging.LogClass;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import view.dialogues.settings.DataFilteringTreebasedDialogue;
import view.table.controller.ATableController;
import view.table.TableSelectionFilter;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class HaplotreeController {

    private Rectangle2D boxBounds = new Rectangle2D(500, 300, 600, 480);
    private double ACTION_BOX_HGT = 30;
    private Label searchLbl;
    private SimpleBooleanProperty isExpanded = new SimpleBooleanProperty();
    private String[] seletcion_haplogroups;
    private ATableController tableManager;

    private TextField searchFieldListHaplogroup;
    private TreeHaplo tree;
    private HashMap<String, List<String>> treeMap;
    private HashMap<String, List<String>> treeMap_leaf_to_root;
    private HashMap<String, List<String>> node_to_children;

    private Logger LOG;
    private VBox treeSearchPane;

    public HaplotreeController(ATableController tableManager, LogClass logClass) throws IOException, SAXException, ParserConfigurationException {

        this.tableManager = tableManager;
        LOG = logClass.getLogger(this.getClass());

        treeMap = new HashMap<>();
        treeMap_leaf_to_root = new HashMap<>();
        node_to_children = new HashMap<>();


        searchLbl = new Label("Haplo tree");
        searchLbl.setId("treeViewOpenCloseLabel");


        tree = new TreeHaplo("Haplo tree");
        tree.addStructure();
        createTreeMap(tree.getRootItem());

    }



    /**
     * Method to configure the search pane.
     * @param
     * @param dataFilteringTreebasedDialogue
     */
    public void configureSearch(DataFilteringTreebasedDialogue dataFilteringTreebasedDialogue) throws IOException, SAXException, ParserConfigurationException{

        Button applyBtn = new Button("Apply filter");
        applyBtn.setId("treeviewApplyButton");
        searchFieldListHaplogroup = new TextField();
        searchFieldListHaplogroup.setId("treeviewSearchField");
        searchFieldListHaplogroup.setPrefSize(50,10);



        /*

                        configure "apply filter" button


         */

        applyBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent paramT) {
                if (tableManager.getTable().getItems().size() > 0){
                    if(searchFieldListHaplogroup.getText().equals("")){
                        applyFilterFunction(dataFilteringTreebasedDialogue);
                    } else {
                        applyFilterFunctionList(dataFilteringTreebasedDialogue);
                    }

                }


            }
        });


        /*

                       search field haplogroupFilter


         */


        tree.getTree().setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    applyFilterFunction(dataFilteringTreebasedDialogue);
                }
            }
        });

        searchFieldListHaplogroup.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    applyFilterFunctionList(dataFilteringTreebasedDialogue);
                }
            }
        });



        Label infolabel = new Label("Please select Haplogroups either " +
                "in the tree OR specify the desired Haplogroups as list:");
        infolabel.setMinSize(80, 80);

        Label haploLabel = new Label("Comma separated list of haplogroups:");
        infolabel.setMinSize(20, 30);

        treeSearchPane = new VBox();
        treeSearchPane.setId("treeView-inner-tree");
        treeSearchPane.setPadding(new Insets(10));
        treeSearchPane.setAlignment(Pos.TOP_LEFT);
        treeSearchPane.setStyle("-fx-background-color:#333333,#b1afb0;-fx-background-insets:0,1.5;-fx-opacity:.92;-fx-background-radius:0px 0px 0px 5px;");
        treeSearchPane.setPrefSize(boxBounds.getWidth(), boxBounds.getHeight()-ACTION_BOX_HGT);
        treeSearchPane.setSpacing(10);

        treeSearchPane.getChildren().addAll(infolabel, tree.getTree(), haploLabel, searchFieldListHaplogroup , applyBtn);

    }


    private void applyFilterFunction(DataFilteringTreebasedDialogue dataFilteringTreebasedDialogue){

        ObservableList<TreeItem<String>> itemSelection =  tree.getTree().getSelectionModel().getSelectedItems();

        if(itemSelection.size() > 0){
            seletcion_haplogroups = new String[itemSelection.size()];
            for(int i = 0; i < itemSelection.size(); i++){
                seletcion_haplogroups[i] = itemSelection.get(i).getValue();
            }
            // if selection has size zero --> take all haplogroups
            String[] backup_hg_selection = seletcion_haplogroups.clone();
            if(seletcion_haplogroups.length == 0){
                TableColumn haplo_col = tableManager.getTableColumnByName("Haplogroup");
                List<String> columnData = new ArrayList<>();
                for (Object item : tableManager.getTable().getItems()) {
                    columnData.add((String)haplo_col.getCellObservableValue(item).getValue());
                }
                seletcion_haplogroups = columnData.toArray(new String[columnData.size()]);
            } else { // get all sub-haplo-groups of selected HG's
                List<String> haplotypes = getAllSubgroups(seletcion_haplogroups);
                seletcion_haplogroups = haplotypes.toArray(new String[haplotypes.size()]);
            }

            // close tree view
            tree.getTree().getSelectionModel().clearSelection();

            // parse selection to tablefilter
            TableSelectionFilter tableFilter = new TableSelectionFilter();

            if (seletcion_haplogroups.length !=0) {
                LOG.info("Filter data. Table now contains only data of Haplotype: "
                        + Arrays.toString(backup_hg_selection));
                tableFilter.haplogroupFilter(tableManager, seletcion_haplogroups,
                        tableManager.getColIndex("Haplogroup"), LOG);
            }
        }
        dataFilteringTreebasedDialogue.close();
    }


    private void applyFilterFunctionList(DataFilteringTreebasedDialogue dataFilteringTreebasedDialogue){

        // parse haplogroup list, get all corresponding table entries (subgroups included)
        // and show results in table

        if(!searchFieldListHaplogroup.getText().equals("") && tableManager.getTable().getItems().size() > 0){
            List<String> allHaplogroups = getAllSubgroups(searchFieldListHaplogroup.getText().split(","));

            seletcion_haplogroups = allHaplogroups.toArray(new String[allHaplogroups.size()]);

            // close tree view
            tree.getTree().getSelectionModel().clearSelection();

            // parse selection to tablefilter
            TableSelectionFilter tableFilter = new TableSelectionFilter();

            if (seletcion_haplogroups.length !=0) {
                LOG.info("Filter data. Table now contains only data of Haplotype: " +
                        Arrays.toString(searchFieldListHaplogroup.getText().split(",")));

                tableFilter.haplogroupFilter(tableManager, seletcion_haplogroups,
                        tableManager.getColIndex("Haplogroup"), LOG);
                searchFieldListHaplogroup.setText("");
            }

        }
        dataFilteringTreebasedDialogue.close();

    }


    /**
     * get all nodes (haplogroups) lying below treenode (path to all leaves connected to treenode)
     * @param haplo_list
     */
    private List<String> getAllSubgroups(String[] haplo_list){
        List<String> haplo_list_extended = new ArrayList<String>();

        // iterate over all specified haplogroups, get subtree of each
        for(String haplo : haplo_list){
            // get sub-haplogroups of 'haplo'
            List<String> path = treeMap.get(haplo.toString().trim().toUpperCase());

            // add them to list
            haplo_list_extended.addAll(path);
        }

        return haplo_list_extended;

    }


    public void createTreeMap(TreeItem item){

        TreeIterator<String> iterator = new TreeIterator<>(item);
        TreeItem it = item;
        while (iterator.hasNext()) {
            treeMap.put(it.getValue().toString(), getSubtree(it));
            treeMap_leaf_to_root.put(it.getValue().toString(), getPathToRoot(it));
            node_to_children.put(it.getValue().toString(), it.getChildren());
            it = iterator.next();
        }
    }


    /**
     * get all nodes on the path from treenode to root
     * @param it
     * @return
     */
    public List<String> getPathToRoot(TreeItem it){

        List<String> path_to_root = new ArrayList<>();
        while(it.getParent()!=null){
            path_to_root.add(it.getParent().getValue().toString());
            it = it.getParent();
        }
        return path_to_root;
    }


    public TreeItem getTreeItem(String item_name){

        TreeItem result=null;

        TreeIterator<String> iterator = new TreeIterator<>(tree.getRootItem());
        TreeItem it;// = iterator.next();
        while (iterator.hasNext()) {
            it = iterator.next();
            if(it.getValue().equals(item_name)){
                result = it;
                break;
            }
        }
        return result;

    }

    private List<String> getSubtree(TreeItem root){

        List<String> path = new ArrayList<String>();
        TreeIterator<String> iterator = new TreeIterator<>(root);
        TreeItem it;// = iterator.next();
        while (iterator.hasNext()) {
            it = iterator.next();
            path.add(it.getValue().toString());
        }
        return path;
    }

    /**
     * get copy of tree
     * @param item
     * @return
     */
    public TreeItem<String> deepcopy(TreeItem<String> item) {
        TreeItem<String> copy = new TreeItem<String>(item.getValue());
        for (TreeItem<String> child : item.getChildren()) {
            copy.getChildren().add(deepcopy(child));
        }
        return copy;
    }

    public HashMap<String, List<String>> getTreeMap() {
        return treeMap;
    }

    public TreeHaplo getTree() {
        return tree;
    }

    public HashMap<String, List<String>> getTreeMap_leaf_to_root() {
        return treeMap_leaf_to_root;
    }

    public VBox getStackPaneSearchWithList() { return treeSearchPane; }
}