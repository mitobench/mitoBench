package view.dialogues.settings;

import Logging.LogClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;


public class HGListDialogue extends AHGDialogue {


    private ComboBox comboBox_hgList;
    private Label label_info;
    private Button button_apply_list;

    private ObservableList<String> options =
            FXCollections.observableArrayList(
                    "Sub-Saharan Africa (L0a,L0d,L0k,L1b,L1c,L2a,L2b,L2c,L3b,L3d,L3e,L3f,L4,L5)",
                    "Americas and the Caribbean (A2,B2,C1b,C1c,C1d,C4c,D1,D2a,D3,D4h3a,X2a,X2g)",
                    "South-eastern Asia (M*,M7,M8,M9,G,D,N*,R*,R9,B)",
                    "Europe (H)");

    public HGListDialogue(String title, LogClass logClass) {
        super(title, logClass);

        LOG = this.logClass.getLogger(this.getClass());
        dialogGrid.setId("custom_hg_list_dialogue");
        addComponents();
    }

    private void addComponents() {

        label_info = new Label("You can define your own list of haplogroups here.\nExample: H,HV,R0\n\nThis list will " +
                " be set in all your analysis and visualization steps as default.");


        comboBox_hgList = new ComboBox(options);
        comboBox_hgList.setEditable(true);

        button_apply_list = new Button("Apply");


        int row_index = 0;
        dialogGrid.add(label_info, 0,row_index,1,1);
        dialogGrid.add(comboBox_hgList, 0,++row_index,1,1);
        dialogGrid.add(button_apply_list, 0,++row_index,1,1);

    }

    public ComboBox getComboBox_hgList() {
        return comboBox_hgList;
    }

    public void setComboBox_hgList(ComboBox comboBox_hgList) {
        this.comboBox_hgList = comboBox_hgList;
    }

    public Label getLabel_info() {
        return label_info;
    }

    public void setLabel_info(Label label_info) {
        this.label_info = label_info;
    }

    public Button getButton_apply_list() {
        return button_apply_list;
    }

    public void setButton_apply_list(Button button_apply_list) {
        this.button_apply_list = button_apply_list;
    }
}