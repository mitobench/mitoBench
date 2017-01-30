package statistics;

import javafx.application.Application;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;
import view.MitoBenchWindow;
import view.table.TableController;
import view.tree.TreeHaploController;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by neukamm on 30.01.17.
 */
public class HaploStatsTest {

    private TreeHaploController treeController;
    private HaploStatistics haploStatistics;
    private TableController tableController;


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
    public void setUp() throws IOException, ParserConfigurationException, SAXException {
        tableController = new TableController();
        treeController = new TreeHaploController(tableController);
        haploStatistics = new HaploStatistics(tableController, treeController);

    }


    @Test
    public void methodsTest() throws IOException {

        haploStatistics.setNumber_of_groups(4);
        assertEquals(4, haploStatistics.getNumber_of_groups());
        assertEquals(tableController, haploStatistics.getTableController());

    }

}
