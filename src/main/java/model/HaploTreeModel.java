package model;

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
public class HaploTreeModel {

    private TreeItem<String> finalTree;
    private TreeView<String> tree;
    private HashMap<String, List<String>> hgs_per_mutation;
    private HashMap<String, String[]> mutations_per_hg;

    public HaploTreeModel(String root){
        finalTree = new TreeItem<> (root);
        finalTree.setExpanded(true);
    }

    public void addStructure() throws IOException {

        PhyloTreeParser p = new PhyloTreeParser();
        finalTree = p.getFinalTree();
        tree = new TreeView<String> (finalTree);
        tree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        MutationParser mutationParser = p.getMutationParser();
        hgs_per_mutation = mutationParser.getMutation_to_hg();
        mutations_per_hg = mutationParser.getHg_to_mutation();

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
    public HashMap<String, String[]> getMutations_per_hg() { return mutations_per_hg; }
}
