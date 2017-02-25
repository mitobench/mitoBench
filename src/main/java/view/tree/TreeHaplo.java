package view.tree;

import io.MutationParser;
import io.PhyloTreeParser;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 09.11.16.
 */
public class TreeHaplo{

    private TreeItem<String> finalTree;
    private TreeView<String> tree;
    private HashMap<String, List<String>> hgs_per_mutation;

    public TreeHaplo(String root){
        finalTree = new TreeItem<String> (root);
        finalTree.setExpanded(true);
    }

    public void addStructure() throws IOException {

        PhyloTreeParser p = new PhyloTreeParser();
        finalTree = p.getFinalTree();
        tree = new TreeView<String> (finalTree);
        tree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        MutationParser mutationParser = p.getMutationParser();
        hgs_per_mutation = mutationParser.getMutation_to_hg();

    }

    public TreeItem<String> getRootItem() {
        return finalTree;
    }

    public TreeView<String> getTree() {
        return tree;
    }

    public HashMap<String, List<String>> getHgs_per_mutation() {
        return hgs_per_mutation;
    }
}
