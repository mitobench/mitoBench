package view.dialogues.settings;

import Logging.LogClass;
import javafx.scene.control.*;

public class PieChartSettingsDialogue  extends ATabpaneDialogue {

    private Button applyBtn;

    private TextField textField;
    private CheckBox default_list_checkbox;


    public PieChartSettingsDialogue(String title, LogClass logClass) {
        super(title, logClass);
        dialogGrid.setId("piechart_popup");

        addComponents();
    }


    /**
     * This method adds all components to dialogue.
     *
     */
    private void addComponents(){
        applyBtn = new Button("Apply");
        applyBtn.setId("stackedBarApplyBtn");

        Label label = new Label("Please enter comma separated list of haplogroups " +
                "\naccording to which the haplogroups should be grouped:");
        Label default_list = new Label("or use the default list:");

        textField = new TextField();


        default_list_checkbox = new CheckBox("Use default list");
        default_list_checkbox.setId("checkbox_hg_default_selection");
        default_list_checkbox.setSelected(false);

        dialogGrid.add(label, 0,0,3,1);
        dialogGrid.add(textField, 0,1,3,1);
        dialogGrid.add(default_list,0,2,1,1);
        dialogGrid.add(default_list_checkbox,1,2,1,1);
        dialogGrid.add(applyBtn,2,3,1,1);
    }


    public TextField getTextField_hgList() {
        return textField;
    }
    public Button getApplyBtn() {
        return applyBtn;
    }

    public CheckBox getDefault_list_checkbox() {
        return default_list_checkbox;
    }
}
