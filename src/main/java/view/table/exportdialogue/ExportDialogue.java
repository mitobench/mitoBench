package view.table.exportdialogue;

import io.writer.CSVWriter;
import io.writer.ExcelWriter;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.table.TableController;

import java.util.List;
import java.util.Optional;

/**
 * Created by peltzer on 30/11/2016.
 */
public class ExportDialogue extends Application {
    private List<String> columnsInTable = FXCollections.observableArrayList("option1", "option2", "option3");
    private TableController tableManager;
    //TODO this should come from the class instantiation...


    public static void main(String[] args) {
        Application.launch(args);
    }

    public ExportDialogue(TableController tableManager) {
        this.tableManager = tableManager;
        this.columnsInTable = tableManager.getCurrentColumnNames();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Select your desired export format");
        alert.setHeaderText("We need to know which kind of output you would like to save here and your desired grouping category.");
        alert.setContentText("Choose your desired export format.");

        ButtonType arp_button = new ButtonType("ARP");
        ButtonType beast_button = new ButtonType("BEAST");
        ButtonType csv_button = new ButtonType("CSV");
        ButtonType xlsx_button = new ButtonType("XLSX");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        //We disallow ID and mtSequence as entries
        removeUnwantedEntries();
        List<String> options = this.columnsInTable;


        alert.getButtonTypes().setAll(arp_button, beast_button, csv_button, xlsx_button, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();


        //ARP output
        if (result.get() == arp_button) {
            DataChoiceDialogue dataChoiceDialogue = new DataChoiceDialogue(options);
            String selection = dataChoiceDialogue.getSelected();
            FileChooser.ExtensionFilter fex = new FileChooser.ExtensionFilter("Arlequin Format (*.arp)", "*.arp");
            SaveAsDialogue saveAsDialogue = new SaveAsDialogue(fex);
            saveAsDialogue.start(new Stage());
            //TODO
            //Beast output
        } else if (result.get() == beast_button) {
            DataChoiceDialogue dataChoiceDialogue = new DataChoiceDialogue(options);
            String selection = dataChoiceDialogue.getSelected();
            FileChooser.ExtensionFilter fex = new FileChooser.ExtensionFilter("BEAST FastA format (*.fasta)", "*.fasta");
            SaveAsDialogue saveAsDialogue = new SaveAsDialogue(fex);
            saveAsDialogue.start(new Stage());
            //TODO
            //CSV Output
        } else if (result.get() == csv_button) {
            DataChoiceDialogue dataChoiceDialogue = new DataChoiceDialogue(options);
            String selection = dataChoiceDialogue.getSelected();
            FileChooser.ExtensionFilter fex = new FileChooser.ExtensionFilter("Comma Separated Values (*.csv)", "*.csv");
            SaveAsDialogue saveAsDialogue = new SaveAsDialogue(fex);
            saveAsDialogue.start(new Stage());
            if (saveAsDialogue.getOutFile() != null) {
                String outFileDB = saveAsDialogue.getOutFile();
                try {
                    CSVWriter csvWriter = new CSVWriter(tableManager);
                    csvWriter.setGroups(selection);
                    csvWriter.writeData(outFileDB);
                } catch (Exception e) {
                    System.err.println("Caught Exception: " + e.getMessage());
                }
            }
            //XLSX output
        } else if (result.get() == xlsx_button) {
            DataChoiceDialogue dataChoiceDialogue = new DataChoiceDialogue(options);
            String selection = dataChoiceDialogue.getSelected();
            FileChooser.ExtensionFilter fex = new FileChooser.ExtensionFilter("Microsoft Excel (*.xlsx)", "*.xlsx");
            SaveAsDialogue saveAsDialogue = new SaveAsDialogue(fex);
            saveAsDialogue.start(new Stage());
            if (saveAsDialogue.getOutFile() != null) {
                String outFileDB = saveAsDialogue.getOutFile();
                try {
                    ExcelWriter excelwriter = new ExcelWriter(tableManager);
                    excelwriter.writeData(outFileDB);
                } catch (Exception e) {
                    System.err.println("Caught Exception: " + e.getMessage());
                }
            }
        } else {
            //TODO what happens on cancel() exactly ?
        }
    }


    private void removeUnwantedEntries() {
        List<String> old_options = this.columnsInTable;
        if(old_options.size() > 2){
            old_options.remove("ID");
            old_options.remove("MTSequence");
        } else {
            //do nothing ;-)
        }

    }
}
