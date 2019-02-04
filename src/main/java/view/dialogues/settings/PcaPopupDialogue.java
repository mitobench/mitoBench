package view.dialogues.settings;

import Logging.LogClass;
import analysis.PCA;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.controlsfx.control.CheckComboBox;
import statistics.HaploStatistics;
import view.MitoBenchWindow;

import java.util.*;

public class PcaPopupDialogue extends AHGDialogue {

    private HashMap<String, Color> group_color;
    private HashMap<String, ObservableList<String>> group_members;
    private Button btn_add;
    private Set<String> groupnames;
    private GridPane grid_colors_group;
    private int row2;
    private CheckBox checkbox_use_grouping_for_colors;
    private List<CheckComboBox> comboBoxes;
    private List<TextField> textfields_with_groupnames = new ArrayList<>();
    private List<CheckComboBox> checkComboBox_with_groupmembers = new ArrayList<>();
    private int id;
    private Button btn_del;
    private PCA pca_analysis;
    private String[] hg_list_trimmed;

    public PcaPopupDialogue(String title, LogClass logClass, int pcaID) {

        super(title, logClass);
        group_color = new HashMap<>();
        group_members = new HashMap<>();
        comboBoxes = new ArrayList<>();
        id = pcaID;
    }

    @Override
    public void addComponents(HaploStatistics haploStatistics, MitoBenchWindow mito){

        this.statsTabPane = mito.getTabpane_statistics();
        this.scene = mito.getScene();
        this.haploStatistics = haploStatistics;


        label = new Label("Please enter comma separated list of haplogroups " +
                "\naccording to which the haplogroups should be grouped:");
        default_list = new Label("or use the example list:");

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
        checkbox_use_grouping_for_colors = new CheckBox("Assign one colour to more than one group");
        addCheckboxColoringListener(checkbox_use_grouping_for_colors);

        default_list_checkbox = new CheckBox("Use example list");
        default_list_checkbox.setId("checkbox_hg_default_selection");
        default_list_checkbox.setSelected(false);

        grid_colors_group = new GridPane();
        grid_colors_group.setHgap(4);
        grid_colors_group.setVgap(4);
        groupnames = new HashSet<>();
        //groupnames.addAll(mito.getGroupController().getGroupnames());
        Set<String> mySet = new HashSet<String>(Arrays.asList(new String[]{"PPP", "PP", "RP", "TRO", "EGYPA", "EGY", "MRT", "TUN", "ESH", "MAR", "SDN", "ETH", "BFA",
                "CMR", "GIN", "SYR", "IRN", "IRQ", "TUR", "GEO", "YEM", "KWT","ARE","LBN","ISR","OMN","ARM","SAU","PAK","QAT","JOR","SVN",
                "HUN","ITA","FRA","SRB","ENG","FRO","FIN","NOR","SWE","ESP","ISL"}));
        groupnames.addAll(mySet);
//        Set<String> set = new HashSet<>();
//        set.add("Ancient");
//        set.add("sub-Saharan Africa");
//        set.add("North Africa");
//        set.add("Near East");
//        set.add("Europe");
//
//        groupnames.addAll(set);
        row2=0;

        int row=0;

        dialogGrid.add(label, 0,row,3,1);
        dialogGrid.add(textField_hglist, 0,++row,3,1);
        dialogGrid.add(default_list,0,++row,1,1);
        dialogGrid.add(default_list_checkbox,1,row,1,1);
        dialogGrid.add(new Separator(), 0, ++row, 3, 1);
        dialogGrid.add(checkbox_use_grouping_for_colors, 0,++row, 1,1);
        dialogGrid.add(grid_colors_group, 0, ++row, 2,1);
        dialogGrid.add(new Separator(), 0, ++row, 3, 1);
        dialogGrid.add(okBtn,2,++row,1,1);

    }


    @Override
    public void addEvents(){

        okBtn.setOnAction(e -> {
            if((textField_hglist.getText().equals("") || textField_hglist.getText().startsWith("Please")) &&  !default_list_checkbox.isSelected()){
                textField_hglist.setText("Please enter list here.");

            } else {


                Task task1 = new Task() {
                    @Override
                    protected Object call() {
                        // calculate hg count statistics
                        String[] hg_list;
                        if(default_list_checkbox.isSelected()){
                            hg_list = haploStatistics.getChartController().getCoreHGs();
                        } else {
                            hg_list = textField_hglist.getText().split(",");
                        }
                        hg_list_trimmed = Arrays.stream(hg_list).map(String::trim).toArray(String[]::new);
                        haploStatistics.count(hg_list_trimmed);

                        // calculate PCA
                        pca_analysis = new PCA(mito.getChartController());
                        //pca_analysis.setGroups(mito.getGroupController().getGroupnames().toArray(new String[mito.getGroupController().getGroupnames().size()]));
                        pca_analysis.setGroups(new String[]{"PPP", "PP", "RP", "TRO", "EGYPA", "EGY", "MRT", "TUN", "ESH", "MAR", "SDN", "ETH", "BFA",
                                "CMR", "GIN", "SYR", "IRN", "IRQ", "TUR", "GEO", "YEM", "KWT","ARE","LBN","ISR","OMN","ARM","SAU","PAK","QAT","JOR","SVN",
                                "HUN","ITA","FRA","SRB","ENG","FRO","FIN","NOR","SWE","ESP","ISL"});

                        parseGroups();

                        return true;
                    }
                };
                mito.getProgressBarhandler().activate(task1.progressProperty());
                task1.setOnSucceeded((EventHandler<Event>) event -> {
                    TableView table = haploStatistics.writeToTable();

                    statsTabPane.getTabs().remove(getTab());

                    Tab tab = new Tab();
                    tab.setId("tab_statistics_" + id);
                    tab.setText("Count statistics (pca " + id + ")");
                    tab.setContent(table);
                    statsTabPane.getTabs().add(tab);
                    statsTabPane.getSelectionModel().select(tab);

                    LOG.info("Calculate Haplotype frequencies.\nSpecified Haplotypes: " + Arrays.toString(hg_list_trimmed));

                    double[][] values=new double[][]{
                            {0.068,0.068,0.045,0.114,0.023,0.0,0.0,0.0,0.023,0.0,0.091,0.023,0.0,0.0,0.023,0.114,0.0,0.159,0.091,0.114,0.0,0.045,0}, // ppp
                            {0.036,0.107,0.036,0.071,0.000,0.000,0.000,0.000,0.036,0.000,0.000,0.036,0.000,0.000,0.036,0.036,0.000,0.214,0.036,0.250,0.107,0.000,0.000},//pp
                            {0.000,0.105,0.053,0.053,0.105,0.000,0.000,0.000,0.000,0.000,0.105,0.053,0.000,0.000,0.053,0.105,0.105,0.053,0.000,0.158,0.000,0.053,0.000},//rp
                            {0.083,0.083,0.000,0.000,0.000,0.000,0.000,0.000,0.000,0.000,0.000,0.000,0.000,0.000,0.000,0.000,0.167,0.000,0.000,0.458,0.042,0.167,0.000},//tro
                            {0.200,0.040,0.020,0.050,0.040,0.000,0.040,0.060,0.100,0.010,0.030,0.070,0.000,0.000,0.000,0.040,0.000,0.080,0.090,0.080,0.000,0.050,0.000},//egypa
                            {0.139,0.055,0.020,0.084,0.052,0.030,0.027,0.045,0.114,0.008,0.080,0.002,0.008,0.028,0.012,0.042,0.007,0.045,0.054,0.105,0.008,0.010,0.025},//egy
                            {0.116,0.000,0.000,0.035,0.058,0.000,0.244,0.163,0.070,0.000,0.000,0.000,0.000,0.000,0.000,0.023,0.000,0.000,0.000,0.256,0.000,0.000,0.035},//mrt
                            {0.283,0.033,0.003,0.045,0.030,0.019,0.042,0.085,0.139,0.001,0.027,0.000,0.000,0.012,0.001,0.017,0.009,0.051,0.026,0.108,0.001,0.012,0.056},//tun
                            {0.167,0.000,0.000,0.083,0.000,0.000,0.042,0.375,0.083,0.000,0.042,0.000,0.000,0.042,0.000,0.000,0.000,0.000,0.000,0.167,0.000,0.000,0.000},//esh
                            {0.299,0.039,0.000,0.054,0.047,0.005,0.066,0.052,0.086,0.002,0.035,0.001,0.000,0.004,0.000,0.018,0.004,0.012,0.040,0.146,0.006,0.022,0.063},//mar
                            {0.052,0.009,0.000,0.017,0.009,0.122,0.035,0.235,0.226,0.043,0.070,0.000,0.000,0.000,0.000,0.043,0.000,0.017,0.000,0.026,0.000,0.009,0.087},//sdn
                            {0.000,0.033,0.008,0.017,0.017,0.092,0.017,0.133,0.317,0.058,0.092,0.058,0.000,0.000,0.000,0.083,0.000,0.000,0.000,0.017,0.000,0.000,0.058},//eth
                            {0.127,0,0,0,0,0,0.209,0.269,0.254,0.007,0.052,0,0,0,0,0,0,0,0,0.022,0,0,0.06},//bfa
                            {0.002,0,0,0,0,0.076,0.363,0.187,0.338,0.018,0,0,0,0,0,0,0,0,0,0.012,0,0,0.003},//cmr
                            {0,0,0,0,0,0.054,0.157,0.42,0.295,0,0.011,0,0,0,0,0,0,0,0,0.051,0,0,0.011},//gin
                            {0.287,0.029,0.022,0.096,0.066,0.007,0.007,0.015,0.015,0.007,0,0,0,0.015,0.007,0.037,0,0.044,0.081,0.191,0.029,0.007,0.037},//syr
                            {0.182,0.088,0.025,0.138,0.059,0.003,0.001,0.006,0.003,0,0.001,0.025,0.003,0.014,0.026,0.014,0.01,0.039,0.042,0.197,0.023,0.019,0.082},//irn
                            {0.173,0.095,0.01,0.133,0.052,0.01,0.014,0.026,0.026,0,0.012,0.012,0,0.029,0.029,0.055,0.005,0.055,0.036,0.176,0.021,0.012,0.021},//irq
                            {0.316,0.053,0.016,0.094,0.064,0.004,0,0.002,0.01,0,0,0.004,0.004,0.012,0.01,0.01,0.006,0.031,0.041,0.177,0.027,0.031,0.086},//tur
                            {0.232,0.037,0.007,0.03,0.133,0,0,0,0,0,0.011,0.011,0,0.018,0.018,0.007,0.03,0.044,0.089,0.236,0.033,0.03,0.033},//geo
                            {0.057,0.05,0.006,0.166,0.061,0.067,0.015,0.048,0.14,0.01,0.015,0.019,0.019,0.004,0.025,0.107,0.006,0.017,0.021,0.071,0.006,0.008,0.061},//yem
                            {0.101,0.023,0.017,0.162,0.026,0.009,0.009,0.055,0.017,0.009,0.023,0.052,0.006,0.014,0.012,0.162,0.003,0.026,0.04,0.136,0.014,0.026,0.058}, //kwt
                            {0.098,0.073,0.028,0.112,0.067,0.042,0.014,0.048,0.062,0.008,0.006,0.022,0.003,0.017,0.028,0.056,0,0.014,0.02,0.14,0.036,0.011,0.095},//are
                            {0.344,0.04,0.016,0.077,0.084,0,0.001,0.006,0.01,0,0.01,0.005,0.001,0.029,0.004,0.032,0.005,0.06,0.045,0.147,0.019,0.022,0.042},//lbn
                            {0.293,0.04,0.015,0.079,0.104,0.007,0.004,0.028,0.033,0.001,0.012,0.009,0.003,0.022,0.009,0.03,0.013,0.034,0.034,0.101,0.01,0.088,0.03},//isr
                            {0.158,0.011,0.032,0.095,0.084,0.063,0,0,0.116,0,0,0.042,0,0,0.021,0.179,0,0,0.021,0.126,0,0,0.053},//omn
                            {0.294,0.06,0.014,0.096,0.073,0,0,0,0,0,0,0.023,0.009,0.028,0.014,0.005,0.009,0.055,0.05,0.22,0.009,0.037,0.005},//arm
                            {0.091,0.011,0.009,0.212,0.039,0.012,0.005,0.035,0.035,0.004,0.037,0.026,0.023,0.023,0.005,0.177,0,0.023,0.04,0.112,0.011,0.028,0.044},//sau
                            {0.109,0.084,0.016,0.04,0.018,0.001,0,0.006,0.016,0,0,0.016,0,0.004,0.046,0.016,0.004,0.025,0.018,0.228,0.029,0.004,0.32},//pak
                            {0.079,0.045,0.011,0.18,0.034,0,0.011,0.034,0.124,0,0.034,0,0.011,0.011,0,0.079,0,0,0.045,0.18,0.034,0.011,0.079}, // qat
                            {0.253,0.06,0.016,0.06,0.044,0.011,0.005,0.033,0.088,0,0.022,0.016,0,0.022,0.005,0.027,0.005,0.005,0.066,0.225,0.011,0.011,0.011},//jor
                            {0.437,0.02,0.022,0.109,0.053,0,0,0,0,0,0,0,0.004,0,0,0,0.002,0.029,0.073,0.174,0.02,0.011,0.045},//svn
                            {0.309,0.013,0.018,0.068,0.131,0,0,0.004,0.001,0,0,0,0.014,0.025,0.003,0.001,0.003,0.02,0.079,0.081,0.026,0.061,0.143},//hun
                            {0.421,0.036,0.014,0.085,0.077,0,0.001,0.002,0.004,0,0.006,0.002,0.002,0.01,0.004,0.008,0.006,0.032,0.087,0.133,0.017,0.019,0.032},//ita
                            {0.447,0.036,0.02,0.078,0.086,0,0.002,0.004,0.003,0,0,0.003,0.003,0.004,0.006,0.004,0.008,0.017,0.065,0.143,0.017,0.012,0.041},//Fra
                            {0.448,0.013,0.019,0.123,0.045,0,0,0,0,0,0,0,0.019,0.006,0,0,0.006,0.032,0.039,0.136,0.052,0.013,0.045},//srb
                            {0.435,0.001,0.038,0.115,0.077,0,0.001,0,0.001,0,0,0,0.002,0.003,0,0,0.007,0.015,0.069,0.167,0.015,0.018,0.037},//eng
                            {0.711,0,0.058,0.182,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0.008,0.017,0.017,0,0.008},//fro
                            {0.343,0.004,0.037,0.053,0.04,0,0,0,0,0,0,0,0.003,0.001,0.002,0,0,0.019,0.023,0.255,0.084,0.012,0.123},//fin
                            {0.419,0.004,0.017,0.094,0.049,0,0,0.001,0.001,0,0,0.001,0.003,0.004,0,0,0.004,0.009,0.069,0.217,0.016,0.004,0.086},//nor
                            {0.38,0.016,0.022,0.062,0.052,0,0,0.004,0.001,0,0,0,0,0,0,0,0,0.021,0.035,0.264,0.01,0.01,0.121},//swe
                            {0.432,0.022,0.011,0.071,0.06,0,0.007,0.012,0.011,0,0.005,0.001,0,0.002,0.003,0.006,0.002,0.021,0.071,0.173,0.013,0.018,0.059},//esp
                            {0.378,0.035,0.042,0.14,0.102,0,0,0,0.002,0,0,0,0,0,0,0,0.011,0.005,0.1,0.136,0.007,0.015,0.026}//isl

                    };
                    //double[][] result_pca = pca_analysis.calculate(haploStatistics.getFrequencies(), 2);
                    double[][] result_pca = pca_analysis.calculate(values, 2);
                    //group_members.clear();
                    pca_analysis.plot(
                            result_pca,
                            mito.getPrimaryStage(),
                            logClass,
                            mito.getTabpane_statistics(),
                            group_members);

                    Tab tab_pca = new Tab("PCA (pca " + id + ")");
                    tab_pca.setId("tab_pca_plot_" + id);
                    tab_pca.setContent(pca_analysis.getPca_plot().getSc());
                    mito.getTabpane_visualization().getTabs().add(tab_pca);
                    mito.getTabpane_visualization().getSelectionModel().select(tab_pca);

                    LOG.info("Calculate PCA");


                    mito.getProgressBarhandler().stop();
                });

                new Thread(task1).start();

            }
        });

        Tooltip tp = new Tooltip("Example list : H,HV,I,J,K,L0,L1,L2,L3,L4,M1,N,N1a,N1b,R,R0,T,T1,T2,U,W,X");
        default_list_checkbox.setOnMouseEntered(event -> {
            Point2D p = default_list_checkbox.localToScreen(default_list_checkbox.getLayoutBounds().getMaxX(), default_list_checkbox.getLayoutBounds().getMaxY()); //I position the tooltip at bottom right of the node (see below for explanation)
            tp.show(default_list_checkbox, p.getX(), p.getY());
        });
        default_list_checkbox.setOnMouseExited(event -> tp.hide());
    }


    /**
     * Add event to delete button.
     * @param btn_remove
     */
    private void addBtnDelEvent(Button btn_remove) {

        btn_remove.setOnAction(e -> {
            deleteRow(grid_colors_group, GridPane.getRowIndex(btn_remove));
        });
    }

    /**
     * Add event to add new group button.
     * @param btn_add
     */
    private void addBtnAddEvent(Button btn_add){

        btn_add.setOnAction(e -> addColoringRow());
    }




    private void addColoringRow(){

        if(groupnames.size() != 0){
            CheckComboBox combo = new CheckComboBox();
            combo.setId("group_members_"+row2);

            groupnames = removedUsedGroups(groupnames);
            combo.getItems().addAll(groupnames);
            comboBoxes.add(combo);

            TextField field_text = new TextField("New groupname");
            field_text.setId("group_id_"+row2);

            btn_add = new Button("Add more");
            addBtnAddEvent(btn_add);
            btn_del = new Button("Delete");
            addBtnDelEvent(btn_del);

            grid_colors_group.add(field_text, 0, row2,1,1);
            grid_colors_group.add(combo, 1, row2,1,1);
            grid_colors_group.add(btn_add, 2, row2, 1,1);
            grid_colors_group.add(btn_del, 3, row2, 1,1);

            row2++;
            this.checkComboBox_with_groupmembers.add(combo);
            this.textfields_with_groupnames.add(field_text);
        }
    }



    /**
     *
     * @param checkBox
     */
    private void addCheckboxColoringListener(CheckBox checkBox){
        checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
            if(new_val){
                addColoringGridpaneNewGrouping(grid_colors_group);
            }
            else {
                //addColoringGridpaneOwnGrouping(grid_colors_group);
                grid_colors_group.getChildren().clear();
            }
        });
    }


    /**
     * Display this gridpane if user just want to color existing grouping
     *
     * @param grid_colors_group
     */
    private void addColoringGridpaneOwnGrouping(GridPane grid_colors_group) {
        grid_colors_group.getChildren().clear();
        row2=0;

        for(String group : groupnames){
            ColorPicker colorPicker = new ColorPicker();
            colorPicker.setId("group_color_"+row2);

            TextField text_field = new TextField(group);
            text_field.setId("group_id_"+row2);

            grid_colors_group.add(text_field, 0, row2,1,1);
            //grid_colors_group.add(colorPicker, 2, row2,1,1);

            row2++;
        }
    }

    /**
     * Display this gridpane if user want to set own coloring/grouping.
     *
     * @param grid_colors_group
     */

    private void addColoringGridpaneNewGrouping(GridPane grid_colors_group) {
        row2=0;
        grid_colors_group.getChildren().clear();
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setId("group_color_"+row2);

        groupnames = removedUsedGroups(groupnames);

        CheckComboBox combo = new CheckComboBox();
        combo.setId("group_members_"+row2);
        combo.getItems().addAll(groupnames);
        comboBoxes.add(combo);


        TextField field_text = new TextField("New groupname");
        field_text.setId("group_id_");


        btn_add = new Button("Add more");
        btn_del = new Button("Delete");
        addBtnAddEvent(btn_add);
        addBtnDelEvent(btn_del);

        grid_colors_group.add(field_text, 0, row2,1,1);
        grid_colors_group.add(combo, 1, row2,1,1);
        //grid_colors_group.add(colorPicker, 2, row2,1,1);
        grid_colors_group.add(btn_add, 2, row2, 1,1);
        grid_colors_group.add(btn_del, 3, row2, 1,1);

        row2++;
    }

    /**
     * Remove groups that are already assigned to another group.
     *
     * @param groupnames
     * @return
     */
    private Set<String> removedUsedGroups(Set<String> groupnames) {

        for(CheckComboBox checkComboBox : comboBoxes){
            groupnames.removeAll(checkComboBox.getCheckModel().getCheckedItems());
        }

        return groupnames;
    }


    /**
     * Delete entire row from grid pane.
     * @param grid
     * @param row
     */
    private void deleteRow(GridPane grid, final int row) {
        groupnames = removedUsedGroups(groupnames);
        Set<Node> deleteNodes = new HashSet<>();
        for (Node child : grid.getChildren()) {
            // get index from child
            Integer rowIndex = GridPane.getRowIndex(child);

            // handle null values for index=0
            int r = rowIndex == null ? 0 : rowIndex;

            if (r > row) {
                // decrement rows for rows after the deleted row
                GridPane.setRowIndex(child, r-1);
            } else if (r == row) {
                // collect matching rows for deletion
                deleteNodes.add(child);
            }
        }


        // add groups to groupnames
        ObservableList<String> checkedItems = comboBoxes.get(row).getCheckModel().getCheckedItems();
        Set<String> tmp = new HashSet<>();
        tmp.addAll(checkedItems);
        groupnames.addAll(tmp);

        // remove nodes from row
        grid.getChildren().removeAll(deleteNodes);
        comboBoxes.remove(row);
        row2--;
    }

    /**
     * parse the colors set for grouping
     */
    private void parseColors() {
        String name = "";
        for (Node child : grid_colors_group.getChildren()) {
            // get index from child
            if(child.getId()==null){
                // todo: set id to all children
            } else if(child.getId().startsWith("group_id_")){
                TextField t = (TextField) child;
                name = t.getText();
            } else if(child.getId().startsWith("group_color_")){
                ColorPicker colorPicker = (ColorPicker) child;
                group_color.put(name, colorPicker.getValue());
            }
        }
    }


    public void parseGroups() {
        String name = "";
        for (Node child : grid_colors_group.getChildren()) {
            // get index from child
            if(child.getId()==null){
                // todo: set id to all children
            } else if(child.getId().startsWith("group_id_")){
                TextField t = (TextField) child;
                name = t.getText();
            } else if(child.getId().startsWith("group_members_")){
                CheckComboBox members = (CheckComboBox) child;
                group_members.put(name, members.getCheckModel().getCheckedItems());
            }
        }
    }




}
