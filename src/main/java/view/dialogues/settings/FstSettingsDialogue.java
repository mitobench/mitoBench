package view.dialogues.settings;

import Logging.LogClass;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import view.MitoBenchWindow;

/**
 * Created by neukamm on 07.06.17.
 */
public class FstSettingsDialogue extends ATabpaneDialogue {

    private Button okBtn;
    private CheckBox checkbox_linearized_slatkin;
    private CheckBox checkbox_linearized_reynolds;
    private MitoBenchWindow mitobenchWindow;
    private ComboBox comboBox_distance;
    private TextField field_gamma_a;
    private TextField field_missing_data;
    private TextField field_level_missing_data;
    private CheckBox checkbox_saveLogFileBtn;
    private TextField field_filePathResult;
    private Button chooseFileBtn;
    private TextField field_numberOfPermutations;
    private TextField field_significance;

    public FstSettingsDialogue(String title, LogClass logClass, MitoBenchWindow mito) {
        super(title, logClass);

        LOG = this.logClass.getLogger(this.getClass());
        mitobenchWindow = mito;

        dialogGrid.setId("fst_popup");

        addComponents();
    }


    /**
     * This method creates and initializes all graphical components of the Fst runner dialog.
     */
    private void addComponents() {

        /*
                    Label
         */

        Label label_lin = new Label("Optional:\n");
        Label label_distance_method = new Label("Distance method:");
        Label label_gamma_a = new Label("Gamma a value:");
        Label label_missing_data = new Label("Symbol for missing data:");
        Label label_allowed_level_of_missing_data = new Label("allowed level of missing data:");
        Label infoLogFile = new Label("Do you want to save the result?");
        Label setFilePathLabel = new Label("File location");
        Label label_numberOfPermutations = new Label("Number of permutations");
        Label label_significance = new Label("Significance p-value");

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
        field_numberOfPermutations = new TextField("0");
        field_significance = new TextField("0.05");

         /*
                   Button
         */


        okBtn = new Button("OK");
        okBtn.setId("button_ok_statistics");
        chooseFileBtn = new Button("Change location");
        chooseFileBtn.setId("btn_chooseLogDir");
        chooseFileBtn.setDisable(true);


        // place components with grid layout
        int row=0;
        dialogGrid.add(label_distance_method, 0,row,1,1);
        dialogGrid.add(comboBox_distance, 1,row,1,1);
        dialogGrid.add(label_gamma_a, 0,++row,1,1);
        dialogGrid.add(field_gamma_a, 1,row,1,1);
        dialogGrid.add(label_numberOfPermutations, 0,++row,1,1);
        dialogGrid.add(field_numberOfPermutations, 1,row,1,1);
        dialogGrid.add(label_missing_data, 0,++row,1,1);
        dialogGrid.add(field_missing_data, 1,row,1,1);
        dialogGrid.add(label_allowed_level_of_missing_data, 0,++row,1,1);
        dialogGrid.add(field_level_missing_data, 1,row,1,1);
        dialogGrid.add(label_significance, 0,++row,1,1);
        dialogGrid.add(field_significance, 1,row,1,1);

        dialogGrid.add(new Separator(), 0, ++row,2,1);


        // linearization

        dialogGrid.add(label_lin,0,++row,1,2);
        dialogGrid.add(checkbox_linearized_slatkin, 1,row,1,1);
        dialogGrid.add(checkbox_linearized_reynolds,1,++row,1,1);

        dialogGrid.add(new Separator(), 0, ++row,2,1);

        // save file
        dialogGrid.add(infoLogFile, 0,++row,1,1);
        dialogGrid.add(checkbox_saveLogFileBtn, 1,row,1,1);
        //dialogGrid.add(discardLogFile, 2,13,1,1);

        dialogGrid.add(setFilePathLabel, 0,++row,1,1);
        dialogGrid.add(field_filePathResult, 0,++row,1,1);
        dialogGrid.add(chooseFileBtn, 1,row,1,1);

        dialogGrid.add(new Separator(), 0, ++row,2,1);

        dialogGrid.add(okBtn,1,++row,1,1);

    }


    /**
     *      GETTER and SETTER
     */

    public Button getOkBtn() {
        return okBtn;
    }

    public void setOkBtn(Button okBtn) {
        this.okBtn = okBtn;
    }

    public CheckBox getCheckbox_linearized_slatkin() {
        return checkbox_linearized_slatkin;
    }

    public void setCheckbox_linearized_slatkin(CheckBox checkbox_linearized_slatkin) {
        this.checkbox_linearized_slatkin = checkbox_linearized_slatkin;
    }

    public CheckBox getCheckbox_linearized_reynolds() {
        return checkbox_linearized_reynolds;
    }

    public void setCheckbox_linearized_reynolds(CheckBox checkbox_linearized_reynolds) {
        this.checkbox_linearized_reynolds = checkbox_linearized_reynolds;
    }

    public MitoBenchWindow getMitobenchWindow() {
        return mitobenchWindow;
    }

    public void setMitobenchWindow(MitoBenchWindow mitobenchWindow) {
        this.mitobenchWindow = mitobenchWindow;
    }

    public ComboBox getComboBox_distance() {
        return comboBox_distance;
    }

    public void setComboBox_distance(ComboBox comboBox_distance) {
        this.comboBox_distance = comboBox_distance;
    }

    public TextField getField_gamma_a() {
        return field_gamma_a;
    }

    public void setField_gamma_a(TextField field_gamma_a) {
        this.field_gamma_a = field_gamma_a;
    }

    public TextField getField_missing_data() {
        return field_missing_data;
    }

    public void setField_missing_data(TextField field_missing_data) {
        this.field_missing_data = field_missing_data;
    }

    public TextField getField_level_missing_data() {
        return field_level_missing_data;
    }

    public void setField_level_missing_data(TextField field_level_missing_data) {
        this.field_level_missing_data = field_level_missing_data;
    }

    public CheckBox getCheckbox_saveLogFileBtn() {
        return checkbox_saveLogFileBtn;
    }

    public void setCheckbox_saveLogFileBtn(CheckBox checkbox_saveLogFileBtn) {
        this.checkbox_saveLogFileBtn = checkbox_saveLogFileBtn;
    }

    public TextField getField_filePathResult() {
        return field_filePathResult;
    }

    public void setField_filePathResult(TextField field_filePathResult) {
        this.field_filePathResult = field_filePathResult;
    }

    public Button getChooseFileBtn() {
        return chooseFileBtn;
    }

    public void setChooseFileBtn(Button chooseFileBtn) {
        this.chooseFileBtn = chooseFileBtn;
    }

    public TextField getField_numberOfPermutations() {
        return field_numberOfPermutations;
    }

    public void setField_numberOfPermutations(TextField field_numberOfPermutations) {
        this.field_numberOfPermutations = field_numberOfPermutations;
    }

    public TextField getField_significance() {
        return field_significance;
    }

    public void setField_significance(TextField field_significance) {
        this.field_significance = field_significance;
    }
}
