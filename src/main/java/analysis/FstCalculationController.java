package analysis;

import javafx.stage.DirectoryChooser;
import view.dialogues.settings.FstSettingsDialogue;

import java.io.File;
import java.io.IOException;

public class FstCalculationController {

    private final FstSettingsDialogue dialog;

    public FstCalculationController(FstSettingsDialogue fstSettingsDialogue){
        dialog = fstSettingsDialogue;
        addListener();
    }

    /**
     * Add listener to FST configuration dialogue
     */
    public void addListener(){


        dialog.getOkBtn().setOnAction(e -> {

            try {

                FstCalculationRunner fstCalculationRunner = new FstCalculationRunner(dialog.getMitobenchWindow(),
                        dialog.getComboBox_distance().getSelectionModel().getSelectedItem().toString(),
                        Double.parseDouble(dialog.getField_gamma_a().getText()),
                        dialog.getField_missing_data().getText().charAt(0));


                fstCalculationRunner.run(
                        dialog.getCheckbox_linearized_slatkin().isSelected(),
                        dialog.getCheckbox_linearized_slatkin().isSelected(),
                        dialog.getField_level_missing_data().getText());

                fstCalculationRunner.writeResultToMitoBench();
                fstCalculationRunner.visualizeResult();


                if(dialog.getCheckbox_saveLogFileBtn().isSelected()){
                    fstCalculationRunner.writeToFile(dialog.getField_filePathResult().getText());
                }


                dialog.getLOG().info("Fst calculations finished.");

                dialog.getMitobenchWindow().getTabpane_statistics().getTabs().remove(dialog.getTab());

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        // add checkbox listener
        dialog.getCheckbox_saveLogFileBtn().selectedProperty().addListener((ov, old_val, new_val) -> {
            dialog.getField_filePathResult().setEditable(new_val);
            dialog.getField_filePathResult().setDisable(old_val);
            dialog.getChooseFileBtn().setDisable(old_val);

        });


        dialog.getChooseFileBtn().setOnAction(e -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("JavaFX Projects");
            File defaultDirectory = new File(System.getProperty("user.dir"));
            chooser.setInitialDirectory(defaultDirectory);
            File selectedDirectory = chooser.showDialog(dialog.getMitobenchWindow().getPrimaryStage());
            dialog.getField_filePathResult().setText(selectedDirectory.getAbsolutePath());

        });

    }
}
