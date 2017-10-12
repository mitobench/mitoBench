package view.dialogues.settings;

import Logging.LogClass;
import analysis.PCA;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
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
    private Button btn_remove;
    private CheckBox checkbox_use_grouping_for_colors;
    private List<CheckComboBox> comboBoxes;

    public PcaPopupDialogue(String title, LogClass logClass) {

        super(title, logClass);
        group_color = new HashMap<>();
        group_members = new HashMap<>();
        comboBoxes = new ArrayList<>();
    }

    @Override
    public void addComponents(HaploStatistics haploStatistics, MitoBenchWindow mito){

        this.statsTabPane = mito.getTabpane_statistics();
        this.scene = mito.getScene();
        this.haploStatistics = haploStatistics;


        label = new Label("Please enter comma separated list of haplogroups " +
                "\naccording to which the haplogroups should be grouped:");
        default_list = new Label("or use the default list:");

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
        checkbox_use_grouping_for_colors = new CheckBox("Set new grouping for coloring");
        addCheckboxColoringListener(checkbox_use_grouping_for_colors);

        default_list_checkbox = new CheckBox("Use default list");
        default_list_checkbox.setId("checkbox_hg_default_selection");
        default_list_checkbox.setSelected(false);

        grid_colors_group = new GridPane();
        grid_colors_group.setHgap(4);
        grid_colors_group.setVgap(4);
        groupnames = new HashSet<>();
        groupnames.addAll(mito.getGroupController().getGroupnames());
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

                // calculate hg count statistics
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


                // calculate PCA
                PCA pca_analysis = new PCA(mito.getChartController());
                pca_analysis.setGroups(mito.getGroupController().getGroupnames().toArray(new String[mito.getGroupController().getGroupnames().size()]));
                double[][] result_pca = pca_analysis.calculate(haploStatistics.getFrequencies(), 2);
                pca_analysis.plot(result_pca, group_color, mito.getPrimaryStage(), logClass, mito.getTabpane_statistics(), group_members);

                Tab tab_pca = new Tab("PCA");
                tab_pca.setId("tab_pca_plot");
                tab_pca.setContent(pca_analysis.getPca_plot().getSc());
                mito.getTabpane_visualization().getTabs().add(tab_pca);
                LOG.info("Calculate PCA");
            }

        });

        Tooltip tp = new Tooltip("Default list : H,HV,I,J,K,L0,L1,L2,L3,L4,M1,N,N1a,N1b,R,R0,T,T1,T2,U,W,X");
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

            TextField field_text = new TextField("Groupname");
            field_text.setId("group_id_"+row2);

            btn_add = new Button("Add more");
            addBtnAddEvent(btn_add);
            btn_remove = new Button("Delete");
            addBtnDelEvent(btn_remove);

            grid_colors_group.add(field_text, 0, row2,1,1);
            grid_colors_group.add(combo, 1, row2,1,1);
            grid_colors_group.add(btn_add, 2, row2, 1,1);
            grid_colors_group.add(btn_remove, 3, row2, 1,1);

            row2++;
        }
    }



    /**
     *
     * @param checkBox
     */
    private void addCheckboxColoringListener(CheckBox checkBox){
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                if(new_val){
                    addColoringGridpaneNewGrouping(grid_colors_group);
                }
                else {
                    //addColoringGridpaneOwnGrouping(grid_colors_group);
                    grid_colors_group.getChildren().clear();
                }
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


        TextField field_text = new TextField("Groupname");
        field_text.setId("group_id_");


        btn_add = new Button("Add more");
        addBtnAddEvent(btn_add);

        grid_colors_group.add(field_text, 0, row2,1,1);
        grid_colors_group.add(combo, 1, row2,1,1);
        //grid_colors_group.add(colorPicker, 2, row2,1,1);
        grid_colors_group.add(btn_add, 2, row2, 1,1);

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


    private void parseGroups() {
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
