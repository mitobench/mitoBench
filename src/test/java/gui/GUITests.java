package gui;


import Logging.LogClass;
import io.PhyloTreeParser;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.testfx.api.FxRobot;
import statistics.HaploStatistics;
import view.MitoBenchWindow;
import view.charts.ChartController;
import view.groups.GroupController;
import view.table.controller.TableControllerUserBench;
import view.tree.HaplotreeController;
import view.tree.TreeHaplo;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static org.testfx.api.FxToolkit.registerPrimaryStage;
import static org.testfx.api.FxToolkit.setupApplication;


/**
 * Created by peltzer on 21/12/2016.
 * Basic GUI Testing is now implemented almost :-)
 */

@RunWith(MockitoJUnitRunner.Silent.class)
public class GUITests extends FxRobot implements GUITestValidator {

    private TableControllerUserBench tableController;
    private ChartController chartController;
    private GroupController groupController;
    private TreeHaplo treeHaplo;
    private HaplotreeController treeController;
    private HaploStatistics haploStatistics;
    private LogClass logClass;
    private GUITestFiles testFiles;

    @BeforeClass
    public static void setupSpec() throws Exception {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
        }
        System.setProperty("javafx.running", "true");
        registerPrimaryStage();
    }

    @Before
    public void setUp() throws Exception {

        logClass = new LogClass();
        logClass.setUp();
        tableController = new TableControllerUserBench(logClass);
        chartController = new ChartController();
        groupController = new GroupController(tableController);

        treeHaplo = new TreeHaplo("Haplo Tree");
        treeHaplo.addStructure();
        treeController = new HaplotreeController(tableController, logClass);
        treeController.configureSearch(new Pane());
        treeController.setAnimation();

        haploStatistics = new HaploStatistics(tableController, treeController, logClass, groupController);

        testFiles = new GUITestFiles();
        setupApplication(MitoBenchWindow.class);
    }

    @Test
    public void testWalkThrough() throws Exception {
        GUITestSteps steps = new GUITestSteps(this);

        steps.part0SetLogDir();
        steps.part1TreeViewTests();
        steps.part2FillTable(getResource(testFiles.getProject_file()).toString());
        steps.part3AboutDialogueTests();
        steps.part4BasicStuff();
        steps.part5DBTest();
        //steps.partTestGrouping();
        steps.part6Statistics();
        steps.part7CreatePlots();
        steps.part8MenuInteraction();

    }

    @Test
    public void chartController(){

        // test method "roundValue()"
        double val1 = 0.01234;
        double val2 = 3.5653;
        assertEquals(0.01, chartController.roundValue(val1));
        assertEquals(3.57, chartController.roundValue(val2));

    }


    @Test
    public void methodsTest(){

        haploStatistics.setNumber_of_groups(4);
        assertEquals(4, haploStatistics.getNumber_of_groups());
        assertEquals(tableController, haploStatistics.getTableController());

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




    /**
     * step("Export the selection", () -> {
     * robot.clickOn("#buttonExport");
     * validator.validateExportFile();
     * });
     */

    @Override
    public void validateSavedSession(String name) {

    }

    @Override
    public void validateExportFile() {

    }

    private Path getResource(final String file) throws Exception {
        URL url = getClass().getResource("/" + file);

        if (url == null) {
            throw new FileNotFoundException(String.format("Unable to load %s", file));
        } else {
            return Paths.get(url.toURI());
        }
    }


}
