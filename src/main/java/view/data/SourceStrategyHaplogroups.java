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
    @Override
    public WeightedTreeItem<String> getData(HashMap<String, List<String>> hg_to_group, HashMap<String,
                                            HashMap<String, Integer>> weights,
                                            HashMap<String, List<String>> treeMap,
                                            TreeItem<String> tree) {

        //return getHaploDataWeightedTree(hg_to_group, weights, treeMap, tree);
        return getHaploDataWeightedTree(hg_to_group, weights);
    }


    private WeightedTreeItem<String> getHaploDataWeightedTree(HashMap<String, List<String>> hg_to_group, HashMap<String, HashMap<String, Integer>> weights){
        WeightedTreeItem<String> root = new WeightedTreeItem(1, "Haplogroups");


        List<WeightedTreeItem<String>> rootItems = new ArrayList<>();
        for(String group : hg_to_group.keySet()){
            // weight == number of haplogroups per group
            int weightGroup = new HashSet<String>(hg_to_group.get(group)).size();
            WeightedTreeItem<String> item = new WeightedTreeItem(weightGroup, group);
            rootItems.add(item);

            Set<String> uniqueHGs = new HashSet<>(hg_to_group.get(group));
            for(String hg : uniqueHGs){
                int weight = weights.get(group).get(hg);

                WeightedTreeItem<String> subItem = new WeightedTreeItem(weight, hg);
                item.getChildren().add(subItem);
            }
        }

        root.getChildren().addAll(rootItems);

        return root;
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
