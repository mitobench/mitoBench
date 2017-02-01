package view.menus;

import io.Exceptions.ARPException;
import io.Exceptions.FastAException;
import io.Exceptions.HSDException;
import io.Exceptions.ProjectException;
import io.datastructure.Entry;
import io.dialogues.Export.SaveAsDialogue;
import io.dialogues.Import.IImportDialogue;
import io.dialogues.Import.IImportDialogueFactory;
import io.dialogues.Import.ImportDialogueAlternative;
import io.dialogues.Import.ImportDialogueFactoryImpl;
import io.reader.*;
import io.writer.StatisticsWriter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.MitoBenchWindow;
import view.dialogues.error.ARPErrorDialogue;
import view.dialogues.error.FastAErrorDialogue;
import view.dialogues.error.HSDErrorDialogue;
import view.table.TableControllerDB;
import view.table.TableControllerUserBench;
import io.dialogues.Export.ExportDialogue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by neukamm on 16.11.16.
 */
public class FileMenu {

    private Menu menuFile;
    private TableControllerUserBench tableControllerUserBench;
    private TableControllerDB tableControllerDB;
    private MitoBenchWindow mitoBenchWindow;
    private String MITOBENCH_VERSION;
    private Stage stage;
    private StatisticsMenu toolsMenu;
    private IImportDialogueFactory importDialogueFactory;
    private FileMenu fm;

    public FileMenu(TableControllerUserBench tableController, String version, Stage stage, StatisticsMenu toolsMenu,
                    MitoBenchWindow mitoBenchWindow, TableControllerDB tableControllerDB ) throws IOException {

        MITOBENCH_VERSION = version;

        this.mitoBenchWindow = mitoBenchWindow;
        this.tableControllerDB = tableControllerDB;
        this.tableControllerUserBench = tableController;
        this.stage = stage;
        this.toolsMenu = toolsMenu;

        this.menuFile = new Menu("File");
        menuFile.setId("fileMenu");

        importDialogueFactory = new ImportDialogueFactoryImpl();
        fm = this;

        addSubMenus();

    }


    private void addSubMenus() throws IOException {



        /*
                        IMPORT DIALOGUE

         */

        MenuItem importFile = new MenuItem("Import Data");
        importFile.setId("importData");
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
                        IMPORT DIALOGUE

         */

        MenuItem importFromDB = new MenuItem("Import Data from DB");
        importFromDB.setId("importFromDB");
        importFromDB.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                // split table pane
                tableControllerDB = new TableControllerDB();
                mitoBenchWindow.splitTablePane();

                // todo: make db query

                // for testing: read test database
                tableControllerDB.init();
                tableControllerDB.setRowFactory();
                mitoBenchWindow.getTable_DB().getChildren().add(tableControllerDB.getTable());

                // create project reader to read tmp database
                ProjectReader reader = new ProjectReader();
                try {
                    reader.read(new File("test_files/project.mitoproj"));
                    reader.loadData(tableControllerDB);
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
                ExportDialogue exportDialogue = new ExportDialogue(tableControllerUserBench, MITOBENCH_VERSION);
                try {
                    exportDialogue.start(new Stage());
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
                StatisticsWriter statisticsWriter = new StatisticsWriter(toolsMenu.getHaploStatistics());
                try {
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
                System.exit(0);
            }
        });

        menuFile.getItems().addAll(importFile, importFromDB, exportFile, new SeparatorMenuItem(), exportCurrStats , new SeparatorMenuItem(), exit);
    }

    public void openProjectFile(File f){

        if (f != null) {
            String absolutePath = f.getAbsolutePath();


            //Input is FastA
            if (absolutePath.endsWith(".fasta") | absolutePath.endsWith("*.fas") | absolutePath.endsWith("*.fa")) {


                MultiFastAInput multiFastAInput = null;
                try {
                    try {
                        multiFastAInput = new MultiFastAInput(f.getPath());
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
                        hsdInputParser = new HSDInput(f.getPath());
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
                    GenericInputParser genericInputParser = new GenericInputParser(f.getPath());
                    HashMap<String, List<Entry>> data_map = genericInputParser.getCorrespondingData();
                    tableControllerUserBench.updateTable(data_map);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //Input is in ARP Format

            if(absolutePath.endsWith(".arp")){
                ARPReader arpreader = null;
                try {
                    arpreader = new ARPReader(f.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ARPException e) {
                    ARPErrorDialogue arpErrorDialogue = new ARPErrorDialogue(e);
                }
                HashMap<String, List<Entry>> data_map = arpreader.getCorrespondingData();
                tableControllerUserBench.updateTable(data_map);
                tableControllerUserBench.loadGroups();
            }

            if(absolutePath.endsWith(".mitoproj")){

                ProjectReader projectReader = new ProjectReader();
                try {
                    projectReader.read(f);
                    projectReader.loadData(tableControllerUserBench);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ProjectException e) {
                    e.printStackTrace();
                }


            }
        } else {
            try {
                //Didndonuffin
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


    private Path getResource(String file) throws Exception {
        URL url = getClass().getResource("/" + file);

        if (url == null) {
            throw new FileNotFoundException(String.format("Unable to load %s", file));
        } else {
            return Paths.get(url.toURI());
        }
    }


}
