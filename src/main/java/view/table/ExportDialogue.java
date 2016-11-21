package view.table;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
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


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save table content");
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                outFile = file.getAbsolutePath();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

    public String getOutFile() {
        return outFile;
    }
}
