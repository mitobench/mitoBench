package view.data;

import view.controls.sunburst.WeightedTreeItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            WeightedTreeItem<String> item = new WeightedTreeItem(hg_to_group.get(group).size(), group);
            rootItems.add(item);

            for(String hg : hg_to_group.get(group)){
                int weight = weights.get(group).get(hg);

                WeightedTreeItem<String> subItem = new WeightedTreeItem(weight, hg);
                item.getChildren().add(subItem);
            }
        }

        root.getChildren().addAll(rootItems);

        return root;
    }
}
