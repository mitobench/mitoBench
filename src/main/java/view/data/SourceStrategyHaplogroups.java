package view.data;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import view.controls.sunburst.WeightedTreeItem;
import view.tree.TreeIterator;

import java.util.*;

/**
 * A dummy implementation of mockup view.data.
 * Created by n0daft on 09.05.2014.
 */

public class SourceStrategyHaplogroups implements ISourceStrategy {

    private TreeItem<String> tree;
    private HashMap<String, HashMap<String, Integer>> weights;
    private HashMap<Integer, List<String>> nodes_to_level;
    private TreeView treeView;
    private HashMap<String, List<String>> treemap;
    private WeightedTreeItem<String> root;

    @Override
    public WeightedTreeItem<String> getData(HashMap<String, List<String>> hg_to_group,
                                            HashMap<String, HashMap<String, Integer>> weights,
                                            HashMap<String, List<String>> treeMap,
                                            TreeItem<String> tree,
                                            TreeView treeView) {

        this.tree = tree;
        this.weights = weights;
        this.treeView = treeView;
        this.nodes_to_level = new HashMap<>();
        this.treemap = treeMap;
        return getHaploDataWeightedTree(hg_to_group);
    }


    /**
     * This method creates weighted tree items for each group and starts creation of all other
     * items.
     *
     * @param hg_to_group
     * @return
     */
    private WeightedTreeItem<String> getHaploDataWeightedTree(HashMap<String, List<String>> hg_to_group) {
        // create inner circle "Haplogroups"
        this.root = new WeightedTreeItem(1, "Haplogroups");

        // List to add all groups
        List<WeightedTreeItem<String>> rootItems = new ArrayList<>();

        for(String group : hg_to_group.keySet()){
            // weight == number of haplogroups per group
            List<String> groupElements = hg_to_group.get(group);
            int weightGroup = groupElements.size();
            // create group-donut-part
            WeightedTreeItem<String> item = new WeightedTreeItem(weightGroup, group);
            rootItems.add(item);
            root.getChildren().add(item);

            nodes_to_level.clear();
            Set<String> uniqueHGs = new HashSet<>(hg_to_group.get(group));
            setNodes_to_level(uniqueHGs);
            getSubDonuts(item, group, uniqueHGs.size());

        }
        return root;
    }


    /**
     * This method assigns to each level all nodes located at this level of the phylo tree
     *
     * @param uniqueHGs
     */
    private void setNodes_to_level(Set<String> uniqueHGs){

        TreeIterator<String> iterator = new TreeIterator<>(tree);
        TreeItem it = tree;

        while (iterator.hasNext()) {
           for(String hg : uniqueHGs){
               if(it.getValue().toString().equals(hg)){
                   int level = treeView.getTreeItemLevel(it);
                   if(!nodes_to_level.containsKey(level)){
                       List<String> l = new ArrayList<>();
                       l.add(hg);
                       nodes_to_level.put(level, l);
                   } else {
                       List<String> tmp = nodes_to_level.get(level);
                       tmp.add(hg);
                       nodes_to_level.put(level, tmp);
                   }
               }
           }
           it = iterator.next();
        }
    }


    /** This method creates for each table entry a weighted tree item and assigns (hierarchically correct) the parent
     *  item.
     *
     * @param item
     * @param group
     * @param maxItems
     */
    private void getSubDonuts(WeightedTreeItem<String> item, String group, int maxItems){
        // get sorted level list
        List<Integer> levels_sorted = new ArrayList(new TreeSet(nodes_to_level.keySet()));
        boolean itemCreated = false;
        String hg_this_level;
        String hg_upper;

        // set first donut, add each element to group-donut-component
        List<String> hgs_of_this_level = nodes_to_level.get(levels_sorted.get(0));
        for(int i = 0; i < hgs_of_this_level.size(); i++){
            int weight = weights.get(group).get(hgs_of_this_level.get(i));
            WeightedTreeItem<String> subItem = new WeightedTreeItem(weight, hgs_of_this_level.get(i));
            item.getChildren().add(subItem);
        }
        // void to add too many children
        if(maxItems > item.getChildren().size()){
            // add all following donut components
            for(int j = 1; j < levels_sorted.size(); j++){

                List<String> hgs_of_upper_level = nodes_to_level.get(levels_sorted.get(j-1));
                hgs_of_this_level = nodes_to_level.get(levels_sorted.get(j));

                for(int k = 0; k < hgs_of_this_level.size(); k++){

                    hg_this_level = hgs_of_this_level.get(k);

                    for(int i = 0; i < hgs_of_upper_level.size(); i++){
                        hg_upper = hgs_of_upper_level.get(i);
                        List<String> path_to_root = treemap.get(hg_this_level);
                        if(path_to_root.contains(hg_upper)){
                            int weight = weights.get(group).get(hg_this_level);
                            WeightedTreeItem newItem = new WeightedTreeItem(weight, hg_this_level);
                            // get item of upper HG, add new item as child
                            getItem(hg_upper).getChildren().add(newItem);
                            itemCreated = true;
                        }
                    }
                    // if item cannot included to one of the items before (no hierarchically relation),
                    // item is added to group item
                    if(!itemCreated){
                        int weight = weights.get(group).get(hg_this_level);
                        WeightedTreeItem newItem = new WeightedTreeItem(weight, hg_this_level);
                        item.getChildren().add(newItem);
                    }
                    itemCreated=false;
                }

            }
        }

    }


    /**
     * This method returns the weighted tree item of a specific haplogroup.
     *
     * @param hg
     * @return
     */
    private WeightedTreeItem getItem(String hg){

        TreeIterator<String> iterator = new TreeIterator<>(root);
        WeightedTreeItem it = root;

        while (iterator.hasNext()) {
            for(Object item : it.getChildren()){
                WeightedTreeItem item1 = (WeightedTreeItem) item;
                if(item1.getValue().toString().equals(hg)){
                    return item1;
                }
            }
            it = (WeightedTreeItem) iterator.next();
        }
        return null;
    }


}
