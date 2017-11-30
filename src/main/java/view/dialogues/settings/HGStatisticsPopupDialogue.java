package view.dialogues.settings;

import Logging.LogClass;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import statistics.HaploStatistics;

import java.util.Arrays;


/**
 * Created by neukamm on 10.01.17.
 */
public class HGStatisticsPopupDialogue extends ATabpaneDialogue {

    private TextField textField_hglist;
    private Button okBtn;
    private CheckBox default_list_checkbox;
    private HaploStatistics haploStatistics;
    private Scene scene;
    private TabPane statsTabPane;

    public HGStatisticsPopupDialogue(String title, LogClass LOGClass){
        super(title, LOGClass);
        dialogGrid.setId("statistics_popup");

    }

    public void init(HaploStatistics haploStatistics, TabPane statsTabpane){
        addComponents(haploStatistics, statsTabpane);
        this.LOG = this.logClass.getLogger(this.getClass());
        addListener();
    }

    /**
     * This method adds all components to dialogue.
     * @param haploStatistics
     */
    private void addComponents(HaploStatistics haploStatistics, TabPane statsTabPane){
        this.statsTabPane = statsTabPane;
        this.haploStatistics = haploStatistics;
        Label label = new Label("Please enter comma separated list of haplogroups " +
                "\naccording to which the haplogroups should be grouped:");
        Label default_list = new Label("or use the default list:");

        textField_hglist = new TextField();
        if(haploStatistics.getChartController().getCustomHGList()!=null) {
            if (haploStatistics.getChartController().getCustomHGList().length != 0) {
                String hgs = "";
                for(String s : haploStatistics.getChartController().getCustomHGList())
                    hgs += s + ",";
                textField_hglist.setText(hgs.substring(0, hgs.length()-1));
            }
        }
        okBtn = new Button("OK");
        okBtn.setId("button_ok_statistics");

        default_list_checkbox = new CheckBox("Use default list");
        default_list_checkbox.setId("checkbox_hg_default_selection");
        default_list_checkbox.setSelected(false);

        dialogGrid.add(label, 0,0,3,1);
        dialogGrid.add(textField_hglist, 0,1,3,1);
        dialogGrid.add(default_list,0,2,1,1);
        dialogGrid.add(default_list_checkbox,1,2,1,1);
        dialogGrid.add(okBtn,2,3,1,1);
    }

    public void addListener(){
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
            Point2D p = default_list_checkbox.localToScreen(default_list_checkbox.getLayoutBounds().getMaxX(), default_list_checkbox.getLayoutBounds().getMaxY()); //I position the tooltip at bottom right of the node (see below for explanation)
            tp.show(default_list_checkbox, p.getX(), p.getY());
        });
        default_list_checkbox.setOnMouseExited(event -> tp.hide());
    }



}
