package view.menus;

import io.datastructure.Entry;
import io.reader.GenericInputParser;
import io.reader.HSDInput;
import io.reader.MultiFastAInput;
import io.writer.CSVWriter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.Stage;
import view.table.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 16.11.16.
 */
public class FileMenu{

    private Menu menuFile;
    private TableController tableManager;

    public FileMenu(TableController tableManager)throws IOException{
        this.menuFile = new Menu("File");
        this.tableManager = tableManager;
        addSubMenus();

    }


    private void addSubMenus() throws IOException{



        /*
                        IMPORT DIALOGUE

         */

        MenuItem importFile = new MenuItem("Import file");
        importFile.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                ImportDialogue importDialogue = new ImportDialogue();
                importDialogue.start(new Stage());
                try{
                    // read file, parse to table
                    GenericInputParser genericInputParser = new GenericInputParser(importDialogue.getInputCSVFile().getPath());
                    HashMap<String, List<Entry>> data_map = genericInputParser.getCorrespondingData();
                    tableManager.updateTable(data_map);
                    //tableManager.populateTable();
                    // populateTable(tableManager, importDialogue.getInputCSVFile(), false);



                } catch (IOException e){
                    e.printStackTrace();
                }

            }
        });



        MenuItem importMultiFasta = new MenuItem("Import MultiFastA file");
        importMultiFasta.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {

                try {

                    ImportDialogue importDialogue = new ImportDialogue();
                    importDialogue.start(new Stage());
                    // read file, parse to table

                    if(importDialogue.getInputCSVFile().getPath() != null){
                        MultiFastAInput multiFastAInput = new MultiFastAInput(importDialogue.getInputCSVFile().getPath());
                        HashMap<String, List<Entry>> input_multifasta = multiFastAInput.getCorrespondingData();
                        tableManager.updateTable(input_multifasta);
                    }

                } catch (IOException e){
                    System.out.println("IOException " + e.getMessage());
                }
            }
        });



        MenuItem hsdImport = new MenuItem("Import HSD file");
        hsdImport.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                ImportDialogue importDialogue = new ImportDialogue();
                importDialogue.start(new Stage());
                try{
                    // read file, parse to table
                    HSDInput hsdInputParser = new HSDInput(importDialogue.getInputCSVFile().getPath());
                    HashMap<String, List<Entry>> data_map = hsdInputParser.getCorrespondingData();
                    tableManager.updateTable(data_map);

                } catch (IOException e){
                    e.printStackTrace();
                }

            }
        });


        /*
                        EXPORT DIALOGUE

         */

        MenuItem exportFile = new MenuItem("Export DB file");
        exportFile.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                ExportDialogue exportDialogue = new ExportDialogue();
                exportDialogue.start(new Stage());
                String outFileDB = exportDialogue.getOutFile();

                try{
                    CSVWriter csvWriter = new CSVWriter(tableManager);
                    csvWriter.writeCSV(outFileDB);
                } catch (Exception e) {
                    System.err.println("Caught Exception: " + e.getMessage());
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

        menuFile.getItems().addAll(importFile,importMultiFasta, hsdImport, exportFile, new SeparatorMenuItem(), exit);
    }


    public Menu getMenuFile() {
        return menuFile;
    }
}
