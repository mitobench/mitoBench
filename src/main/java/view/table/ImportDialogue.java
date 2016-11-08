package view.table;/**
 * Created by neukamm on 07.11.16.
 */


import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


public class ImportDialogue extends Application {

    private FileChooser fileChooser;
    private Stage primaryStage;
    private File inputCSVFile;

    public static void main(String[] args) {
        Application.launch(args);
    }


    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            inputCSVFile = file;
        }



    }

    public File getInputCSVFile() {
        return inputCSVFile;
    }
}
