package view.dialogues.settings;

import Logging.LogClass;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import view.MitoBenchWindow;


public class HGListDialogue extends AHGDialogue {


    private ComboBox comboBox_hgList;
    private Label label_info;
    private Button button_apply_list;


    public HGListDialogue(String title, LogClass logClass, MitoBenchWindow mito) {
        super(title, logClass);

        LOG = this.logClass.getLogger(this.getClass());
        dialogGrid.setId("custom_hg_list_dialogue");
        this.mito = mito;
        String add_list = getMacrogroupsAsString(mito);
        if(!add_list.equals(""))
            options.add("Macrogroups (" + add_list + ")");
        addComponents();
    }



    /**
     * Add graphical components to main pane
     */
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

    public Button getButton_apply_list() {
        return button_apply_list;
    }

    public void setButton_apply_list(Button button_apply_list) {
        this.button_apply_list = button_apply_list;
    }
}