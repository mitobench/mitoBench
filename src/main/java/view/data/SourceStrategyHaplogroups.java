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
                                            TreeView tree) {

        return getHaploDataWeightedTree(hg_to_group, weights, treeMap, tree);
    }


    /**
     * parse haplo view.data
     * @return
     */
    private WeightedTreeItem<String> getHaploDataWeightedTree(HashMap<String, List<String>> hg_to_group,
                                                              HashMap<String, HashMap<String, Integer>> weights,
                                                              HashMap<String, List<String>> treeMap,
                                                              TreeView tree){

        // make copy of tree
        TreeView t_copy = tree;

        WeightedTreeItem<String> root = new WeightedTreeItem(1, "Haplogroups");


        List<WeightedTreeItem<String>> rootItems = new ArrayList<>();

        for(String group : hg_to_group.keySet()){

            /*
                            group items
             */

            // weight == number of haplogroups per group
            int weightGroup = new HashSet<String>(hg_to_group.get(group)).size();
            WeightedTreeItem<String> item = new WeightedTreeItem(weightGroup, group);
            rootItems.add(item);

            /*

                            hg items for each level

             */

            Set<String> uniqueHGs = new HashSet<>(hg_to_group.get(group));
            TreeView tree_new = removeUnusedTreeItems(uniqueHGs, t_copy);



            // iterate over new tree, create items

            TreeIterator<String> iterator = new TreeIterator<>(tree_new.getRoot());
            TreeItem it = tree_new.getRoot();
            while (iterator.hasNext()) {
                // ....... do something
                System.out.println(it.getValue().toString());
                it = iterator.next();
            }



//            HashMap<Integer, List<String>> treeHGHierarchy = getHierarchy(uniqueHGs, treeMap);
//
//
//            // get sorted level list
//            List<Integer> list = new ArrayList<Integer>(treeHGHierarchy.keySet());
//            Collections.sort(list);
//
//            List<WeightedTreeItem<String>> itemsSameLevel = new ArrayList<>();
//
//            for(int i = 0; i < list.size(); i++){
//                List<String> HG_same_level = treeHGHierarchy.get(list.get(i));
//
//                for(String hg : HG_same_level){
//                    int weight = weights.get(group).get(hg);
//                    WeightedTreeItem<String> subItem = new WeightedTreeItem(weight, hg);
//                    itemsSameLevel.add(subItem);
//                }
//
//                item.getChildren().addAll(itemsSameLevel);
//                item = itemsSameLevel.get(0);
//                itemsSameLevel.clear();
//            }

        }



        root.getChildren().addAll(rootItems);

        return root;
    }

    private HashMap<Integer, List<String>> getHierarchy(Set<String> uniqueHGs, HashMap<String, List<String>> treeMap){
        // IDEA: sort uniqueHGs, root -> leaves
        HashMap<Integer, List<String>> level_to_HG = new HashMap<>();
        for(String s : uniqueHGs){
            List<String> subHGs = treeMap.get(s.toLowerCase());
            int level;
            if(subHGs == null){
                level = 0;
            } else {
                level = subHGs.size();
            }


            if(!level_to_HG.containsKey(level)){ // create new entry
                List<String> l = new ArrayList<String>();
                l.add(s);
                level_to_HG.put(level, l);
            } else {
                List<String> l2 = level_to_HG.get(level);// update entry
                l2.add(s);
                level_to_HG.put(level, l2);
            }

        }
        return level_to_HG;
    }

    private TreeView removeUnusedTreeItems(Set<String> HGs, TreeView tree){
        for(String s : HGs){
            // iterate over tree, delete items that are NOT included in HG list
            // CAUTION! set new parents / children
            TreeIterator<String> iterator = new TreeIterator<>(tree.getRoot());
            TreeItem it = tree.getRoot();
            while (iterator.hasNext()) {

                if(!it.getValue().toString().equals(s)){
                    if(it.getParent()!=null && it.getChildren()!=null){
                        it.getParent().getChildren().addAll(it.getChildren());
                        it.getParent().getChildren().remove(it);
                    } else if(it.isLeaf()){
                        it.getParent().getChildren().remove(it);
                    }
                }

                it = iterator.next();
            }
        }

        return tree;

    }
}
