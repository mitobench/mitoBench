package view.menus;

import io.ErrorDialogues.ARPErrorDialogue;
import io.ErrorDialogues.FastAErrorDialogue;
import io.ErrorDialogues.HSDErrorDialogue;
import io.Exceptions.ARPException;
import io.Exceptions.FastAException;
import io.Exceptions.HSDException;
import io.datastructure.Entry;
import io.reader.ARPReader;
import io.reader.GenericInputParser;
import io.reader.HSDInput;
import io.reader.MultiFastAInput;
import io.writer.CSVWriter;
import io.writer.ExcelWriter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.Stage;
import view.table.ExportDialogue;
import view.table.ImportDialogue;
import view.table.TableController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 16.11.16.
 */
public class FileMenu {

    private Menu menuFile;
    private TableController tableManager;

    public FileMenu(TableController tableManager) throws IOException {
        this.menuFile = new Menu("File");
        this.tableManager = tableManager;
        addSubMenus();

    }


    private void addSubMenus() throws IOException {



        /*
                        IMPORT DIALOGUE

         */

        MenuItem importFile = new MenuItem("Import file");
        importFile.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                ImportDialogue importDialogue = new ImportDialogue();
                importDialogue.start(new Stage());


                if (importDialogue.getInputFile().getPath() != null) {
                    String absolutePath = importDialogue.getInputFile().getAbsolutePath();


                    //Input is FastA
                    if (absolutePath.endsWith(".fasta") | absolutePath.endsWith("*.fas") | absolutePath.endsWith("*.fa")) {


                        MultiFastAInput multiFastAInput = null;
                        try {
                            try {
                                multiFastAInput = new MultiFastAInput(importDialogue.getInputFile().getPath());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (FastAException e) {
                            FastAErrorDialogue fastAErrorDialogue = new FastAErrorDialogue(e);
                        }
                        HashMap<String, List<Entry>> input_multifasta = multiFastAInput.getCorrespondingData();
                        tableManager.updateTable(input_multifasta);


                    }

                    //Input is HSD Format
                    if (absolutePath.endsWith(".hsd")) {
                        try {
                            HSDInput hsdInputParser = null;
                            try {
                                hsdInputParser = new HSDInput(importDialogue.getInputFile().getPath());
                            } catch (HSDException e) {
                                HSDErrorDialogue hsdErrorDialogue = new HSDErrorDialogue(e);
                            }
                            HashMap<String, List<Entry>> data_map = hsdInputParser.getCorrespondingData();
                            tableManager.updateTable(data_map);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    //Input is Generic Format

                    if (absolutePath.endsWith(".tsv")) {
                        try {
                            GenericInputParser genericInputParser = new GenericInputParser(importDialogue.getInputFile().getPath());
                            HashMap<String, List<Entry>> data_map = genericInputParser.getCorrespondingData();
                            tableManager.updateTable(data_map);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    //Input is in ARP Format

                    if(absolutePath.endsWith(".arp")){
                        ARPReader arpreader = null;
                        try {
                            arpreader = new ARPReader(importDialogue.getInputFile().getPath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ARPException e) {
                            ARPErrorDialogue arpErrorDialogue = new ARPErrorDialogue(e);
                        }
                        HashMap<String, List<Entry>> data_map = arpreader.getCorrespondingData();
                        tableManager.updateTable(data_map);
                    }
                }
            }
        });



        /*
                        EXPORT DIALOGUE

         */

        MenuItem exportFile = new MenuItem("Export Table file");
        exportFile.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                ExportDialogue exportDialogue = new ExportDialogue();
                exportDialogue.start(new Stage());

                if (exportDialogue.getOutFile() != null) {
                    String outFileDB = exportDialogue.getOutFile();
                    if (outFileDB.endsWith(".csv")) {
                        try {
                            CSVWriter csvWriter = new CSVWriter(tableManager);
                            csvWriter.writeData(outFileDB);
                        } catch (Exception e) {
                            System.err.println("Caught Exception: " + e.getMessage());
                        }
                    } else if (outFileDB.endsWith(".xlsx")) {
                        try {
                            ExcelWriter excelwriter = new ExcelWriter(tableManager);
                            excelwriter.writeData(outFileDB);
                        } catch (Exception e) {
                            System.err.println("Caught Exception: " + e.getMessage());
                        }
                    }
                }
            }
        });

        /*
                EXIT OPTION
         */

        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.exit(0);
            }
        });

        menuFile.getItems().addAll(importFile, exportFile, new SeparatorMenuItem(), exit);
    }


    public Menu getMenuFile() {
        return menuFile;
    }


}
