package controller;

/**
 * Created by neukamm on 09.11.16.
 */

import Logging.LogClass;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import model.HaploTreeModel;
import model.TreeIterator;
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

    private Logger LOG;

    private String[] seletcion_haplogroups;
    private ATableController tableManager;
    private HaploTreeModel tree;
    private HashMap<String, List<String>> treeMap;
    private HashMap<String, List<String>> treeMap_leaf_to_root;
    private HashMap<String, List<String>> node_to_children;
    private view.tree.TreeView treeView;

    public HaplotreeController(ATableController tableManager, LogClass logClass) throws IOException, SAXException, ParserConfigurationException {

        this.tableManager = tableManager;
        LOG = logClass.getLogger(this.getClass());

        treeMap = new HashMap<>();
        treeMap_leaf_to_root = new HashMap<>();
        node_to_children = new HashMap<>();

        tree = new HaploTreeModel("Haplo tree");
        tree.addStructure();
        createTreeMap(tree.getRootItem());

    }



    /**
     * Method to configure the search pane.
     * @param
     * @param dataFilteringTreebasedDialogue
     */
    public void setKeyEvents(DataFilteringTreebasedDialogue dataFilteringTreebasedDialogue) throws IOException, SAXException, ParserConfigurationException{

        treeView.getApplyBtn().setOnAction(paramT -> {
            if (tableManager.getTable().getItems().size() > 0){
                if(treeView.getSearchFieldListHaplogroup().getText().equals("")){
                    applyFilterFunction(dataFilteringTreebasedDialogue);
                } else {
                    applyFilterFunctionList(dataFilteringTreebasedDialogue);
                }
            }
        });

        tree.getTree().setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                applyFilterFunction(dataFilteringTreebasedDialogue);
            }
        });

        treeView.getSearchFieldListHaplogroup().setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                applyFilterFunctionList(dataFilteringTreebasedDialogue);
            }
        });


    }


    /**
     * This method applies HG filtering based on list of HGs specified by user.
     *
     * @param dataFilteringTreebasedDialogue
     */
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


    /**
     * This method applies the filtering based on selected items in phyloTree.
     *
     * @param dataFilteringTreebasedDialogue
     */
    private void applyFilterFunctionList(DataFilteringTreebasedDialogue dataFilteringTreebasedDialogue){

        // parse haplogroup list, get all corresponding table entries (subgroups included)
        // and show results in table

        if(!treeView.getSearchFieldListHaplogroup().getText().equals("") && tableManager.getTable().getItems().size() > 0){
            List<String> allHaplogroups = getAllSubgroups(treeView.getSearchFieldListHaplogroup().getText().split(","));

            seletcion_haplogroups = allHaplogroups.toArray(new String[allHaplogroups.size()]);

            // close tree view
            tree.getTree().getSelectionModel().clearSelection();

            // parse selection to tablefilter
            TableSelectionFilter tableFilter = new TableSelectionFilter();

            if (seletcion_haplogroups.length !=0) {
                LOG.info("Filter data. Table now contains only data of Haplotype: " +
                        Arrays.toString(treeView.getSearchFieldListHaplogroup().getText().split(",")));

                tableFilter.haplogroupFilter(tableManager, seletcion_haplogroups,
                        tableManager.getColIndex("Haplogroup"), LOG);
                treeView.getSearchFieldListHaplogroup().setText("");
            }

        }
        dataFilteringTreebasedDialogue.close();

    }


    /**
     * This method returns a list of Haplogroups that are below tree item (HG) for each HG in list.
     *
     * @param haplo_list
     */
    private List<String> getAllSubgroups(String[] haplo_list){
        List<String> haplo_list_extended = new ArrayList<String>();

        // iterate over all specified haplogroups, get subtree of each
        for(String haplo : haplo_list){
            // get sub-haplogroups of 'haplo'
            List<String> path = treeMap.get(haplo.toString().trim());

            // add them to list
            haplo_list_extended.addAll(path);
        }

        return haplo_list_extended;

    }

    /**
     * This method assigns to each tree item the corresponding subtree.
     *
     * @param item
     */

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
     * This method gets all nodes on the path from treenode to root
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


    /**
     * This method accepts String as input and returns treeItem with item ID == string.
     *
     * @param item_name
     * @return
     */

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

    /**
     * This method returns all nodes below certain node (= subtree of node).
     *
     * @param root
     * @return
     */
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

    public HaploTreeModel getTree() {
        return tree;
    }

    public HashMap<String, List<String>> getTreeMap_leaf_to_root() {
        return treeMap_leaf_to_root;
    }

    public view.tree.TreeView getTreeView(){
        return treeView;
    }

    public void setTreeView(view.tree.TreeView treeView){
        this.treeView = treeView;
    }

}