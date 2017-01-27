package gui.GUI_functionality;

import io.PhyloTreeParser;
import javafx.application.Application;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;
import view.MitoBenchWindow;
import view.tree.TreeHaplo;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by neukamm on 27.01.2017.
 */
public class TreeTest {


    private TreeHaplo treeHaplo;

    @BeforeClass
    public static void setUpClass() throws InterruptedException {
        // Initialise Java FX

        System.out.printf("About to launch FX App\n");
        Thread t = new Thread("JavaFX Init Thread") {
            public void run() {
                Application.launch(MitoBenchWindow.class, new String[0]);
            }
        };
        t.setDaemon(true);
        t.start();
        System.out.printf("FX App thread started\n");
        Thread.sleep(500);
    }

    @Before
    public void setUp() throws IOException {

        treeHaplo = new TreeHaplo("Haplo Tree");
        treeHaplo.addStructure();
    }

    @Test
    public void treeTest() throws IOException {

        assertEquals(new TreeItem<String>("RSRS").getValue(), treeHaplo.getRootItem().getValue());

        PhyloTreeParser p = new PhyloTreeParser();
        TreeItem<String> finalTree = p.getFinalTree();
        TreeView<String> tree = new TreeView<>(finalTree);
        assertEquals(tree.getHeight(), treeHaplo.getTree().getHeight());
    }

}
