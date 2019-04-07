package analysis;

import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.DirectoryChooser;
import view.MitoBenchWindow;
import view.dialogues.settings.FstSettingsDialogue;

import java.io.File;

public class
FstCalculationController {

    private final FstSettingsDialogue dialog;
    private final MitoBenchWindow mito;
    private FstCalculationRunner fstCalculationRunner;

    public FstCalculationController(FstSettingsDialogue fstSettingsDialogue, MitoBenchWindow mito){
        dialog = fstSettingsDialogue;
        this.mito = mito;
        addListener();
    }

    /**
     * Add listener to FST configuration dialogue
     */
    public void addListener(){

        dialog.getOkBtn().setOnAction(e -> {
            Task task = createTask();
            mito.getProgressBarhandler().activate(task.progressProperty());

            task.setOnSucceeded((EventHandler<Event>) event -> {
                fstCalculationRunner.writeResultToMitoBench();
                fstCalculationRunner.visualizeResult();

                if(dialog.getCheckbox_saveLogFileBtn().isSelected()){
                    fstCalculationRunner.writeResultToMitoBench();
                }

                dialog.getLOG().info("Fst calculations finished.");
                dialog.getMitobenchWindow().getTabpane_statistics().getTabs().remove(dialog.getTab());
                mito.getProgressBarhandler().stop();
            });
            new Thread(task).start();
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


    public Task createTask(){
        return new Task() {
            @Override
            protected Object call() throws Exception {
                fstCalculationRunner = new FstCalculationRunner(dialog.getMitobenchWindow(),
                        dialog.getComboBox_distance().getSelectionModel().getSelectedItem().toString(),
                        Double.parseDouble(dialog.getField_gamma_a().getText()),
                        dialog.getField_missing_data().getText().charAt(0),
                        Integer.parseInt(dialog.getField_numberOfPermutations().getText()),
                        Double.parseDouble(dialog.getField_significance().getText()));



                fstCalculationRunner.run(
                        dialog.getCheckbox_linearized_slatkin().isSelected(),
                        dialog.getCheckbox_linearized_slatkin().isSelected(),
                        dialog.getField_level_missing_data().getText());



                return true;
            }
        };

    }
}
