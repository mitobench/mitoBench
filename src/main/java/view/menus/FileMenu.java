package view.menus;

import io.datastructure.Entry;
import io.reader.MultiFastAInput;
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
                // read file, parse to table
                CSVReader csvReader = new CSVReader();
                csvReader.populateTable(tableManager, importDialogue.getInputCSVFile(), false);

            }
        });



        MenuItem importMultiFasta = new MenuItem("Import MultiFastA file");
        importMultiFasta.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {

                try {
                    ImportDialogue importDialogue = new ImportDialogue();
                    importDialogue.start(new Stage());
                    // read file, parse to table
                    MultiFastAInput multiFastAInput = new MultiFastAInput(importDialogue.getInputCSVFile().getPath());
                    HashMap<String, List<Entry>> input_multifasta = multiFastAInput.getCorrespondingData();
                    tableManager.updateTable(input_multifasta);
                } catch (IOException e){
                    System.out.println("IOException " + e.getMessage());
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
                    CSVWriter csvWriter = new CSVWriter(tableManager.getData());
                    csvWriter.writeExcel(outFileDB);
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

        menuFile.getItems().addAll(importFile,importMultiFasta, exportFile, new SeparatorMenuItem(), exit);

    }



    public Menu getMenuFile() {
        return menuFile;
    }
}
