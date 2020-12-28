package view.dialogues.error;

import Logging.LogClass;
import controller.TableControllerDB;
import io.datastructure.Entry;
import io.dialogues.Export.SaveAsDialogue;
import io.writer.GenericWriter;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.dialogues.settings.APopupDialogue;

import java.util.HashMap;
import java.util.List;

public class DuplicatesDialogue extends APopupDialogue {


    private Button save_dup;

    public DuplicatesDialogue(HashMap<String, List<Entry>> list_duplicates, LogClass LOG, TableControllerDB tableControllerDB) {
        super("Duplicates Warning", LOG);

        addComponents(list_duplicates, tableControllerDB);
        addListener(tableControllerDB);

        show();
    }

    private void addListener(TableControllerDB tableControllerDB) {
        save_dup.setOnAction(e -> {
            FileChooser.ExtensionFilter fex = new FileChooser.ExtensionFilter("Tab Separated Values (*.tsv)", "*.tsv");
            SaveAsDialogue saveAsDialogue = new SaveAsDialogue(fex);
            saveAsDialogue.start(new Stage());
            if (saveAsDialogue.getOutFile() != null) {
                String outFileDB = saveAsDialogue.getOutFile();
                try {
                    GenericWriter tsvWriter = new GenericWriter(tableControllerDB.getData(), "\t", false);
                    tsvWriter.writeData(outFileDB, tableControllerDB);
                    LOG.info("Export data into CSV format. File: " + outFileDB);
                } catch (Exception ex) {
                    System.err.println("Caught Exception: " + ex.getMessage());
                }
            }
        });
    }

    private void addComponents(HashMap<String, List<Entry>> list_duplicates, TableControllerDB tableControllerDB) {
        tableControllerDB.updateTable(list_duplicates);

        dialogGrid.setPadding(new Insets(10,10,10,10));
        ScrollPane scrollPane_duplicates = new ScrollPane();
        scrollPane_duplicates.setContent(tableControllerDB.getTable());
        save_dup = new Button("Save data");
        //scrollPane_duplicates.setContent(text);
        //this.dialogGrid.add(new Label(""), 0,0,1,1);
        //this.dialogGrid.add(new Label("WARNING"), 0,1,1,1);
        //this.dialogGrid.add(new Label(""), 0,2,1,1);
        this.dialogGrid.add(new Label("There are the duplicates found in the dataset and will be ignored.\nPlease revise the input file."),
                0,0,1,1);
        this.dialogGrid.add(scrollPane_duplicates, 0,1,1,1);
        this.dialogGrid.add(save_dup, 0,2,1,1);
    }
}
