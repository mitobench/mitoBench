package view.dialogues.settings;

import Logging.LogClass;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import statistics.HaploStatistics;
import view.MitoBenchWindow;

import java.util.Arrays;

public abstract class AHGDialogue extends ATabpaneDialogue{


    protected TextField textField_hglist;
    protected Button okBtn;
    protected CheckBox default_list_checkbox;
    protected HaploStatistics haploStatistics;
    protected Scene scene;
    protected TabPane statsTabPane;
    protected MitoBenchWindow mito;
    protected Label label;
    protected Label default_list;


    public AHGDialogue(String title, LogClass logClass) {
        super(title, logClass);
    }


    public void init(MitoBenchWindow mito){
        this.mito = mito;
        haploStatistics = new HaploStatistics(mito.getTableControllerUserBench(), mito.getChartController(), logClass);
        addComponents(haploStatistics, mito);
        this.LOG = this.logClass.getLogger(this.getClass());
        addEvents();
    }

    /**
     * This method adds all components to dialogue.
     * @param haploStatistics
     */
    public void addComponents(HaploStatistics haploStatistics, MitoBenchWindow mito){

        this.statsTabPane = mito.getTabpane_statistics();
        this.scene = mito.getScene();
        this.haploStatistics = haploStatistics;
        label = new Label("Please enter comma separated list of haplogroups " +
                "\naccording to which the haplogroups should be grouped:");
        default_list = new Label("or use the default list:");

        textField_hglist = new TextField();

        okBtn = new Button("OK");
        okBtn.setId("button_ok_statistics");

        int row=0;

        default_list_checkbox = new CheckBox("Use default list");
        default_list_checkbox.setId("checkbox_hg_default_selection");
        default_list_checkbox.setSelected(false);

        dialogGrid.add(label, 0,row,3,1);
        dialogGrid.add(textField_hglist, 0,++row,3,1);
        dialogGrid.add(default_list,0,++row,1,1);
        dialogGrid.add(default_list_checkbox,1,row,1,1);

        dialogGrid.add(okBtn,2,++row,1,1);
    }


    public void addEvents(){
        okBtn.setOnAction(e -> {
            if((textField_hglist.getText().equals("") || textField_hglist.getText().startsWith("Please")) &&  !default_list_checkbox.isSelected()){
                textField_hglist.setText("Please enter list here.");

            } else {
                String[] hg_list;
                if(default_list_checkbox.isSelected()){
                    hg_list = haploStatistics.getChartController().getCoreHGs();
                } else {
                    hg_list = textField_hglist.getText().split(",");
                }
                String[] hg_list_trimmed = Arrays.stream(hg_list).map(String::trim).toArray(String[]::new);
                haploStatistics.count(hg_list_trimmed);

                TableView table = haploStatistics.writeToTable();

                statsTabPane.getTabs().remove(getTab());

                Tab tab = new Tab();
                tab.setId("tab_statistics");
                tab.setText("Count statistics");
                tab.setContent(table);
                statsTabPane.getTabs().add(tab);
                statsTabPane.getSelectionModel().select(tab);

                LOG.info("Calculate Haplotype frequencies.\nSpecified Haplotypes: " + Arrays.toString(hg_list_trimmed));

            }


        });

        Tooltip tp = new Tooltip("Default list : H,HV,I,J,K,L0,L1,L2,L3,L4,M1,N,N1a,N1b,R,R0,T,T1,T2,U,W,X");
        default_list_checkbox.setOnMouseEntered(event -> {
            Point2D p = default_list_checkbox.localToScreen(
                    default_list_checkbox.getLayoutBounds().getMaxX(),
                    default_list_checkbox.getLayoutBounds().getMaxY());

            tp.show(default_list_checkbox, p.getX(), p.getY());
        });
        default_list_checkbox.setOnMouseExited(event -> tp.hide());
    }



}
