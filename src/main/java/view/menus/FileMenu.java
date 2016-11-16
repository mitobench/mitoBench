package view.menus;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.Stage;
import view.table.*;

/**
 * Created by neukamm on 16.11.16.
 */
public class FileMenu {

    private Menu menuFile;
    private TableManager tableManager;

    public FileMenu(TableManager tableManager){
        this.menuFile = new Menu("File");
        this.tableManager = tableManager;
        addSubMenus();

    }


    private void addSubMenus(){



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

        menuFile.getItems().addAll(importFile, exportFile, new SeparatorMenuItem(), exit);

    }


    public Menu getMenuFile() {
        return menuFile;
    }
}
