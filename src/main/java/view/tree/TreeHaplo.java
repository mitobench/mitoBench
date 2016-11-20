package view.tree;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by neukamm on 09.11.16.
 */
public class TreeHaplo{

    private TreeItem<String> finalTree;
    private TreeView<String> tree;

    public TreeHaplo(String root){

        finalTree = new TreeItem<String> (root);
        finalTree.setExpanded(true);

    }

    public void addStructure() throws IOException, SAXException, ParserConfigurationException {

        //finalTree = PhyloTreeParser.getFinalTree();
        //PhyloTreeParser p = new PhyloTreeParser();
        //finalTree = PhyloTreeParser.getFinalTree();


        tree = new TreeView<String> (finalTree);
        tree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    public TreeItem<String> getRootItem() {
        return finalTree;
    }

    public TreeView<String> getTree() {
        return tree;
    }
}
