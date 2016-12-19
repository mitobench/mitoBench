package view.charts;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import view.table.TableController;

import java.util.*;

/**
 * Created by neukamm on 19.12.16.
 */
public class ChartController {

    private TableController tableController;
    private List<String> used_hgs;
    private HashMap<String, List<String>> treeMap;
    private HashMap<String, HashMap<String, Double>> weights;



    public ChartController(TableController tableController, HashMap<String, List<String>> treeMap) {
        this.tableController = tableController;
        this.treeMap = treeMap;
    }


    /**
     * This method adds all data to the bar chart.
     * @param barPlotHaplo
     * @param columnData
     * @param haplo_col
     */
    public void addDataBarChart(BarPlotHaplo barPlotHaplo, List<String> columnData, TableColumn haplo_col){

        for (Object item : tableController.getTable().getItems()) {
            columnData.add((String)haplo_col.getCellObservableValue(item).getValue());
        }
        String[] seletcion_haplogroups = columnData.toArray(new String[columnData.size()]);

        barPlotHaplo.clearData();

        if (seletcion_haplogroups.length !=0) {
            barPlotHaplo.addData(tableController.getDataHist());

        }

    }


    /**
     * This method adds all data to the stacked bar chart.
     *
     * @param stackedBar
     * @param cols
     * @param selection_haplogroups
     * @param selection_groups
     */

    public void addDataStackedBarChart(StackedBar stackedBar, String[][] cols, String[] selection_haplogroups, String[] selection_groups) {

        // get number of elements per group
        int[] numberOfElementsPerCaregory = getNumberOfElementsPerCategory(selection_groups);

        // reduce haplogroups to maximum size of 20
        if (selection_haplogroups.length >= 20) {

            stackedBar.clearData();
            stackedBar.setCategories(selection_groups);
            HashMap<String, ArrayList> hgs_summed = reduceHGs(selection_haplogroups);
            HashMap<String, List<XYChart.Data<String, Number>>> data_all = new HashMap<String, List<XYChart.Data<String, Number>>>();

            for (String key : hgs_summed.keySet()) {

                ArrayList<String> subHGs = hgs_summed.get(key);
                List<XYChart.Data<String, Number>> data_list = new ArrayList<XYChart.Data<String, Number>>();
                for (int i = 0; i < selection_groups.length; i++) {
                    String group = selection_groups[i];
                    double count = 0.0;
                    for (String hg : subHGs) {
                        count += tableController.getCountPerHG(hg, group, tableController.getColIndex("Haplogroup"), tableController.getColIndex("Grouping"));
                    }

                    double percentage = (count / numberOfElementsPerCaregory[i]) * 100;
                    XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(group, roundValue(percentage));
                    data_list.add(data);

                }
                data_all.put(key, data_list);
            }

            // iterate over used hgs and check if there are some unused hgs
            List<String> unused_hgs = new ArrayList<String>();
            for (String hg : selection_haplogroups) {
                if (!used_hgs.contains(hg)) {
                    unused_hgs.add(hg);
                }
            }

            // get all so far unused haplogroups to assign them to "others"
            List<XYChart.Data<String, Number>> data_list = new ArrayList<XYChart.Data<String, Number>>();
            for (int i = 0; i < selection_groups.length; i++) {
                String group = selection_groups[i];
                double count_others = 0.0;

                for (String hg : unused_hgs) {
                    count_others += tableController.getCountPerHG(hg, group, tableController.getColIndex("Haplogroup"), tableController.getColIndex("Grouping"));
                }
                XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(group, roundValue((count_others / numberOfElementsPerCaregory[i]) * 100));
                data_list.add(data);

            }
            data_all.put("Others", data_list);

            // add HGs in correct order
            stackedBar.addSeries(data_all.get("L0"), "L0");
            stackedBar.addSeries(data_all.get("L1"), "L1");
            stackedBar.addSeries(data_all.get("L2"), "L2");
            stackedBar.addSeries(data_all.get("L3"), "L3");
            stackedBar.addSeries(data_all.get("L4"), "L4");
            stackedBar.addSeries(data_all.get("M1"), "M1");
            stackedBar.addSeries(data_all.get("N"), "N");
            stackedBar.addSeries(data_all.get("I"), "I");
            stackedBar.addSeries(data_all.get("W"), "W");
            stackedBar.addSeries(data_all.get("X"), "X");
            stackedBar.addSeries(data_all.get("R"), "R");
            stackedBar.addSeries(data_all.get("R0"), "R0");
            stackedBar.addSeries(data_all.get("U"), "U");
            stackedBar.addSeries(data_all.get("K"), "K");
            stackedBar.addSeries(data_all.get("J"), "J");
            stackedBar.addSeries(data_all.get("T"), "T");
            stackedBar.addSeries(data_all.get("T1"), "T1");
            stackedBar.addSeries(data_all.get("T2"), "T2");
            stackedBar.addSeries(data_all.get("H"), "H");
            stackedBar.addSeries(data_all.get("HV"), "HV");
            stackedBar.addSeries(data_all.get("Others"), "Others");

        } else {        // add data if less than 20 haplogroups

            stackedBar.clearData();
            stackedBar.setCategories(selection_groups);

            // consider Hgs only once per group
            if (selection_haplogroups.length != 0) {
                for (int i = 0; i < selection_haplogroups.length; i++) {

                    List<XYChart.Data<String, Number>> data_list = new ArrayList<XYChart.Data<String, Number>>();
                    // fill data_list : <group(i), countHG >
                    for (int j = 0; j < selection_groups.length; j++) {

                        double count_per_HG = tableController.getCountPerHG(selection_haplogroups[i], selection_groups[j], tableController.getColIndex("Haplogroup"),
                                tableController.getColIndex("Grouping"));

                        XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(selection_groups[j], roundValue((count_per_HG / numberOfElementsPerCaregory[i]) * 100));
                        data_list.add(data);
                    }
                    stackedBar.addSeries(data_list, selection_haplogroups[i]);
                }
            }
        }
    }


    /**
     * This method counts the number of items assigned to each category.
     * @param categories
     * @return
     */

    private int[] getNumberOfElementsPerCategory(String[] categories){
        int[] counts = new int[categories.length];
        Arrays.fill(counts, 0);
        ObservableList<ObservableList> selectedTableItems = tableController.getTable().getItems();
        for(int i = 0; i < categories.length; i++) {
            String cat = categories[i];

            for (int k = 0; k < selectedTableItems.size(); k++) {
                ObservableList list = selectedTableItems.get(k);
                if (list.get(tableController.getColIndex("Grouping")).equals(cat)) {
                    counts[i]++;
                }
            }
        }
        return counts;
    }


    /**
     * This method summarizes all haplogroups belonging to one of the defined "core" groups.
     * If HG belongs to more than one group, assign it to the "deepest" in the tree.
     * @param hgs
     * @return
     */

    private HashMap<String, ArrayList> reduceHGs(String[] hgs){

        HashMap<String, ArrayList> hgs_summarized = new HashMap<>();
        // number of subHGs
        //"L4-16", "M1-44", "T1-51", "W-59",  "I-62", "X-88", "L1-99", "L0-156", "L2-131", "T2-175",  "K-197" ,  "T-229" ,  "J-239" ,
        //"H-677",  "U-730" , "HV-1132", "R0-1171", "R-2954", "N-3470",  "L3-5010",

        String[] hg_core_list = new String[]{"L4", "M1", "T1", "W", "I", "X",  "L1", "L0", "L2", "T2",
                "K",  "T",  "J",  "H", "U", "HV", "R0",  "R",  "N",  "L3"};

        used_hgs = new ArrayList<>();

        for(String hg_core : hg_core_list){
            List<String> core_subs = treeMap.get(hg_core);
            for(String hg : hgs){
                if(core_subs.contains(hg)){
                    if(!used_hgs.contains(hg)){
                        used_hgs.add(hg);
                        if(hgs_summarized.containsKey(hg_core)){
                            ArrayList tmp = hgs_summarized.get(hg_core);
                            tmp.add(hg);
                            hgs_summarized.put(hg_core, tmp);
                        } else {
                            ArrayList<String> tmp = new ArrayList<>();
                            tmp.add(hg);
                            hgs_summarized.put(hg_core, tmp);
                        }
                    }
                }
            }
        }

        return hgs_summarized;

    }


    /**
     * This method rounds value to two decimal places.
     * @param val
     * @return
     */

    private double roundValue(double val){
        return  (double)Math.round(val * 100d) / 100d;

    }



    /**
     * This method assigns to each group the haplogroups which occurs within this group
     * @return
     */
    public HashMap<String, List<String>> getHG_to_group(ObservableList<ObservableList> selectedTableItems ){



        String[][] cols = prepareColumns(new String[]{"Haplogroup", "Grouping"}, tableController.getSelectedRows());
        String[] seletcion_haplogroups = cols[0];
        String[] seletcion_groups = cols[1];


        // parse selection to tablefilter
        HashMap<String, List<String>> hg_to_group = new HashMap<>();


        if (seletcion_haplogroups.length != 0) {

            // iteration over grouping
            for(int i = 0; i < seletcion_groups.length; i++){
                String group = seletcion_groups[i];

                // create new hash entry for each group
                if(!hg_to_group.containsKey(group)){
                    hg_to_group.put(group, new ArrayList<>());
                }

                // iterate over all table view rows
                for(int k = 0; k < selectedTableItems.size(); k++){
                    ObservableList list = selectedTableItems.get(k);

                    if(list.get( tableController.getColIndex("Grouping")).equals(group)){

                        List<String> tmp = hg_to_group.get(group);
                        tmp.add((String)list.get(tableController.getColIndex("Haplogroup")));
                        hg_to_group.put(group, tmp);

                    }

                }
            }
        }
        getWeights(seletcion_haplogroups, seletcion_groups);
        return hg_to_group;



    }


    /**
     * This method iterates over groups and their corresponding haplogroups and counts the occurrences per haplogroup
     * per group. Counts are later used as weights for sunburst chart
     *
     * @param seletcion_haplogroups unique list of haplogroups
     * @param seletcion_groups  unique list of groups
     */
    public void getWeights(String[] seletcion_haplogroups, String[] seletcion_groups){

        // hash map
        // Group : <HG : count>
        weights = new HashMap<>();
        Set<String> haplogroups = new HashSet<String>(Arrays.asList(seletcion_haplogroups));

        // get weights
        for(int i = 0; i < seletcion_groups.length; i++) {
            String group = seletcion_groups[i];
            if (!weights.containsKey(group)) {
                weights.put(group, new HashMap<String, Double>());
            }
            HashMap<String, Double> hash_tmp = weights.get(group);

            for(String hg : haplogroups){

                // get number of occurrences of this hg within this group
                double count_per_HG = tableController.getCountPerHG(
                        hg,
                        group,
                        tableController.getColIndex("Haplogroup"),
                        tableController.getColIndex("Grouping")
                );

                if(count_per_HG!=0){
                    if (!hash_tmp.containsKey(hg)) {
                        hash_tmp.put(hg, count_per_HG);
                    } else {
                        hash_tmp.put(hg, hash_tmp.get(hg) + 1);
                    }
                }

            }

        }
    }
    /**
     * This method gets all entries of column "Haplogroups" and "Grouping" as set of unique entries.
     *
     * @param names
     * @param selectedTableItems
     * @return
     */
    public String[][] prepareColumns(String[] names, ObservableList<ObservableList> selectedTableItems){


        TableColumn haplo_col = tableController.getTableColumnByName(names[0]);
        TableColumn grouping_col = tableController.getTableColumnByName(names[1]);


        Set<String> columnDataHG = new HashSet<>();
        selectedTableItems.stream().forEach((o)
                -> columnDataHG.add((String)haplo_col.getCellData(o)));

        Set<String> columnDataGroup = new HashSet<>();
        selectedTableItems.stream().forEach((o)
                -> columnDataGroup.add((String)grouping_col.getCellData(o)));

        return new String[][]{columnDataHG.toArray(new String[columnDataHG.size()]),
                columnDataGroup.toArray(new String[columnDataGroup.size()])};
    }


    public HashMap<String, HashMap<String, Double>> getWeights() {
        return weights;
    }
}
