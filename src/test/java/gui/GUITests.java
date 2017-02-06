package gui;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.testfx.api.FxRobot;
import view.MitoBenchWindow;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.testfx.api.FxToolkit.registerPrimaryStage;
import static org.testfx.api.FxToolkit.setupApplication;


/**
 * Created by peltzer on 21/12/2016.
 * Basic GUI Testing is now implemented almost :-)
 */

@RunWith(MockitoJUnitRunner.Silent.class)
public class GUITests extends FxRobot implements GUITestValidator {

    private Path project;



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

        String project_file_string = "./project.mitoproj";
        project = getResource(project_file_string);
        setupApplication(MitoBenchWindow.class);

    }

    @Test
    public void testWalkThrough(){
        GUITestSteps steps = new GUITestSteps(this);

        steps.part3AboutDialogueTests();
        steps.part1BasicStuff();
        steps.part2MenuInteraction();
        steps.part4TreeViewTests();
        steps.part6FillTable(project.toString());
        steps.part5CreatePlots();
        steps.partStatistics();

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


//package gui;
//
//
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.testfx.api.FxRobot;
//import view.MitoBenchWindow;
//
//import java.io.*;
//import java.net.URL;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//import static org.testfx.api.FxToolkit.registerPrimaryStage;
//import static org.testfx.api.FxToolkit.setupApplication;
//
//
///**
// * Created by peltzer on 21/12/2016.
// * Basic GUI Testing is now implemented almost :-)
// */
//
//@RunWith(MockitoJUnitRunner.Silent.class)
//public class GUITests extends FxRobot implements GUITestValidator {
//
//    private GUITestFiles testFiles;
//
//
//
//    @BeforeClass
//    public static void setupSpec() throws Exception {
//        if (Boolean.getBoolean("headless")) {
//            System.setProperty("testfx.robot", "glass");
//            System.setProperty("testfx.headless", "true");
//            System.setProperty("prism.order", "sw");
//            System.setProperty("prism.text", "t2k");
//            System.setProperty("java.awt.headless", "true");
//        }
//        System.setProperty("javafx.running", "true");
//        registerPrimaryStage();
//    }
//
//    @Before
//    public void setUp() throws Exception {
//
//        testFiles = new GUITestFiles();
//        setupApplication(MitoBenchWindow.class);
//
//    }
//
//    @Test
//    public void testWalkThrough() throws Exception {
//        GUITestSteps steps = new GUITestSteps(this);
//
//        steps.part3AboutDialogueTests();
//        steps.part1BasicStuff();
//        steps.part2MenuInteraction();
//        steps.part4TreeViewTests();
//        steps.part6FillTable(getResource(testFiles.getProject_file()).toString());
//        steps.part5CreatePlots();
//        steps.partStatistics();
//
//    }
////
////    @Test
////    public void treeTests() throws IOException, ParserConfigurationException, SAXException {
////
////        TreeTest treeTest = new TreeTest();
////        treeTest.setUp();
////        treeTest.treeTest();
////
////    }
////
////    @Test
////    public void methodTests() throws IOException, SAXException, ParserConfigurationException {
////        MethodTests methodTests = new MethodTests();
////        methodTests.setUp();
////        methodTests.chartControllerTests();
////    }
//
//
//
//    /**
//     * step("Export the selection", () -> {
//     * robot.clickOn("#buttonExport");
//     * validator.validateExportFile();
//     * });
//     */
//
//    @Override
//    public void validateSavedSession(String name) {
//
//    }
//
//    @Override
//    public void validateExportFile() {
//
//    }
//
//    private Path getResource(String file) throws Exception {
//        URL url = getClass().getResource("/" + file);
//
//        if (url == null) {
//            throw new FileNotFoundException(String.format("Unable to load %s", file));
//        } else {
//            return Paths.get(url.toURI());
//        }
//    }
//
//
//}
