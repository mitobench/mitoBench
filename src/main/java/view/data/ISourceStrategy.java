package view.data;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import view.controls.sunburst.WeightedTreeItem;

import java.util.HashMap;
import java.util.List;

/**
 * Represents a strategy by which the view.data for the control is received.
 * Created by n0daft on 09.05.2014.
 */
public interface ISourceStrategy {

    /**
     * Returns the view.data model represented by the root item.
     * We expect the model to be a tree model.
     * @return
     */
    WeightedTreeItem<String> getData(HashMap<String, List<String>> hg_to_group,
                                     HashMap<String, HashMap<String, Integer>> weights,
                                     HashMap<String, List<String>> treeMap,
                                     TreeItem<String> tree,
                                     TreeView treeView);

}
