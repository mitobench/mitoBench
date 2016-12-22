package io.dialogues.Import;

/**
 * Created by neukamm on 07.11.16.
 */


import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.util.function.BiFunction;


public class ImportDialogue  {

    private FileChooser fileChooser;
    private File selectedFile;
    private Stage stage;

    public File start(Stage stage) {
        this.stage = stage;
        show();
        //selectedFile = fileChooser.showOpenDialog(new Stage());
        //File file = new File("test_files/project.mitoproj");
        if (selectedFile == null) {
            try {
                //Dont do anything here...
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        } else {
            return selectedFile;
        }

        return null;
    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Import your file");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Multi-FastA Input (*.fa, *.fasta, *.fas)", "*.fasta", "*.fa", "*.fas"),
                new FileChooser.ExtensionFilter("Haplogrep 2 HSD Format (*.hsd)", "*.hsd"),
                new FileChooser.ExtensionFilter("ARP Arlequin Input Format (*.arp)", "*.arp"),
                new FileChooser.ExtensionFilter("Generic Input Format (*.tsv)", "*.tsv"),
                new FileChooser.ExtensionFilter("MitoProject Input (*.mitoproj)", "*.mitoproj")
        );
    }

    public boolean isFileSelected() {
        return selectedFile != null;
    }

    public File getSelectedFile() {
        System.out.println("");
        return selectedFile;
    }

    public void show(){
        fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        BiFunction<FileChooser, Window, File> openFunction = FileChooser::showOpenDialog;
        selectedFile = openFunction.apply(fileChooser, stage);
        System.out.println("");
    }



}
