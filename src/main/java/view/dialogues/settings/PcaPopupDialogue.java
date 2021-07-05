package view.dialogues.settings;

import Logging.LogClass;
import analysis.PCA_Analysis;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.controlsfx.control.CheckComboBox;

import java.util.*;
import java.util.List;

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
    private PCA_Analysis pca_alternative;
    private double[][] result_pca;

    public PcaPopupDialogue(String title, LogClass logClass, int pcaID) {

        super(title, logClass);
        group_color = new HashMap<>();
        group_members = new HashMap<>();
        comboBoxes = new ArrayList<>();
        id = pcaID;
        addAdditionalComponents();
    }


    public void addAdditionalComponents(){

        checkbox_use_grouping_for_colors = new CheckBox("Assign one colour to more than one group");
        addCheckboxColoringListener(checkbox_use_grouping_for_colors);

        grid_colors_group = new GridPane();
        grid_colors_group.setHgap(4);
        grid_colors_group.setVgap(4);
        groupnames = new HashSet<>();

        dialogGrid.add(checkbox_use_grouping_for_colors, 0,++row, 1,1);
        dialogGrid.add(grid_colors_group, 0, ++row, 2,1);

    }


    @Override
    public void addListener(){

        okBtn.setOnAction(e -> {
            if((combobox_hglist.getSelectionModel().getSelectedItem().toString().equals("") || combobox_hglist.getSelectionModel().getSelectedItem().toString().startsWith("Please"))){
                combobox_hglist.getItems().add("Please enter list here.");
                combobox_hglist.getSelectionModel().select("Please enter list here.");

            } else {
                Task task = new Task() {
                    @Override
                    protected Object call() {
                        System.out.println("Start PCA");
                        LOG.info("Start PCA");
                        System.out.println("\t... prepare HG list");
                        LOG.info("\t... prepare HG list");
                        // calculate hg count statistics
                        calculateTrimmedHGList();
                        haploStatistics.count(getHg_list_trimmed());

                        // calculate PCA

                        pca_alternative = new PCA_Analysis();

                        System.out.println("\t... calculate haplogroup frequencies");
                        LOG.info("\t... calculate haplogroup frequencies");
                        pca_alternative.setData(haploStatistics.getFrequencies());
                        System.out.println("\t... set groups");
                        LOG.info("\t... set groups");
                        pca_alternative.setGroups(mito.getGroupController().getGroupnames().toArray(new String[mito.getGroupController().getGroupnames().size()]));
                        System.out.println("\t... calculate PCs");
                        LOG.info("\t... calculate PCs");
                        pca_alternative.calculate();

                        result_pca = pca_alternative.getResult();

                        return true;
                    }
                };
                mito.getProgressBarhandler().activate(task);

                task.setOnCancelled((EventHandler<Event>) event -> {
                    statsTabPane.getTabs().remove(getTab());
                    mito.getProgressBarhandler().stop();
                });

                task.setOnSucceeded((EventHandler<Event>) event -> {
                    TableView table = haploStatistics.writeToTable();

                    statsTabPane.getTabs().remove(getTab());

                    Tab tab = new Tab();
                    tab.setId("tab_statistics_" + id);
                    tab.setText("Count statistics (pca " + id + ")");
                    tab.setContent(table);
                    statsTabPane.getTabs().add(tab);
                    statsTabPane.getSelectionModel().select(tab);

                    LOG.info("Calculate Haplotype frequencies.\nSpecified Haplotypes: " + Arrays.toString(getHg_list_trimmed()));

                    //group_members.clear();
                    pca_alternative.plot(
                            result_pca,
                            mito.getPrimaryStage(),
                            logClass,
                            mito.getTabpane_statistics(),
                            group_members,
                            mito.getChartController(),
                            pca_alternative.getVariancePC1(),
                            pca_alternative.getVariancePC2(),
                            haploStatistics,
                            mito.getTableControllerUserBench()
                    );

                    Tab tab_pca = new Tab("PCA (pca " + id + ")");
                    tab_pca.setId("tab_pca_plot_" + id);

                    BorderPane basis = new BorderPane();
                    basis.setCenter(pca_alternative.getPca_plot().getSc());
                    basis.setBottom(pca_alternative.getPca_plot().getBottomBox());

                    tab_pca.setContent(basis);
                    mito.getTabpane_visualization().getTabs().add(tab_pca);
                    mito.getTabpane_visualization().getSelectionModel().select(tab_pca);

                    LOG.info("Calculate PCA");
                    mito.getProgressBarhandler().stop();
                });

                new Thread(task).start();

            }
        });

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
