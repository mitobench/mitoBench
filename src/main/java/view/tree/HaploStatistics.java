package view.tree;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import view.charts.ChartController;
import view.table.TableController;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * Created by neukamm on 09.01.17.
 */
public class HaploStatistics {


    private TableController tableController;
    private ChartController chartController;
    private HashMap<String, List<XYChart.Data<String, Number>>> data_all;
    private int number_of_groups;

    public HaploStatistics(TableController tableController, TreeHaploController treeHaploController){
        this.tableController  =tableController;
        chartController = new ChartController(tableController, treeHaploController.getTreeMap());
    }

    public void getCounts(String[] HGs){
        ObservableList<ObservableList> tableItems = tableController.getTable().getItems();

        String[][] cols = chartController.prepareColumns(new String[]{"Haplogroup", "Grouping"}, tableItems);

        TableColumn haplo_col = tableController.getTableColumnByName("Haplogroup");
        TableColumn grouping_col = tableController.getTableColumnByName("Grouping");

//        List<String> columnDataHG = new ArrayList<>();
//        tableItems.stream().forEach((o)
//                -> columnDataHG.add((String)haplo_col.getCellData(o)));
//
//        Set<String> columnDataGroup = new HashSet<>();
//        tableItems.stream().forEach((o)
//                -> columnDataGroup.add((String)grouping_col.getCellData(o)));
//
//        String[] selection_haplogroups = columnDataHG.toArray(new String[columnDataHG.size()]);
//        String[] selection_groups =columnDataGroup.toArray(new String[columnDataGroup.size()]);

        String[] selection_haplogroups = cols[0];
        String[] selection_groups = cols[1];


        number_of_groups = selection_groups.length;
        HashMap<String, ArrayList> hgs_summed = chartController.reduceHGs(selection_haplogroups, HGs);
        data_all = new HashMap<String, List<XYChart.Data<String, Number>>>();
        int[] numberOfElementsPerCaregory = chartController.getNumberOfElementsPerCategory(selection_groups);

        data_all = chartController.assignHGs(hgs_summed, selection_haplogroups, selection_groups, numberOfElementsPerCaregory);

    }

    public void printStatistics() throws IOException {
        Writer writer = null;
        try {

            List<String> keys = new ArrayList<>();
            keys.addAll(data_all.keySet());
            keys.remove("Others");
            Collections.sort(keys);
            keys.add("Others");


            writer = new BufferedWriter(new FileWriter("/home/neukamm/Desktop/stats.txt"));

            // write header
            writer.write("Population\tSum\t");
            for(String key : keys){
                writer.write(key+"\t");
            }
            writer.write("\n");

            // write population HG count information
            for(int i = 0; i < number_of_groups ; i++){
                int count_all_hgs = countAllHGs(i);
                for(String key : data_all.keySet()){
                    List<XYChart.Data<String, Number>> data_list = data_all.get(key);
                    writer.write(data_list.get(i).getXValue() + "\t" + count_all_hgs + "\t" );
                    break;
                }


                for(String key : keys){
                    List<XYChart.Data<String, Number>> data_list = data_all.get(key);
                    writer.write(data_list.get(i).getYValue() + "\t");
                }

                writer.write("\n");
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {

            writer.flush();
            writer.close();
        }

    }


    private int countAllHGs(int group){
        int count=0;
        for(String key : data_all.keySet()){
            count += data_all.get(key).get(group).getYValue().intValue();
        }
        return count;
    }


}
