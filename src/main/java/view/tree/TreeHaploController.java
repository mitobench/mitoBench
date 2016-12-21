package view.tree;

/**
 * Created by neukamm on 09.11.16.
 */

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.xml.sax.SAXException;
import view.table.TableController;
import view.table.TableSelectionFilter;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TreeHaploController {

    private VBox searchPane;
    private Rectangle2D boxBounds = new Rectangle2D(500, 300, 600, 480);
    private double ACTION_BOX_HGT = 30;
    private StackPane downArrow = new StackPane();
    private StackPane upArrow = new StackPane();
    private Label searchLbl = new Label("Tree View");
    private SimpleBooleanProperty isExpanded = new SimpleBooleanProperty();
    private Rectangle clipRect;
    private Timeline timelineUp;
    private Timeline timelineDown;
    private String[] seletcion_haplogroups;
    private TableController tableManager;

    private TextField searchFieldListHaplogroup;
    private TreeHaplo tree;
    private HashMap<String, List<String>> treeMap;
    private HashMap<String, List<String>> treeMap_leaf_to_root;
    private HashMap<String, List<String>> node_to_children;

    public TreeHaploController(Pane root, TableController tableManager) throws IOException, SAXException, ParserConfigurationException {

        this.tableManager = tableManager;

        treeMap = new HashMap<>();
        treeMap_leaf_to_root = new HashMap<>();
        node_to_children = new HashMap<>();

        searchLbl.setGraphic(downArrow);
        searchLbl.setId("treeViewOpenCloseLabel");
        upArrow.setStyle("-fx-padding: 8px 5px 0px 5px;-fx-background-color: black;-fx-shape: \"M0 1 L1 1 L.5 0 Z\";");
        downArrow.setStyle("-fx-padding: 8px 5px 0px 5px;-fx-background-color: black;-fx-shape: \"M0 0 L1 0 L.5 1 Z\";");

        configureSearch(root);
        setAnimation();


        isExpanded.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> paramObservableValue, Boolean paramT1, Boolean paramT2) {
                if(paramT2){
                    // To expand
                    timelineDown.play();
                    searchLbl.setGraphic(upArrow);

                }else{
                    // To close
                    timelineUp.play();
                    searchLbl.setGraphic(downArrow);
                }
            }
        });


    }



    /**
     * Method to configure the search pane.
     * @param
     */
    private void configureSearch(Pane root) throws IOException, SAXException, ParserConfigurationException{
        searchPane = new VBox();
        searchPane.setId("treeviewSearchPane");
        searchPane.setAlignment(Pos.TOP_LEFT);

        StackPane sp1 = new StackPane();
        sp1.setPadding(new Insets(10));
        sp1.setAlignment(Pos.TOP_LEFT);
        sp1.setStyle("-fx-background-color:#333333,#EAA956;-fx-background-insets:0,1.5;-fx-opacity:.92;-fx-background-radius:0px 0px 0px 5px;");
        sp1.setPrefSize(boxBounds.getWidth(), boxBounds.getHeight()-ACTION_BOX_HGT);
        //sp1.autosize();

        Button applyBtn = new Button("Apply filter");
        applyBtn.setId("treeviewApplyButton");
        searchFieldListHaplogroup = new TextField();
        searchFieldListHaplogroup.setId("treeviewSearchField");
        searchFieldListHaplogroup.setPrefSize(50,10);


        tree = new TreeHaplo("Haplo tree");
        tree.addStructure();
        createTreeMap(tree.getRootItem());


        /*

                        configure "apply filter" button


         */

        applyBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent paramT) {
                if(searchFieldListHaplogroup.getText().equals(""))
                    applyFilterFunction();
                else
                    applyFilterFunctionList();

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
                    applyFilterFunction();
                }
            }
        });

        searchFieldListHaplogroup.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    applyFilterFunctionList();
                }
            }
        });



        Label infolabel = new Label("Please select haplogroups either in the tree or specify a list:");
        infolabel.setMinSize(80, 80);

        Label haploLabel = new Label("Comma separated list of haplogroups:");
        infolabel.setMinSize(20, 30);

        VBox hb2 = new VBox();
        hb2.setId("treeView-inner-tree");
        hb2.getChildren().addAll(infolabel, tree.getTree(), haploLabel, searchFieldListHaplogroup , applyBtn);
        hb2.setSpacing(10);
        sp1.getChildren().addAll(hb2);

        StackPane sp2 = new StackPane();
        sp2.setPrefSize(100, ACTION_BOX_HGT);
        sp2.getChildren().add(searchLbl);
        sp2.setStyle("-fx-cursor:hand;-fx-background-color:#EAA956;-fx-border-width:0px 1px 1px 1px;-fx-border-color:#333333;-fx-opacity:.92;-fx-border-radius:0px 0px 5px 5px;-fx-background-radius:0px 0px 5px 5px;");
        sp2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent paramT) {
                togglePaneVisibility();
            }
        });

        searchPane.getChildren().addAll(sp1, sp2);
        Group searchpaneGroup = new Group();
        searchpaneGroup.getChildren().add(searchPane);
        root.getChildren().add(searchpaneGroup);
    }


    private void applyFilterFunction(){

        ObservableList<TreeItem<String>> itemSelection =  tree.getTree().getSelectionModel().getSelectedItems();

        if(itemSelection.size() > 0){
            seletcion_haplogroups = new String[itemSelection.size()];
            for(int i = 0; i < itemSelection.size(); i++){
                seletcion_haplogroups[i] = itemSelection.get(i).getValue();
            }
            // if selection has size zero --> take all haplogroups
            if(seletcion_haplogroups.length == 0){
                TableColumn haplo_col = tableManager.getTableColumnByName("Haplogroup");
                List<String> columnData = new ArrayList<>();
                for (Object item : tableManager.getTable().getItems()) {
                    columnData.add((String)haplo_col.getCellObservableValue(item).getValue());
                }
                seletcion_haplogroups = columnData.toArray(new String[columnData.size()]);
            } else { // get all sub-haplo-groups of selected HG's
                List<String> HGs = getAllSubgroups(seletcion_haplogroups);
                seletcion_haplogroups = HGs.toArray(new String[HGs.size()]);
            }

            // close tree view
            timelineUp.play();
            searchLbl.setGraphic(downArrow);
            togglePaneVisibility();
            tree.getTree().getSelectionModel().clearSelection();

            // parse selection to tablefilter
            TableSelectionFilter tableFilter = new TableSelectionFilter();

            if (seletcion_haplogroups.length !=0) {
                tableFilter.haplogroupFilter(tableManager, seletcion_haplogroups, tableManager.getColIndex("Haplogroup"));
            }

        }

    }


    private void applyFilterFunctionList(){

        // parse haplogroup list, get all corresponding table entries (subgroups included)
        // and show results in table

        if(!searchFieldListHaplogroup.getText().equals("") && tableManager.getTable().getItems().size() > 0){
            List<String> allHaplogroups = getAllSubgroups(searchFieldListHaplogroup.getText().split(","));

            seletcion_haplogroups = allHaplogroups.toArray(new String[allHaplogroups.size()]);

            // close tree view
            timelineUp.play();
            searchLbl.setGraphic(downArrow);
            togglePaneVisibility();
            tree.getTree().getSelectionModel().clearSelection();

            // parse selection to tablefilter
            TableSelectionFilter tableFilter = new TableSelectionFilter();

            if (seletcion_haplogroups.length !=0) {
                tableFilter.haplogroupFilter(tableManager, seletcion_haplogroups, tableManager.getColIndex("Haplogroup"));
            }
        }


    }

    private void setAnimation(){
		/* Initial position setting for Top Pane*/
        clipRect = new Rectangle();
        clipRect.setWidth(boxBounds.getWidth());
        clipRect.setHeight(ACTION_BOX_HGT);
        clipRect.translateYProperty().set(boxBounds.getHeight()-ACTION_BOX_HGT);
        searchPane.setClip(clipRect);
        searchPane.translateYProperty().set(-(boxBounds.getHeight()-ACTION_BOX_HGT));

		/* Animation for bouncing effect. */
        final Timeline timelineDown1 = new Timeline();
        timelineDown1.setCycleCount(2);
        timelineDown1.setAutoReverse(true);
        final KeyValue kv1 = new KeyValue(clipRect.heightProperty(), (boxBounds.getHeight()-15));
        final KeyValue kv2 = new KeyValue(clipRect.translateYProperty(), 15);
        final KeyValue kv3 = new KeyValue(searchPane.translateYProperty(), -15);
        final KeyFrame kf1 = new KeyFrame(Duration.millis(100), kv1, kv2, kv3);
        timelineDown1.getKeyFrames().add(kf1);

		/* Event handler to call bouncing effect after the scroll down is finished. */
        EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                timelineDown1.play();
            }
        };

        timelineDown = new Timeline();
        timelineUp = new Timeline();

        /* Animation for scroll down. */
        timelineDown.setCycleCount(1);
        timelineDown.setAutoReverse(true);
        final KeyValue kvDwn1 = new KeyValue(clipRect.heightProperty(), boxBounds.getHeight());
        final KeyValue kvDwn2 = new KeyValue(clipRect.translateYProperty(), 0);
        final KeyValue kvDwn3 = new KeyValue(searchPane.translateYProperty(), 0);
        final KeyFrame kfDwn = new KeyFrame(Duration.millis(200), onFinished, kvDwn1, kvDwn2, kvDwn3);
        timelineDown.getKeyFrames().add(kfDwn);

		/* Animation for scroll up. */
        timelineUp.setCycleCount(1);
        timelineUp.setAutoReverse(true);
        final KeyValue kvUp1 = new KeyValue(clipRect.heightProperty(), ACTION_BOX_HGT);
        final KeyValue kvUp2 = new KeyValue(clipRect.translateYProperty(), boxBounds.getHeight()-ACTION_BOX_HGT);
        final KeyValue kvUp3 = new KeyValue(searchPane.translateYProperty(), -(boxBounds.getHeight()-ACTION_BOX_HGT));
        final KeyFrame kfUp = new KeyFrame(Duration.millis(200), kvUp1, kvUp2, kvUp3);
        timelineUp.getKeyFrames().add(kfUp);
    }



    /**
     * Method to toggle the search pane visibility.
     */
    private void togglePaneVisibility(){
        if(isExpanded.get()){
            isExpanded.set(false);
        }else{
            isExpanded.set(true);
        }
    }



    /**
     * get all sub-groups of haplo
     * @param haplo_list
     */
    private List<String> getAllSubgroups(String[] haplo_list){
        List<String> haplo_list_extended = new ArrayList<String>();

        // iterate over all specified haplogroups
        // get subtree of each
        for(String haplo : haplo_list){
            // get sub-haplogroups of 'haplo'
            List<String> path = treeMap.get(haplo.toString().trim().toUpperCase());

            // add them to list
            haplo_list_extended.addAll(path);
        }

        return haplo_list_extended;

    }



    private void createTreeMap(TreeItem item){

        TreeIterator<String> iterator = new TreeIterator<>(item);
        TreeItem it = item;
        while (iterator.hasNext()) {
            treeMap.put(it.getValue().toString(), getSubtree(it));
            treeMap_leaf_to_root.put(it.getValue().toString(), getPathToRoot(it));
            node_to_children.put(it.getValue().toString(), it.getChildren());
            it = iterator.next();
        }
    }


    private List<String> getPathToRoot(TreeItem it){

        List<String> path_to_root = new ArrayList<>();
        while(it.getParent()!=null){
            path_to_root.add(it.getParent().getValue().toString());
            it = it.getParent();
        }


        return path_to_root;

    }

    private List<String> getSubtree(TreeItem root){

        if(root.getValue().toString().equals("H")){
            System.out.println("");
        }

        List<String> path = new ArrayList<String>();
        TreeIterator<String> iterator = new TreeIterator<>(root);
        TreeItem it = iterator.next();
        while (iterator.hasNext()) {
            path.add(it.getValue().toString());
            it = iterator.next();

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
}