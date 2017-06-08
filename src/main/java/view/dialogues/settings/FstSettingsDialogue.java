package view.dialogues.settings;

import Logging.LogClass;
import analysis.FstCalculationRunner;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import view.MitoBenchWindow;

import java.io.IOException;

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
        Label label_general_settings = new Label("General settings:" +
                "\n\t- allowed level of missing data: 5%");
        Label label_lin = new Label("Optional:\n" +
                "This result is only printed in the result file.");
        Label label_distance_method = new Label("Distance method:");
        Label label_gamma_a = new Label("Gamma a value:");
        Label label_missing_data = new Label("Symbol for missing data:");

         /*
                    Checkbox
         */

        checkbox_linearized_slatkin = new CheckBox("Slatkin's linearized Fst's");
        checkbox_linearized_slatkin.setId("checkbox_slatkin");
        checkbox_linearized_slatkin.setSelected(false);
        checkbox_linearized_reynolds = new CheckBox("Reynolds's distance");
        checkbox_linearized_reynolds.setId("checkbox_reynolds");
        checkbox_linearized_reynolds.setSelected(false);


         /*
                   Combo box
         */

        comboBox_distance = new ComboBox();
        comboBox_distance.getItems().addAll(
                "Pairwise difference",
                "Percentage difference",
                "Jukes & Cantor",
                "Kimura 2-parameters",
                "Tamura",
                "Tajima and Nei",
                "Tamura and Nei"
        );

        comboBox_distance.getSelectionModel().selectFirst();



         /*
                   Textfield
         */

        field_gamma_a = new TextField("0.00");
        field_missing_data = new TextField("N");


         /*
                   Button
         */


        okBtn = new Button("OK");
        okBtn.setId("button_ok_statistics");


        dialogGrid.add(label_general_settings, 0,0,1,4);
        dialogGrid.add(label_distance_method, 0,5,1,1);
        dialogGrid.add(comboBox_distance, 1,6,1,1);
        dialogGrid.add(label_gamma_a, 0,7,1,1);
        dialogGrid.add(field_gamma_a, 1,7,1,1);
        dialogGrid.add(label_missing_data, 0,8,1,1);
        dialogGrid.add(field_missing_data, 1,8,1,1);

        dialogGrid.add(label_lin,0,9,1,2);
        dialogGrid.add(checkbox_linearized_slatkin, 0,10,1,1);
        dialogGrid.add(checkbox_linearized_reynolds,0,11,1,1);

        dialogGrid.add(okBtn,0,12,1,1);

    }

    private void addListener() {

        okBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                try {
                    FstCalculationRunner fstCalculationRunner = new FstCalculationRunner(mito,
                            comboBox_distance.getSelectionModel().getSelectedItem().toString(),
                            Double.parseDouble(field_gamma_a.getText()),
                            field_missing_data.getText().charAt(0));

                    fstCalculationRunner.run(checkbox_linearized_slatkin.isSelected(), checkbox_linearized_slatkin.isSelected());
                    fstCalculationRunner.writeToTable();

//                    fstCalculationRunner.writeToTable(fstCalculationRunner.getFsts(),
//                                                                fstCalculationRunner.getGroupnames(),
//                                                                "Fst values");

                    logClass.getLogger(this.getClass()).info("Calculate pairwise Fst " +
                            "values between following groups:\n" + fstCalculationRunner.getGroupnames());

                    dialog.close();

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void show(){
        Scene dialogScene = new Scene(dialogGrid, 500, 350);
        dialog.setScene(dialogScene);
        dialog.show();
    }

}
