
package view.menus;

import Logging.LogClass;
import Logging.LoggerSettingsDialogue;
import controller.DatabaseConnectionController;
import io.Exceptions.*;
import io.datastructure.Entry;
import io.dialogues.Export.SaveAsDialogue;
import io.dialogues.Import.IImportDialogue;
import io.dialogues.Import.IImportDialogueFactory;
import io.dialogues.Import.ImportDialogueAlternative;
import io.dialogues.Import.ImportDialogueFactoryImpl;
import io.fileConversionPGDSpider.SpiderCoversion;
import io.reader.*;
import io.writer.ImageWriter;
import io.writer.StatisticsWriter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import view.MitoBenchWindow;
import view.dialogues.error.ARPErrorDialogue;
import view.dialogues.error.FastAErrorDialogue;
import view.dialogues.error.HSDErrorDialogue;
import view.dialogues.information.InformationDialogue;
import view.dialogues.settings.DBSearchDialogue;
import view.dialogues.settings.DatabaseConnectionDialogue;
import view.table.DrapAndDropEventMaganer;
import view.table.controller.TableControllerDB;
import view.table.controller.TableControllerUserBench;
import io.dialogues.Export.ExportDialogue;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by neukamm on 16.11.16.
 */
public class FileMenu {

    private TabPane viz_pane;
    private Menu menuFile;
    private TableControllerUserBench tableControllerUserBench;
    private TableControllerDB tableControllerDB;
    private MitoBenchWindow mitoBenchWindow;
    private String MITOBENCH_VERSION;
    private Stage stage;
    private StatisticsMenu toolsMenu;
    private IImportDialogueFactory importDialogueFactory;
    private FileMenu fm;
    private DrapAndDropEventMaganer drapAndDropEventMaganer;
    private Logger LOG;
    private LogClass logClass;
    private DatabaseConnectionController databaseConnectionController;


    public FileMenu( StatisticsMenu toolsMenu,
                     MitoBenchWindow mitoBenchWindow)
            throws IOException {

        MITOBENCH_VERSION = mitoBenchWindow.getMITOBENCH_VERSION();

        this.mitoBenchWindow = mitoBenchWindow;
        this.tableControllerDB = mitoBenchWindow.getTableControllerDB();
        this.tableControllerUserBench = mitoBenchWindow.getTableControllerUserBench();
        this.stage = mitoBenchWindow.getPrimaryStage();
        this.toolsMenu = toolsMenu;

        this.menuFile = new Menu("File");
        menuFile.setId("fileMenu");

        importDialogueFactory = new ImportDialogueFactoryImpl();
        fm = this;
        viz_pane = mitoBenchWindow.getTabpane_visualization();

        LOG = mitoBenchWindow.getLogClass().getLogger(this.getClass());
        this.logClass = mitoBenchWindow.getLogClass();
        addSubMenus();

    }


    private void addSubMenus() throws IOException {

        // new project

        MenuItem newProject = new MenuItem("New Project");
        newProject.setId("menu_item_new_project");
        newProject.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                // clear all
                tableControllerDB.cleartable();
                tableControllerUserBench.cleartable();
                viz_pane.getTabs().removeAll(viz_pane.getTabs());
                mitoBenchWindow.getTabpane_statistics().getTabs().removeAll(mitoBenchWindow.getTabpane_statistics().getTabs());
                mitoBenchWindow.setProjectLoaded(false);
            }
        });



        /*
                        IMPORT DIALOGUE
         */

        MenuItem importFile = new MenuItem("Import");
        importFile.setId("importFile");
        importFile.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                IImportDialogue importDialogue;

                if(isJUnitTest()){

                    ImportDialogueAlternative importDialogueAlternative = new ImportDialogueAlternative(fm);
                    importDialogueAlternative.getFile();

                } else {
                    importDialogue = importDialogueFactory.create(stage);
                    importDialogue.start();
                    openProjectFile(importDialogue.getSelectedFile());
                }

            }
        });



        /*
                        IMPORT from database
         */

        MenuItem importFromDB = new MenuItem("Import Data from DB");
        importFromDB.setId("importFromDB");
        importFromDB.setOnAction(new EventHandler<ActionEvent>() {
            // todo: make db query
            public void handle(ActionEvent t) {
                if(databaseConnectionController==null){
                    databaseConnectionController = new DatabaseConnectionController();
                    Tab tab = new Tab("Database Login");

                    DatabaseConnectionDialogue databaseConnectionDialogue = new DatabaseConnectionDialogue(
                            tableControllerDB,
                            logClass,
                            mitoBenchWindow,
                            tab,
                            databaseConnectionController
                    );
                    GridPane dialogue = databaseConnectionDialogue.getDialogGrid();
                    tab.setContent(dialogue);
                    mitoBenchWindow.getTabpane_statistics().getTabs().add(tab);
                } else {
                    // open search mask to specify which data should be loaded
                    DBSearchDialogue dbSearchDialogue = new DBSearchDialogue("SQL statement configurator", mitoBenchWindow);
                    dbSearchDialogue.fillDialogue();
                    dbSearchDialogue.addFunctionality(
                            databaseConnectionController.getUserName(),
                            databaseConnectionController.getPassword(),
                            databaseConnectionController.getTable()
                    );
                }

//                if(drapAndDropEventMaganer==null){
//                    drapAndDropEventMaganer = new DrapAndDropEventMaganer(tableControllerDB, tableControllerUserBench);
//                    drapAndDropEventMaganer.createEvent();
//                }


            }
        });



        /*
                        Export Image
         */


        MenuItem exportImage = new MenuItem("Export Chart");
        exportImage.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    FileChooser.ExtensionFilter fex = new FileChooser.ExtensionFilter("Image format (*.png)", "*.txt");
                    //SaveAsDialogue sad = new SaveAsDialogue(fex);
                    //sad.start(new Stage());

                    int scale = 6; //6x resolution should be enough, users should downscale if required
                    final SnapshotParameters spa = new SnapshotParameters();
                    spa.setTransform(javafx.scene.transform.Transform.scale(scale, scale));
                    ImageWriter imageWriter = new ImageWriter(logClass);
                    try {
                        imageWriter.saveImage(viz_pane.getSelectionModel().getSelectedItem().getContent());
                    } catch (ImageException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        /*
                        EXPORT DIALOGUE
         */

        MenuItem exportFile = new MenuItem("Export Data");
        exportFile.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                ExportDialogue exportDialogue = new ExportDialogue(tableControllerUserBench, MITOBENCH_VERSION, logClass);
                try {
                    exportDialogue.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });




        /*
                        EXPORT WITH PGDSPIDER
         */

        MenuItem exportFileSpider = new MenuItem("Convert files with PGDSpider");
        exportFileSpider.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    SpiderCoversion spiderCoversion = new SpiderCoversion();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        MenuItem exportCurrStats = new MenuItem("Export statistics");
        exportCurrStats.setId("#exportCurrentStats");
        exportCurrStats.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {

                FileChooser.ExtensionFilter fex = new FileChooser.ExtensionFilter("Text format (*.txt)", "*.txt");
                SaveAsDialogue sad = new SaveAsDialogue(fex);
                sad.start(new Stage());
                //StatisticsWriter statisticsWriter = new StatisticsWriter(toolsMenu.getHaploStatistics());
                StatisticsWriter statisticsWriter = new StatisticsWriter(
                        mitoBenchWindow.getTabpane_statistics().getSelectionModel().getSelectedItem());
                try {
                    //statisticsWriter.writeData(sad.getOutFile());
                    statisticsWriter.writeData(sad.getOutFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });


        /*
                EXIT OPTION
         */

        MenuItem exit = new MenuItem("Exit");
        exit.setId("fileMenu-exitItem");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                t.consume();
                LoggerSettingsDialogue loggerSettingsDialogue =
                        new LoggerSettingsDialogue("Log file configuration", logClass, stage);
            }
        });

        menuFile.getItems().addAll(newProject, new SeparatorMenuItem(), importFile, importFromDB, exportFile, exportFileSpider, new SeparatorMenuItem(), exportImage, exportCurrStats , new SeparatorMenuItem(), exit);
    }

    /**
     * Method to open files with specific parser.
     * @param f
     */
    public void openProjectFile(File f){

        if (f != null) {
            String absolutePath = f.getAbsolutePath();

            //Input is FastA
            if (absolutePath.endsWith(".fasta") || absolutePath.endsWith(".fas") || absolutePath.endsWith(".fa")) {

                MultiFastAInput multiFastAInput = null;
                try {
                    try {
                        multiFastAInput = new MultiFastAInput(f.getPath(), LOG);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (FastAException e) {
                    FastAErrorDialogue fastAErrorDialogue = new FastAErrorDialogue(e);
                }
                HashMap<String, List<Entry>> input_multifasta = multiFastAInput.getCorrespondingData();
                tableControllerUserBench.updateTable(input_multifasta);
            }

            //Input is HSD Format
            if (absolutePath.endsWith(".hsd")) {
                try {
                    HSDInput hsdInputParser = null;
                    try {
                        hsdInputParser = new HSDInput(f.getPath(), LOG);
                    } catch (HSDException e) {
                        HSDErrorDialogue hsdErrorDialogue = new HSDErrorDialogue(e);
                    }
                    HashMap<String, List<Entry>> data_map = hsdInputParser.getCorrespondingData();
                    tableControllerUserBench.updateTable(data_map);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //Input is Generic Format

            if (absolutePath.endsWith(".tsv")) {
                try {
                    GenericInputParser genericInputParser = new GenericInputParser(f.getPath(), LOG);
                    HashMap<String, List<Entry>> data_map = genericInputParser.getCorrespondingData();
                    tableControllerUserBench.updateTable(data_map);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //Input is Excel Format

            if (absolutePath.endsWith(".xlsx") || absolutePath.endsWith(".xls")) {
                ExcelReader excelReader = null;
                try {
                    excelReader = new ExcelReader(f.getPath(), LOG);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                HashMap<String, List<Entry>> data_map = excelReader.getCorrespondingData();

                tableControllerUserBench.updateTable(data_map);
                //tableControllerUserBench.loadGroups();
            }

            //Input is in ARP Format

            if(absolutePath.endsWith(".arp")){
                ARPReader arpreader = null;
                try {
                    arpreader = new ARPReader(f.getPath(), LOG);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ARPException e) {
                    ARPErrorDialogue arpErrorDialogue = new ARPErrorDialogue(e);
                }
                HashMap<String, List<Entry>> data_map = arpreader.getCorrespondingData();

                tableControllerUserBench.updateTable(data_map);
                //tableControllerUserBench.loadGroups();
            }

            if(absolutePath.endsWith(".mitoproj")){

                if(mitoBenchWindow.isProjectLoaded()){
                    InformationDialogue informationDialogue = new InformationDialogue(
                            "Project already loaded",
                            "Please clean up your analysis before \na new project can be loaded.",
                            "Project already loaded",
                            "projectLoadedDialogue"
                    );
                } else {
                    ProjectReader projectReader = new ProjectReader();
                    try {

                        projectReader.read(f, LOG);
                        projectReader.loadData(tableControllerUserBench);
                        mitoBenchWindow.setProjectLoaded(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ProjectException e) {
                        e.printStackTrace();
                    }

                }
            }
        } else {
            try {
                // do nothing
            }catch (Exception e ) {
                System.out.println(e.getMessage());
            }
        }

    }


    public Menu getMenuFile() {
        return menuFile;
    }


    /**
     * This method tests based on a user defined System.property whether java is in
     * testing mode.
     *
     * @return
     */
    public static boolean isJUnitTest() {
        Properties props = System.getProperties();
        Enumeration e = props.propertyNames();

        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            if(key.startsWith("javafx.running")){
                if(props.getProperty("javafx.running").equals("true")){
                    return true;
                }

            }
        }
        return false;
    }


}

