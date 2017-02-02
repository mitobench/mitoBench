package gui.GUI_functionality;

import io.PhyloTreeParser;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import view.table.TableControllerUserBench;
import view.tree.TreeHaplo;
import view.tree.HaplotreeController;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by neukamm on 27.01.2017.
 */
public class TreeTest {


    private TreeHaplo treeHaplo;
    private HaplotreeController treeController;


    @Before
    public void setUp() throws IOException, ParserConfigurationException, SAXException {

        treeHaplo = new TreeHaplo("Haplo Tree");
        treeHaplo.addStructure();
        TableControllerUserBench tableController = new TableControllerUserBench();
        tableController.init();
        treeController = new HaplotreeController(tableController);
        treeController.configureSearch(new Pane());
        treeController.setAnimation();

    }

    @Test
    public void treeTest() throws IOException {

        System.out.println("Test if tree is initialized correct.");
        assertEquals(new TreeItem<String>("RSRS").getValue(), treeHaplo.getRootItem().getValue());

        System.out.println("Test if treeView was initialized correct.");
        PhyloTreeParser p = new PhyloTreeParser();
        TreeItem<String> finalTree = p.getFinalTree();
        TreeView<String> tree = new TreeView<>(finalTree);
        assertEquals(tree.getHeight(), treeHaplo.getTree().getHeight());


        // test method "togglePaneVisibility()"
        System.out.println("test method togglePaneVisibility()");
        treeController.setIsExpanded(false);
        treeController.togglePaneVisibility();
        assertTrue(treeController.isIsExpanded());

        // test method getAllSubgroups()

        // test treeMap
        HashMap<String, List<String>> treeMap = treeController.getTreeMap_leaf_to_root();
        List<String> pathH = Arrays.asList("L0", "RSRS");
        assertEquals(pathH, treeMap.get("L0d"));

    }

}
