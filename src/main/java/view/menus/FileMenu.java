package view.menus;

import io.Exceptions.ARPException;
import io.Exceptions.FastAException;
import io.Exceptions.HSDException;
import io.Exceptions.ProjectException;
import io.datastructure.Entry;
import io.dialogues.Export.SaveAsDialogue;
import io.dialogues.Import.IImportDialogue;
import io.dialogues.Import.IImportDialogueFactory;
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
import view.dialogues.error.ARPErrorDialogue;
import view.dialogues.error.FastAErrorDialogue;
import view.dialogues.error.HSDErrorDialogue;
import view.table.TableController;
import io.dialogues.Export.ExportDialogue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 16.11.16.
 */
public class FileMenu {

    private Menu menuFile;
    private TableController tableController;
    private String MITOBENCH_VERSION;
    private Stage stage;
    private ToolsMenu toolsMenu;
    private boolean isTestMode;
    private IImportDialogueFactory importDialogueFactory;

    public FileMenu(TableController tableController, String version, Stage stage, ToolsMenu toolsMenu) throws IOException {
        this.menuFile = new Menu("File");
        menuFile.setId("fileMenu");
        this.tableController = tableController;
        MITOBENCH_VERSION = version;
        this.stage = stage;
        this.toolsMenu = toolsMenu;
        isTestMode = false;
        importDialogueFactory = new ImportDialogueFactoryImpl();
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
//                FileDialogueFactoryImpl fileDialogueFactory = new FileDialogueFactoryImpl();
//                FileDialogue fileDialogue = fileDialogueFactory.create(FileDialogueType.NEW_SESSION, stage);
//                fileDialogue.showChooser();
//                File f = fileDialogue.getSelectedFile().toFile();

                File f;

                // = new File("/home/neukamm/GitWorkspace/MitoBench/test_files/project.mitoproj");
                IImportDialogue importDialogue = importDialogueFactory.create(stage, false);
                importDialogue.start();
                f = importDialogue.getSelectedFile();


                   // f = new File("/home/neukamm/GitWorkspace/MitoBench/test_files/project.mitoproj");



                openProjectFile(f);

            }
        });



        /*
                        EXPORT DIALOGUE

         */

        MenuItem exportFile = new MenuItem("Export Data");
        exportFile.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                ExportDialogue exportDialogue = new ExportDialogue(tableController, MITOBENCH_VERSION);
                try {
                    exportDialogue.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        MenuItem exportCurrStats = new MenuItem("Export statistics (current view)");
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

        menuFile.getItems().addAll(importFile, exportFile, new SeparatorMenuItem(), exportCurrStats , new SeparatorMenuItem(), exit);
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
                tableController.updateTable(input_multifasta);


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
                    tableController.updateTable(data_map);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //Input is Generic Format

            if (absolutePath.endsWith(".tsv")) {
                try {
                    GenericInputParser genericInputParser = new GenericInputParser(f.getPath());
                    HashMap<String, List<Entry>> data_map = genericInputParser.getCorrespondingData();
                    tableController.updateTable(data_map);
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
                tableController.updateTable(data_map);
                tableController.loadGroups();
            }

            if(absolutePath.endsWith(".mitoproj")){

                ProjectReader projectReader = new ProjectReader();
                try {
                    projectReader.read(f);
                    projectReader.loadData(tableController);
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


    public static boolean isJUnitTest() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        List<StackTraceElement> list = Arrays.asList(stackTrace);
        for (StackTraceElement element : list) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }
        return false;
    }

}
