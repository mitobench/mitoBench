package view.table;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

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
        configureFileChooser(fileChooser);

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                outFile = file.getAbsolutePath();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

    }



    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Save table content");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Microsoft Excel (*.xlsx)", "*.xlsx"),
                new FileChooser.ExtensionFilter("Comma Separated Values (*.csv)", "*.csv")
        );
    }

    public String getOutFile() {
        return outFile;
    }
}
