package view.table;

/**
 * Created by neukamm on 07.11.16.
 */


import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


public class ImportDialogue extends Application {

    private FileChooser fileChooser;
    private Stage primaryStage;
    private File inputFile;

    public static void main(String[] args) {
        Application.launch(args);
    }


    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            inputFile = file;
        }
    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Import your file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Multi-FastA Input (*.fa, *.fasta, *.fas)", "*.fasta", "*.fa", "*.fas"),
                new FileChooser.ExtensionFilter("Haplogrep 2 HSD Format (*.hsd)", "*.hsd"),
                new FileChooser.ExtensionFilter("ARP Arlequin Input Format (*.arp)", "*.arp"),
                new FileChooser.ExtensionFilter("Generic Input Format (*.tsv)", "*.tsv")
        );
    }

    public File getInputFile() {
        return inputFile;
    }
}
