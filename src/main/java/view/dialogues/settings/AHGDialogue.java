package view.dialogues.settings;

import Logging.LogClass;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import statistics.HaploStatistics;
import view.MitoBenchWindow;

import java.util.Arrays;

public abstract class AHGDialogue extends ATabpaneDialogue{


    protected TextField textField;
    protected Button okBtn;
    protected CheckBox default_list_checkbox;
    protected HaploStatistics haploStatistics;
    protected Scene scene;
    protected TabPane statsTabPane;
    protected MitoBenchWindow mito;


    public AHGDialogue(String title, LogClass logClass) {
        super(title, logClass);
    }


    public void init(MitoBenchWindow mito){
        haploStatistics = new HaploStatistics(mito.getTableControllerUserBench(), mito.getTreeController(), logClass);
        addComponents(haploStatistics, mito.getTabpane_statistics(), mito.getScene());
        this.LOG = this.logClass.getLogger(this.getClass());
        this.mito = mito;
        addListener();
    }

    /**
     * This method adds all components to dialogue.
     * @param haploStatistics
     */
    protected void addComponents(HaploStatistics haploStatistics, TabPane statsTabPane, Scene scene){
        this.statsTabPane = statsTabPane;
        this.scene = scene;
        this.haploStatistics = haploStatistics;
        Label label = new Label("Please enter comma separated list of haplogroups " +
                "\naccording to which the haplogroups should be grouped:");
        Label default_list = new Label("or use the default list:");

        textField = new TextField();

        okBtn = new Button("OK");
        okBtn.setId("button_ok_statistics");

        default_list_checkbox = new CheckBox("Use default list");
        default_list_checkbox.setId("checkbox_hg_default_selection");
        default_list_checkbox.setSelected(false);

        dialogGrid.add(label, 0,0,3,1);
        dialogGrid.add(textField, 0,1,3,1);
        dialogGrid.add(default_list,0,2,1,1);
        dialogGrid.add(default_list_checkbox,1,2,1,1);
        dialogGrid.add(okBtn,2,3,1,1);
    }


    public void addListener(){
        okBtn.setOnAction(e -> {
            if((textField.getText().equals("") || textField.getText().startsWith("Please")) &&  !default_list_checkbox.isSelected()){
                textField.setText("Please enter list here.");

            } else {
                String[] hg_list;
                if(default_list_checkbox.isSelected()){
                    hg_list = haploStatistics.getChartController().getCoreHGs();
                } else {
                    hg_list = textField.getText().split(",");
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
            Point2D p = default_list_checkbox.localToScreen(default_list_checkbox.getLayoutBounds().getMaxX(), default_list_checkbox.getLayoutBounds().getMaxY()); //I position the tooltip at bottom right of the node (see below for explanation)
            tp.show(default_list_checkbox, p.getX(), p.getY());
        });
        default_list_checkbox.setOnMouseExited(event -> tp.hide());
    }



}
