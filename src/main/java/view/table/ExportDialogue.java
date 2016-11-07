package view.table;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.logging.Logger;

/**
 * Created by neukamm on 07.11.16.
 */
public class ExportDialogue extends Application {

    private String outFile;

    public static void main(String[] args) {
        Application.launch(args);
    }


    @Override
    public void start(Stage stage) {


        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory =
                directoryChooser.showDialog(stage);

        if(selectedDirectory == null){
            outFile = "No Directory selected";
        }else{
            outFile = selectedDirectory.getAbsolutePath();
        }
//        FileChooser fileChooser = new FileChooser();
//
//        //Show save file dialog
//        File file = fileChooser.showSaveDialog(stage);
//
//        if(file != null){
//            outFile = file;
//        }
    }

    public String getOutFile() {
        return outFile;
    }
}
