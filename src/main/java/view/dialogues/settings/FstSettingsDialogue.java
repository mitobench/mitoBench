package view.dialogues.settings;

import Logging.LogClass;
import analysis.FstCalculationRunner;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import view.MitoBenchWindow;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by neukamm on 07.06.17.
 */
public class FstSettingsDialogue extends APopupDialogue{

    private Button okBtn;
    private CheckBox checkbox_linearized_slatkin;
    private CheckBox checkbox_linearized_reynolds;
    private MitoBenchWindow mito;
    private ComboBox comboBox_distance;
    private TextField field_gamma_a;
    private TextField field_missing_data;
    private TextField field_level_missing_data;
    private CheckBox checkbox_saveLogFileBtn;
    private TextField field_filePathResult;
    private Button chooseFileBtn;

    public FstSettingsDialogue(String title, LogClass logClass) {
        super(title, logClass);
        dialogGrid.setId("fst_popup");
        this.LOG = this.logClass.getLogger(this.getClass());
        show();
    }

    public void init(MitoBenchWindow mito){
        this.mito = mito;
        addComponents();
        addListener();
    }



    private void addComponents() {

        /*
                    Label
         */
//        Label label_general_settings = new Label("General settings:" +
//                "\n\t- allowed level of missing data: 5%");
        Label label_general_settings = new Label("");
        Label label_lin = new Label("Optional:\n");//+"This result is only printed in the result file.");
        Label label_distance_method = new Label("Distance method:");
        Label label_gamma_a = new Label("Gamma a value:");
        Label label_missing_data = new Label("Symbol for missing data:");
        Label label_allowed_level_of_missing_data = new Label("allowed level of missing data:");
        Label infoLogFile = new Label("Do you want to save the result?");
        Label setFilePathLabel = new Label("File location");

         /*
                    Checkbox
         */

        checkbox_linearized_slatkin = new CheckBox("Slatkin's linearized Fst's");
        checkbox_linearized_slatkin.setId("checkbox_slatkin");
        checkbox_linearized_slatkin.setSelected(false);
        checkbox_linearized_reynolds = new CheckBox("Reynolds' distance");
        checkbox_linearized_reynolds.setId("checkbox_reynolds");
        checkbox_linearized_reynolds.setSelected(false);
        checkbox_saveLogFileBtn = new CheckBox("Save");


         /*
                   Combo box
         */

        comboBox_distance = new ComboBox();
        comboBox_distance.getItems().addAll(
                "Pairwise Difference",
                //"Percentage difference",
                "Jukes and Cantor",
                "Kimura 2-parameters"
//                ,
//                "Tamura",
//                "Tajima and Nei",
//                "Tamura and Nei"
        );

        comboBox_distance.getSelectionModel().selectFirst();



         /*
                   Textfield
         */

        field_gamma_a = new TextField("0.00");
        field_missing_data = new TextField("N");
        field_level_missing_data = new TextField("0.05");
        field_filePathResult = new TextField(System.getProperty("user.dir"));
        field_filePathResult.setDisable(true);


         /*
                   Button
         */


        okBtn = new Button("OK");
        okBtn.setId("button_ok_statistics");
        chooseFileBtn = new Button("Change location");
        chooseFileBtn.setId("btn_chooseLogDir");
        chooseFileBtn.setDisable(true);


        // general settings

        dialogGrid.add(label_general_settings, 0,0,1,4);
        dialogGrid.add(label_distance_method, 0,5,1,1);
        dialogGrid.add(comboBox_distance, 1,5,1,1);
        dialogGrid.add(label_gamma_a, 0,6,1,1);
        dialogGrid.add(field_gamma_a, 1,6,1,1);
        dialogGrid.add(label_missing_data, 0,7,1,1);
        dialogGrid.add(field_missing_data, 1,7,1,1);
        dialogGrid.add(label_allowed_level_of_missing_data, 0,8,1,1);
        dialogGrid.add(field_level_missing_data, 1,8,1,1);

        dialogGrid.add(new Separator(), 0, 9,2,1);


        // linearization

        dialogGrid.add(label_lin,0,10,1,2);
        dialogGrid.add(checkbox_linearized_slatkin, 1,10,1,1);
        dialogGrid.add(checkbox_linearized_reynolds,1,11,1,1);

        dialogGrid.add(new Separator(), 0, 12,2,1);

        // save file
        //addButtonListener(applyBtn, chooseFileBtn);
        dialogGrid.add(infoLogFile, 0,13,1,1);
        dialogGrid.add(checkbox_saveLogFileBtn, 1,13,1,1);
        //dialogGrid.add(discardLogFile, 2,13,1,1);

        dialogGrid.add(setFilePathLabel, 0,14,1,1);
        dialogGrid.add(field_filePathResult, 0,15,1,1);
        dialogGrid.add(chooseFileBtn, 1,15,1,1);

        dialogGrid.add(new Separator(), 0, 16,2,1);

        dialogGrid.add(okBtn,1,17,1,1);

    }

    private void addListener() {

        okBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                try {



                    FstCalculationRunner fstCalculationRunner = new FstCalculationRunner(mito,
                            comboBox_distance.getSelectionModel().getSelectedItem().toString(),
                            Double.parseDouble(field_gamma_a.getText()),
                            field_missing_data.getText().charAt(0));

                    logClass.getLogger(this.getClass()).info("Calculate pairwise Fst " +
                            "values between following groups:\n" + Arrays.toString(fstCalculationRunner.getGroupnames()));

                    fstCalculationRunner.run(
                            checkbox_linearized_slatkin.isSelected(),
                            checkbox_linearized_slatkin.isSelected(),
                            field_level_missing_data.getText());
                    fstCalculationRunner.writeToTable();

                    fstCalculationRunner.visualizeResult();


                    if(checkbox_saveLogFileBtn.isSelected()){
                        fstCalculationRunner.writeToFile(field_filePathResult.getText());
                    }


                    LOG.info("Fst calculations finished.");

                    dialog.close();

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        // add checkbox listener
        checkbox_saveLogFileBtn.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                field_filePathResult.setEditable(new_val);
                field_filePathResult.setDisable(old_val);
                chooseFileBtn.setDisable(old_val);

            }

        });


        chooseFileBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("JavaFX Projects");
                File defaultDirectory = new File(System.getProperty("user.dir"));
                chooser.setInitialDirectory(defaultDirectory);
                File selectedDirectory = chooser.showDialog(mito.getPrimaryStage());
                field_filePathResult.setText(selectedDirectory.getAbsolutePath());

            }
        });
    }

    @Override
    protected void show(){
        Scene dialogScene = new Scene(dialogGrid, 500, 440);
        dialog.setScene(dialogScene);
        dialog.show();
    }

}
