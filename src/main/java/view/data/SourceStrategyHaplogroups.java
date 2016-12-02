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
    private HashMap<String, List<String>> node_to_children;
    private HashMap<Integer, List<String>> nodes_to_level;
    private TreeView treeView;
    private HashMap<String, List<String>> treemap;


    @Override
    public WeightedTreeItem<String> getData(HashMap<String, List<String>> hg_to_group, HashMap<String,
                                            HashMap<String, Integer>> weights,
                                            HashMap<String, List<String>> treeMap,
                                            TreeItem<String> tree,
                                            HashMap<String, List<String>> note_to_children,
                                            TreeView treeView) {

        this.tree = tree;
        this.weights = weights;
        this.node_to_children = note_to_children;
        //return getHaploDataWeightedTree(hg_to_group, weights, treeMap, tree);
        this.treeView = treeView;
        this.nodes_to_level = new HashMap<>();
        this.treemap = treeMap;
        return getHaploDataWeightedTree(hg_to_group);
    }


    private WeightedTreeItem<String> getHaploDataWeightedTree(HashMap<String, List<String>> hg_to_group) {
        // create inner circle "Haplogroups"
        WeightedTreeItem<String> root = new WeightedTreeItem(1, "Haplogroups");

        // List to add all groups
        List<WeightedTreeItem<String>> rootItems = new ArrayList<>();

        for(String group : hg_to_group.keySet()){
            // weight == number of haplogroups per group
            int weightGroup = new HashSet<String>(hg_to_group.get(group)).size();
            // create group-donut-part
            WeightedTreeItem<String> item = new WeightedTreeItem(weightGroup, group);
            rootItems.add(item);

            nodes_to_level.clear();
            Set<String> uniqueHGs = new HashSet<>(hg_to_group.get(group));
            List<String> hgs_sorted = sortHGs(uniqueHGs);
            //for(String hg : hgs_sorted){
                //System.out.println(hg);
                // get all haplogroups of the group and add to group-donut-part
                // weight = number of occurrences
                //int weight = weights.get(group).get(hg);
                //WeightedTreeItem<String> subItem = new WeightedTreeItem(weight, hg);
                getSubDonuts(item, group, uniqueHGs.size());
                //item.getChildren().add(subItem);
           // }
        }

        root.getChildren().addAll(rootItems);

        return root;
    }


    private List<String> sortHGs(Set<String> uniqueHGs){

        List<String> sortedHGs = new ArrayList<>();

        TreeIterator<String> iterator = new TreeIterator<>(tree);
        TreeItem it = tree;

        while (iterator.hasNext()) {
           for(String hg : uniqueHGs){
               if(it.getValue().toString().equals(hg)){
                   sortedHGs.add(hg);
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
        return sortedHGs;
    }

    private void getSubDonuts(WeightedTreeItem<String> item, String group, int maxItems){


        // get sorted level list
        List<Integer> levels_sorted = new ArrayList(new TreeSet(nodes_to_level.keySet()));
        boolean itemCreated = false;
        String hg_this_level="";
        String hg_upper;
        WeightedTreeItem<String> subItem = null;

        // set first donut, add each element to group-donut-component
        List<String> hgs_of_this_level = nodes_to_level.get(levels_sorted.get(0));
        for(int i = 0; i < hgs_of_this_level.size(); i++){
            int weight = weights.get(group).get(hgs_of_this_level.get(i));
            subItem = new WeightedTreeItem(weight, hgs_of_this_level.get(i));
            item.getChildren().add(subItem);
        }

        if(maxItems > item.getChildren().size()){
            // add all following donut components

            for(int j = 1; j < levels_sorted.size(); j++){

                List<String> hgs_of_upper_level = nodes_to_level.get(levels_sorted.get(j-1));
                hgs_of_this_level = nodes_to_level.get(levels_sorted.get(j));

                for(int k = 0; k < hgs_of_upper_level.size(); k++){
                    hg_upper = hgs_of_upper_level.get(k);

                    for(int i = 0; i < hgs_of_this_level.size(); i++){
                        hg_this_level = hgs_of_this_level.get(i);
                        List<String> path_to_root = treemap.get(hg_this_level);
                        if(path_to_root.contains(hg_upper)){
                            item = subItem;
                            int weight = weights.get(group).get(hg_this_level);
                            subItem = new WeightedTreeItem(weight, hg_this_level);
                            item.getChildren().add(subItem);
                            itemCreated = true;
                        }
                    }
                }
                if(!itemCreated){
                    int weight = weights.get(group).get(hg_this_level);
                    subItem = new WeightedTreeItem(weight, hg_this_level);
                    item.getChildren().add(subItem);
                }
                itemCreated=false;
            }
        }

    }

//
//    /**
//     * parse haplo view.data
//     * @return
//     */
//    private WeightedTreeItem<String> getHaploDataWeightedTree(HashMap<String, List<String>> hg_to_group,
//                                                              HashMap<String, HashMap<String, Integer>> weights,
//                                                              HashMap<String, List<String>> treeMap,
//                                                              TreeItem<String> tree_root){
//
//
//        WeightedTreeItem<String> root = new WeightedTreeItem(1, "Haplogroups");
//
//
//        List<WeightedTreeItem<String>> rootItems = new ArrayList<>();
//
//
//        for(String group : hg_to_group.keySet()){
//            TreeItem tree_copy = deepcopy(tree_root);
//            /*
//                            group items
//             */
//
//            // weight == number of haplogroups per group
//            int weightGroup = new HashSet<String>(hg_to_group.get(group)).size();
//            WeightedTreeItem<String> item = new WeightedTreeItem(weightGroup, group);
//            rootItems.add(item);
//
//            /*
//
//                            hg items for each level
//
//             */
//
//            Set<String> uniqueHGs = new HashSet<>(hg_to_group.get(group));
//            TreeItem tree_new = removeUnusedTreeItems(uniqueHGs, tree_copy);
//
//            List<WeightedTreeItem<String>> its = new ArrayList<>();
//
//            // iterate over new tree, create items
//
//            TreeIterator<String> iterator = new TreeIterator<>(tree_new);
//            TreeItem it = tree_new;
//
//            while (iterator.hasNext()) {
//                if(it != tree_new){
//
//                    if(weights.containsKey(group) && weights.get(group).containsKey(it.getValue().toString())) {
//                        int weight = weights.get(group).get(it.getValue().toString());
//
//                        WeightedTreeItem<String> item1 = new WeightedTreeItem(weight, it.getValue().toString());
//                        List<WeightedTreeItem<String>> itemsChildren = new ArrayList<>();
//
////                        if(!it.isLeaf()){
////                            for (Object child : it.getChildren()) {
////
////                                TreeItem c = (TreeItem) child;
////                                HashMap h = weights.get(group);
////                                String n = c.getValue().toString();
////                                weight = (int)h.get(n);
////                                WeightedTreeItem<String> subItem = new WeightedTreeItem(weight, c.getValue().toString());
////                                itemsChildren.add(subItem);
////
////                            }
////                        }
//
//                        item1.getChildren().addAll(itemsChildren);
//                        item.getChildren().add(item1);
//
//                    }
//                }
//
//                it = iterator.next();
//            }
//
//
//
//
//
////            HashMap<Integer, List<String>> treeHGHierarchy = getHierarchy(uniqueHGs, treeMap);
////
////
////            // get sorted level list
////            List<Integer> list = new ArrayList<Integer>(treeHGHierarchy.keySet());
////            Collections.sort(list);
////
////            List<WeightedTreeItem<String>> itemsSameLevel = new ArrayList<>();
////
////            for(int i = 0; i < list.size(); i++){
////                List<String> HG_same_level = treeHGHierarchy.get(list.get(i));
////
////                for(String hg : HG_same_level){
////                    int weight = weights.get(group).get(hg);
////                    WeightedTreeItem<String> subItem = new WeightedTreeItem(weight, hg);
////                    itemsSameLevel.add(subItem);
////                }
////
////                item.getChildren().addAll(itemsSameLevel);
////                item = itemsSameLevel.get(0);
////                itemsSameLevel.clear();
////            }
//
//        }
//
//
//
//        root.getChildren().addAll(rootItems);
//
//        return root;
//    }
//
//    private HashMap<Integer, List<String>> getHierarchy(Set<String> uniqueHGs, HashMap<String, List<String>> treeMap){
//        // IDEA: sort uniqueHGs, root -> leaves
//        HashMap<Integer, List<String>> level_to_HG = new HashMap<>();
//        for(String s : uniqueHGs){
//            List<String> subHGs = treeMap.get(s.toLowerCase());
//            int level;
//            if(subHGs == null){
//                level = 0;
//            } else {
//                level = subHGs.size();
//            }
//
//
//            if(!level_to_HG.containsKey(level)){ // create new entry
//                List<String> l = new ArrayList<String>();
//                l.add(s);
//                level_to_HG.put(level, l);
//            } else {
//                List<String> l2 = level_to_HG.get(level);// update entry
//                l2.add(s);
//                level_to_HG.put(level, l2);
//            }
//
//        }
//        return level_to_HG;
//    }
//
//    private TreeItem removeUnusedTreeItems(Set<String> HGs, TreeItem tree_root){
//        //for(String s : HGs){
//            // iterate over tree, delete items that are NOT included in HG list
//            // CAUTION! set new parents / children
//            TreeIterator<String> iterator = new TreeIterator<>(tree_root);
//            TreeItem it = tree_root;
//
//            while (iterator.hasNext()) {
//
//                if(!HGs.contains(it.getValue().toString())){
//                    if(it.getParent()!=null && it.getChildren()!=null){
//                        it.getParent().getChildren().addAll(it.getChildren());
//                        it.getParent().getChildren().remove(it);
//                    } else if(it.isLeaf()){
//                        it.getParent().getChildren().remove(it);
//                    }
//                }
//
//                it = iterator.next();
//            }
//        //}
//
//        return tree_root;
//
//    }
//
//
//    /**
//     * get copy of tree
//     * @param item
//     * @return
//     */
//    public TreeItem<String> deepcopy(TreeItem<String> item) {
//        TreeItem<String> copy = new TreeItem<String>(item.getValue());
//        for (TreeItem<String> child : item.getChildren()) {
//            copy.getChildren().add(deepcopy(child));
//        }
//        return copy;
//    }
}
