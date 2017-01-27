package gui.GUI_functionality;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import org.junit.Test;
import org.xml.sax.SAXException;
import view.charts.ChartController;
import view.table.TableController;

import javax.swing.text.TabableView;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


import static junit.framework.TestCase.assertEquals;

/**
 * Created by neukamm on 27.01.2017.
 */
public class MethodTests {

    private ChartController chartController;
    private TableController tableController;

    private void setUp() throws ParserConfigurationException, SAXException, IOException {
        HashMap<String, List<String>> treeMap = new HashMap<>();

        tableController = new TableController();
        chartController = new ChartController();

    }



    @Test
    public void chartController() throws IOException, SAXException, ParserConfigurationException {
        setUp();

        // test method "roundValue()"
        double val1 = 0.01234;
        double val2 = 3.5653;
        assertEquals(0.01, chartController.roundValue(val1));
        assertEquals(3.57, chartController.roundValue(val2));

    }


}
