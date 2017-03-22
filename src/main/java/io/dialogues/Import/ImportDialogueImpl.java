package io.dialogues.Import;

/**
 * Created by neukamm on 07.11.16.
 */


import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.util.function.BiFunction;


public class ImportDialogueImpl implements IImportDialogue {

    private FileChooser fileChooser;
    private File selectedFile;
    private Stage stage;

    public ImportDialogueImpl(Stage stage){
        this.stage = stage;
    }

    @Override
    public void start() {
        show();
    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Import your file");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Multi-FastA Input (*.fa, *.fasta, *.fas)", "*.fasta", "*.fa", "*.fas"),
                new FileChooser.ExtensionFilter("Haplogrep 2 HSD Format (*.hsd)", "*.hsd"),
                new FileChooser.ExtensionFilter("ARP Arlequin Input Format (*.arp)", "*.arp"),
                new FileChooser.ExtensionFilter("Excel Input Format (*.xlsx, *.xls)", "*.xlsx","*.xls"),
                new FileChooser.ExtensionFilter("Generic Input Format (*.tsv)", "*.tsv"),
                new FileChooser.ExtensionFilter("MitoProject Input (*.mitoproj)", "*.mitoproj"));
    }

    @Override
    public boolean isFileSelected() {
        return selectedFile != null;
    }

    @Override
    public File getSelectedFile() {
        return selectedFile;
    }

    private void show(){
        fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        BiFunction<FileChooser, Window, File> openFunction = FileChooser::showOpenDialog;
        selectedFile = openFunction.apply(fileChooser, stage);
    }



}
