package view.dialogues.settings;

import Logging.LogClass;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class HGListDisalogue extends ATabpaneDialogue {


    private TextField textField_hgList;
    private Label label_info;
    private Button button_apply_list;

    public HGListDisalogue(String title, LogClass logClass) {
        super(title, logClass);

        LOG = this.logClass.getLogger(this.getClass());
        dialogGrid.setId("custom_hg_list_dialogue");
        addComponents();
    }

    private void addComponents() {

        label_info = new Label("You can define you own list of haplogroups here.\nExample: H,HV,R0\n\nThis list will " +
                " be set in all your analysis and visualization steps as default.");

        textField_hgList = new TextField();

        button_apply_list = new Button("Apply");


        int row_index = 0;
        dialogGrid.add(label_info, 0,row_index,1,1);
        dialogGrid.add(textField_hgList, 0,++row_index,1,1);
        dialogGrid.add(button_apply_list, 0,++row_index,1,1);

    }

    public TextField getTextField_hgList() {
        return textField_hgList;
    }

    public void setTextField_hgList(TextField textField_hgList) {
        this.textField_hgList = textField_hgList;
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