package view.dialogues.settings;

import Logging.LogClass;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import statistics.HaploStatistics;
import view.MitoBenchWindow;

import java.util.Arrays;

public class HGlistProfilePlot extends ATabpaneDialogue {

    private final MitoBenchWindow mito;
    private TextField textField_hglist;
    private Button okBtn;
    private CheckBox example_hg_list_checkbox;
    private HaploStatistics haploStatistics;
    private TabPane statsTabPane;

    public HGlistProfilePlot(String title, LogClass LOGClass, MitoBenchWindow mito){
        super(title, LOGClass);
        dialogGrid.setId("statistics_popup");
        this.mito = mito;
    }


    public void init(){
        addComponents();
        this.LOG = this.logClass.getLogger(this.getClass());
        addListener();
    }

    /**
     * This method adds all components to dialogue.
     */
    private void addComponents(){

        Label label = new Label("Please enter comma separated list of haplogroups " +
                "\naccording to which the haplogroups should be grouped:");
        Label example_hg_list = new Label("or use example list:");

        textField_hglist = new TextField();

        if(mito.getChartController().getCustomHGList()!=null) {
            if (mito.getChartController().getCustomHGList().length != 0) {
                String hgs = "";
                for(String s : mito.getChartController().getCustomHGList())
                    hgs += s + ",";
                textField_hglist.setText(hgs.substring(0, hgs.length()-1));
            }
        }

        okBtn = new Button("OK");
        okBtn.setId("button_ok_statistics");

        example_hg_list_checkbox = new CheckBox("Use example list");
        example_hg_list_checkbox.setId("checkbox_hg_example_selection");
        example_hg_list_checkbox.setSelected(false);

        dialogGrid.add(label, 0,0,3,1);
        dialogGrid.add(textField_hglist, 0,1,3,1);
        dialogGrid.add(example_hg_list,0,2,1,1);
        dialogGrid.add(example_hg_list_checkbox,1,2,1,1);
        dialogGrid.add(okBtn,2,3,1,1);
    }

    public void addListener(){

        example_hg_list_checkbox.selectedProperty().addListener((ov, old_val, new_val) -> {
            if(new_val){
                textField_hglist.setText(Arrays.toString(mito.getChartController().getCoreHGs()));
            }
        });


        Tooltip tp = new Tooltip("Example list : H,HV,I,J,K,L0,L1,L2,L3,L4,M1,N,N1a,N1b,R,R0,T,T1,T2,U,W,X");
        example_hg_list_checkbox.setOnMouseEntered(event -> {
            Point2D p = example_hg_list_checkbox.localToScreen(example_hg_list_checkbox.getLayoutBounds().getMaxX(), example_hg_list_checkbox.getLayoutBounds().getMaxY()); //I position the tooltip at bottom right of the node (see below for explanation)
            tp.show(example_hg_list_checkbox, p.getX(), p.getY());
        });
        example_hg_list_checkbox.setOnMouseExited(event -> tp.hide());
    }

    public Button getOkBtn() {
        return okBtn;
    }

    public String[] getHGsForProfilelotVis(){
        String[] hg_list;
        if(example_hg_list_checkbox.isSelected()){
            hg_list = mito.getChartController().getCoreHGs();
        } else {
            hg_list = textField_hglist.getText().split(",");
        }

        return hg_list;
    }
}
