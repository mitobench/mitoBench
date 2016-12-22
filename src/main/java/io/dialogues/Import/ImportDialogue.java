package io.dialogues.Import;

/**
 * Created by neukamm on 07.11.16.
 */


import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


public class ImportDialogue  {

    private FileChooser fileChooser;

    public File start() {
        fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        //File file = fileChooser.showOpenDialog(new Stage());
        File file = new File("test_files/project.mitoproj");
        if (file == null) {
            try {
                //Dont do anything here...
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        } else {
            return file;
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

}
