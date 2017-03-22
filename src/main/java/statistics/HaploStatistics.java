package statistics;

import Logging.LogClass;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import view.charts.ChartController;
import view.charts.ProfilePlot;
import view.table.controller.TableControllerMutations;
import view.table.controller.TableControllerUserBench;
import controller.HaplotreeController;

import java.util.*;

/**
 * Created by neukamm on 09.01.17.
 */
public class HaploStatistics {


    private TableControllerUserBench tableController;
    private TableControllerMutations tableControllerMutations;
    private ChartController chartController;
    private HashMap<String, List<XYChart.Data<String, Number>>> data_all;
    private int number_of_groups;


    public HaploStatistics(TableControllerUserBench tableController, HaplotreeController treeHaploController,
                           LogClass LOGClass){

        this.tableController = tableController;
        chartController = new ChartController();
        chartController.init(tableController, treeHaploController.getTreeMap());
        tableControllerMutations = new TableControllerMutations(LOGClass);
        tableControllerMutations.init();
    }

    /**
     * This method gets counts for each haplogroup (per group). haplogropus are summarized to user defined "core-groups".
     * @param coreHGs user defined core HGs
     */
    public void count(String[] coreHGs){
        ObservableList<ObservableList> tableItems = tableController.getTable().getItems();

        // get set of unique group and haplogroup entries
        String[][] cols = chartController.prepareColumns(new String[]{"Haplogroup", "Grouping"}, tableItems);
        String[] selection_haplogroups = cols[0];
        String[] selection_groups = cols[1];
        number_of_groups = selection_groups.length;
        HashMap<String, ArrayList> hgs_summarized = chartController.summarizeHaolpgroups(selection_haplogroups, coreHGs);
        data_all = chartController.assignHGs(hgs_summarized, selection_haplogroups, selection_groups);


    }



    /**
     * This method writes count information to table in GUI.
     *
     * @param data_all
     * @param scene
     * @return
     */
    public TableView writeToTable(HashMap<String, List<XYChart.Data<String, Number>>>  data_all, Scene scene){
        List<String> keys = new ArrayList<>();
        keys.addAll(data_all.keySet());
        keys.remove("Others");
        Collections.sort(keys);
        keys.add("Others");

        TableView<ObservableList> table = tableControllerMutations.getTable();
        tableControllerMutations.addColumn("Groups", 0);
        tableControllerMutations.addColumn("Total Number", 1);
        int k = 2;
        for(String key : keys){
            tableControllerMutations.addColumn(key, k);
            k++;
        }

        // add data (table content)
        // write population HG count information
        ObservableList<ObservableList> entries = FXCollections.observableArrayList();
        for(int i = 0; i < number_of_groups ; i++){
            ObservableList  entry = FXCollections.observableArrayList();
            int count_all_hgs = countAllHGs(i);
            for(String key : data_all.keySet()){
                List<XYChart.Data<String, Number>> data_list = data_all.get(key);
                entry.add(data_list.get(i).getXValue());
                entry.add(count_all_hgs);
                break;
            }


            for(String key : keys){
                List<XYChart.Data<String, Number>> data_list = data_all.get(key);
                entry.add(data_list.get(i).getYValue().intValue());
            }

            entries.add(entry);

        }

        // clear Items in table
        table.getItems().removeAll(table.getItems());
        //FINALLY ADDED TO TableView
        table.getItems().addAll(entries);

        return table;

    }


    /**
     * This method counts all occurrences of one haplogroup in a group.
     *
     * @param group
     * @return
     */
    public int countAllHGs(int group){
        int count=0;
        for(String key : data_all.keySet()){
            count += data_all.get(key).get(group).getYValue().intValue();
        }
        return count;
    }


    /**
     * This method adds listener to each row in count table.
     * When cursor enters row, the corresponding line in profile plot is highlighted.
     * @param table
     * @param profilePlot
     */
    public void addListener(TableView table, ProfilePlot profilePlot){



        table.setRowFactory( tv -> {
            TableRow<ObservableList> row = new TableRow<>();
            row.setOnMouseEntered(event -> {
                if (! row.isEmpty()) {
                    ObservableList rowData = row.getItem();
                    String group = rowData.get(0).toString();
                    row.setStyle("-fx-background-color: dodgerblue;");

                    // set line width of corresponding line to 6px
                    for(XYChart.Series serie : profilePlot.getPlot().getData()){
                        if(serie.getName().equals(group)){
                            serie.getNode().setStyle(" -fx-stroke-width: 6px;");
                        }
                    }

                }
            });

            row.setOnMouseExited(event -> {
                if (! row.isEmpty()) {
                    row.setStyle("");
                    ObservableList rowData = row.getItem();
                    String group = rowData.get(0).toString();

                    for(XYChart.Series serie : profilePlot.getPlot().getData()){
                        if(serie.getName().equals(group)){
                            serie.getNode().setStyle(" -fx-stroke-width: 2px;");
                        }
                    }
                }
            });


            return row ;
        });
    }

    public HashMap<String, List<XYChart.Data<String, Number>>> getData_all() {
        return data_all;
    }

    public ChartController getChartController() {
        return chartController;
    }

    public TableControllerUserBench getTableController() {
        return tableController;
    }

    public int getNumber_of_groups() {
        return number_of_groups;
    }

    public void setTableController(TableControllerUserBench tableController) {
        this.tableController = tableController;
    }

    public void setChartController(ChartController chartController) {
        this.chartController = chartController;
    }

    public void setData_all(HashMap<String, List<XYChart.Data<String, Number>>> data_all) {
        this.data_all = data_all;
    }

    public void setNumber_of_groups(int number_of_groups) {
        this.number_of_groups = number_of_groups;
    }
}