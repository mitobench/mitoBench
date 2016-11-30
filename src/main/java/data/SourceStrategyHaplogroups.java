package data;

import controls.sunburst.WeightedTreeItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A dummy implementation of mockup data.
 * Created by n0daft on 09.05.2014.
 */
public class SourceStrategyHaplogroups implements ISourceStrategy {
    @Override
    public WeightedTreeItem<String> getData(HashMap<String, List<String>> hg_to_group) {
        return getHaploDataWeightedTree(hg_to_group);
    }


    /**
     * parse haplo data
     * @return
     */
    private WeightedTreeItem<String> getHaploDataWeightedTree(HashMap<String, List<String>> hg_to_group){
        // todo: weight = number of this HG in this group
        WeightedTreeItem<String> root = new WeightedTreeItem(1, "Haplogroups");

        List<WeightedTreeItem<String>> rootItems = new ArrayList<>();
        for(String key : hg_to_group.keySet()){
            WeightedTreeItem<String> item = new WeightedTreeItem(1, key);
            rootItems.add(item);

            for(String s : hg_to_group.get(key)){
                WeightedTreeItem<String> subItem = new WeightedTreeItem(5, s);
                item.getChildren().add(subItem);
            }
        }

        root.getChildren().addAll(rootItems);

        return root;
    }
}
