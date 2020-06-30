
package view.menus;

import Logging.LogClass;
import Logging.LoggerSettingsDialogue;
import controller.*;
import io.dialogues.Export.SaveAsDialogue;
import io.dialogues.Import.IImportDialogue;
import io.dialogues.Import.IImportDialogueFactory;
import io.dialogues.Import.ImportDialogueAlternative;
import io.dialogues.Import.ImportDialogueFactoryImpl;
import io.fileConversionPGDSpider.SpiderCoversion;
import io.writer.ImageWriter;
import io.writer.StatisticsWriter;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import view.MitoBenchWindow;
import io.dialogues.Export.ExportDialogue;
import view.dialogues.settings.NewProjectWarning;
import view.dialogues.settings.SqlQueryBuilderWindow;

import java.io.IOException;
import java.util.*;

/**
 * Created by neukamm on 16.11.16.
 */
public class FileMenu {

    private final FileReaderController fileReaderController;
    private TabPane viz_pane;
    private Menu menuFile;
    private TableControllerUserBench tableControllerUserBench;
    private TableControllerDB tableControllerDB;
    private MitoBenchWindow mitoBenchWindow;
    private String MITOBENCH_VERSION;
    private Stage stage;
    private IImportDialogueFactory importDialogueFactory;
    private FileMenu fm;
    private Logger LOG;
    private LogClass logClass;


    public FileMenu( MitoBenchWindow mitoBenchWindow) {

        MITOBENCH_VERSION = mitoBenchWindow.getMITOBENCH_VERSION();

        this.mitoBenchWindow = mitoBenchWindow;
        this.tableControllerDB = mitoBenchWindow.getTableControllerDB();
        this.tableControllerUserBench = mitoBenchWindow.getTableControllerUserBench();
        this.stage = mitoBenchWindow.getPrimaryStage();

        this.menuFile = new Menu("File");
        menuFile.setId("fileMenu");

        importDialogueFactory = new ImportDialogueFactoryImpl();
        fm = this;
        viz_pane = mitoBenchWindow.getTabpane_visualization();

        LOG = mitoBenchWindow.getLogClass().getLogger(this.getClass());
        this.logClass = mitoBenchWindow.getLogClass();
        addSubMenus();

        fileReaderController = mitoBenchWindow.getFileReaderController();

    }


    private void addSubMenus() {

        // new project

        MenuItem newProject = new MenuItem("New Project");
        newProject.setId("menu_item_new_project");
        newProject.setOnAction(t -> {
            // ask if project should be saved
            NewProjectWarning newProjectWarning = new NewProjectWarning("Warning!", mitoBenchWindow.getLogClass(),
                    mitoBenchWindow);


        });



        /*
                        IMPORT DIALOGUE
         */

        MenuItem importFile = new MenuItem("Import");
        importFile.setId("importFile");
        importFile.setOnAction(t -> {
            IImportDialogue importDialogue;

            if(isJUnitTest()){

                ImportDialogueAlternative importDialogueAlternative = new ImportDialogueAlternative(fileReaderController);
                importDialogueAlternative.getFile();

            } else {
                importDialogue = importDialogueFactory.create(stage);
                importDialogue.start();
                fileReaderController.openFile(importDialogue.getSelectedFile());
            }

        });



        /*
                        IMPORT from database
         */

        MenuItem importFromDB = new MenuItem("Import Data from DB");
        importFromDB.setId("importFromDB");
        // todo: make db query
        importFromDB.setOnAction(t -> {
            SqlQueryBuilderWindow sqlQueryBuilderWindow = new SqlQueryBuilderWindow(mitoBenchWindow);
            Tab sqlConfigTab = new Tab("DB search config");
            sqlConfigTab.setContent(sqlQueryBuilderWindow.getPane());
            mitoBenchWindow.getTabpane_statistics().getTabs().add(sqlConfigTab);
            mitoBenchWindow.getTabpane_statistics().getSelectionModel().select(sqlConfigTab);
        });



        /*
                        Export Image
         */


        MenuItem exportImage = new MenuItem("Export Chart");
        exportImage.setOnAction(t -> {
            try {
                FileChooser.ExtensionFilter fex = new FileChooser.ExtensionFilter("Image format (*.png)", "*.txt");

                int scale = 6; //6x resolution should be enough, users should downscale if required
                final SnapshotParameters spa = new SnapshotParameters();
                spa.setTransform(javafx.scene.transform.Transform.scale(scale, scale));
                ImageWriter imageWriter = new ImageWriter(logClass);

                imageWriter.saveImage(viz_pane.getSelectionModel().getSelectedItem().getContent());


            } catch (Exception e) {
                e.printStackTrace();
            }
        });



        /*
                        EXPORT DIALOGUE
         */

        MenuItem exportFile = new MenuItem("Export all Data");
        exportFile.setOnAction(t -> {
            if(tableControllerUserBench.getTable().getItems().size() != 0){
                ExportDialogue exportDialogue = new ExportDialogue(mitoBenchWindow, true);
                try {
                    exportDialogue.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });


        MenuItem exportSelectedData = new MenuItem("Export selected Data");
        exportSelectedData.setOnAction(t -> {
            if(tableControllerUserBench.getTable().getItems().size() != 0){
                ExportDialogue exportDialogue = new ExportDialogue(mitoBenchWindow, false);
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
        exportFileSpider.setOnAction(t -> {
            try {
                SpiderCoversion spiderCoversion = new SpiderCoversion();
                LOG.info("Running PGDSpider.");
            } catch (Exception e) {
                e.printStackTrace();
            }

        });


        MenuItem exportCurrStats = new MenuItem("Export statistics");
        exportCurrStats.setId("#exportCurrentStats");
        exportCurrStats.setOnAction(t -> {

            FileChooser.ExtensionFilter fex = new FileChooser.ExtensionFilter("Text format (*.txt)", "*.txt");
            SaveAsDialogue sad = new SaveAsDialogue(fex);
            sad.start(new Stage());
            StatisticsWriter statisticsWriter = new StatisticsWriter();
            statisticsWriter.setTab( mitoBenchWindow.getTabpane_statistics().getSelectionModel().getSelectedItem());
            try {
                statisticsWriter.writeData(sad.getOutFile(), tableControllerUserBench);
            } catch (IOException e) {
                e.printStackTrace();
            }


        });


        /*
                EXIT OPTION
         */

        MenuItem exit = new MenuItem("Exit");
        exit.setId("fileMenu-exitItem");
        exit.setOnAction(t -> {
            t.consume();
            LoggerSettingsDialogue loggerSettingsDialogue =
                    new LoggerSettingsDialogue("Log file configuration", logClass, stage);
        });

        menuFile.getItems().addAll(newProject, new SeparatorMenuItem(), importFile, importFromDB, exportFile, exportSelectedData,
                exportFileSpider, new SeparatorMenuItem(), exportImage, exportCurrStats , new SeparatorMenuItem(), exit);
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

