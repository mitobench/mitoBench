package view.data;

import view.controls.sunburst.WeightedTreeItem;

import java.util.*;

/**
 * A dummy implementation of mockup view.data.
 * Created by n0daft on 09.05.2014.
 */
public class SourceStrategyHaplogroups implements ISourceStrategy {
    @Override
    public WeightedTreeItem<String> getData(HashMap<String, List<String>> hg_to_group, HashMap<String, HashMap<String, Integer>> weights) {
        return getHaploDataWeightedTree(hg_to_group, weights);
    }


    /**
     * parse haplo view.data
     * @return
     */
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
}
