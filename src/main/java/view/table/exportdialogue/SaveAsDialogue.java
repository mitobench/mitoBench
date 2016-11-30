package view.table.exportdialogue;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by neukamm on 07.11.16.
 */
public class SaveAsDialogue extends Application {

    private String outFile;
    private FileChooser fileChooser = new FileChooser();
    private FileChooser.ExtensionFilter extensionFilter;

    public static void main(String[] args) {
        Application.launch(args);
    }

    public SaveAsDialogue(FileChooser.ExtensionFilter fex){
        this.extensionFilter = fex;
        fileChooser.getExtensionFilters().add(this.extensionFilter);
    }


    @Override
    public void start(Stage stage) {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Save table content");

        File file = fileChooser.showSaveDialog(stage);
        if (file == null) {
            try {
                //Dont do anything here...
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        } else {
            try {
                outFile = file.getAbsolutePath();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

    /*+
    Returns the output file to write to!
     */

    public String getOutFile() {
        return outFile;
    }


    /**
     * This method sets the selected extension filter based on the users selection from a dialogue class.
     *
     * @param selectmeFirst
     */
    public void setPreselectedExport(FileChooser.ExtensionFilter selectmeFirst) {
        fileChooser.setSelectedExtensionFilter(selectmeFirst);
    }
}
