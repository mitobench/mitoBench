package statistics;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import view.charts.ChartController;
import view.table.TableController;
import view.tree.TreeHaploController;

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
        data_all = chartController.assignHGs(hgs_summarized,
                                             selection_haplogroups,
                                             selection_groups,
                                             chartController.getNumberOfElementsPerCategory(selection_groups));

    }

    /**
     * This method prints counting information.
     * @throws IOException
     */
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


    public HashMap<String, List<XYChart.Data<String, Number>>> getData_all() {
        return data_all;
    }
}
