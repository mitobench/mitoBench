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
    private String[] hg_core_list;
    private String[] coreHGs = new String[]{"L4", "M1", "T1", "W", "I", "X",  "L1", "L0", "L2", "T2",
            "K",  "T",  "J",  "H", "U", "HV", "R0",  "R",  "N",  "L3"};



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
            HashMap<String, ArrayList> hgs_summed = summarizeHaolpgroups(selection_haplogroups, coreHGs);

            HashMap<String, List<XYChart.Data<String, Number>>> data_all = new HashMap<String, List<XYChart.Data<String, Number>>>();
            data_all = assignHGs(hgs_summed, selection_haplogroups, selection_groups, numberOfElementsPerCaregory);


            for(String key : hg_core_list){
                if(data_all.containsKey(key)) {
                    for(int i = 0; i < selection_groups.length; i++){
                        data_all.get(key).get(i).setYValue(roundValue((data_all.get(key).get(i).getYValue().doubleValue() / numberOfElementsPerCaregory[i]) * 100));
                    }
                    stackedBar.addSeries(data_all.get(key), key);

                }
            }

            if(data_all.containsKey("Others"))
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
                        if(selection_haplogroups[i].contains("+")){
                            System.out.print("");
                        }

                        double count_per_HG = tableController.getCountPerHG(selection_haplogroups[i], selection_groups[j], tableController.getColIndex("Haplogroup"),
                                tableController.getColIndex("Grouping"));

                        XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(selection_groups[j], count_per_HG);
                        data_list.add(data);
                    }
                    stackedBar.addSeries(data_list, selection_haplogroups[i]);
                }
            }
        }
    }


    /**
     * This method iterates over haplogroup columns with unique entries and counts each haplogroup per group.
     * It also considers haplogroups with additional mutations (+...) and assign them to the haplogroup mentioned
     * before the plus. Unused haplogroups are collected as "Others".
     *
     * @param hgs_summed
     * @param selection_haplogroups
     * @param selection_groups
     * @param numberOfElementsPerCaregory
     * @return
     */
    public HashMap<String, List<XYChart.Data<String, Number>>> assignHGs(HashMap<String, ArrayList> hgs_summed,
                                                                         String[] selection_haplogroups,
                                                                         String[] selection_groups,
                                                                         int[] numberOfElementsPerCaregory){
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

                XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(group, roundValue(count));
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

        // filter unused for "+" HGs
        List<String> unused_hgs_tmp = new ArrayList<String>();
        for (String hg : unused_hgs) {
            List<XYChart.Data<String, Number>> data_list = new ArrayList<XYChart.Data<String, Number>>();
            for (int i = 0; i < selection_groups.length; i++) {
                String group = selection_groups[i];
                if(hg.contains("+")){
                    double count = tableController.getCountPerHG(hg,
                                                                 group,
                                                                 tableController.getColIndex("Haplogroup"),
                                                                 tableController.getColIndex("Grouping"));
                    XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(group, roundValue(count));
                    data_list.add(data);

                }
            }

            String hg_old = hg;
            hg = hg.split("\\+")[0];
            if(hg.equals("L2'3'4'6")){
                hg = "L2";
            }
            // get coreHG of this HG
            String keyHG = hg;
            for(String cHG : hg_core_list){
                if(treeMap.get(cHG).contains(hg)){
                    keyHG = cHG;
                    unused_hgs_tmp.add(hg_old);
                    break;
                }
            }

            if (data_all.containsKey(keyHG)){

                for(int i = 0; i < data_all.get(keyHG).size(); i++){
                    for(int j = 0; j < data_list.size(); j++) {
                        if (data_list.get(j).getXValue().equals(data_all.get(keyHG).get(i).getXValue())) {
                            data_all.get(keyHG).get(i).setYValue(data_all.get(keyHG).get(i).getYValue().intValue()
                                                               + data_list.get(j).getYValue().intValue());
                        }
                    }
                }
            }
        }


        // remove all "newly" used HGs
        unused_hgs.removeAll(unused_hgs_tmp);

        // get all so far unused haplogroups to assign them to "others"
        List<XYChart.Data<String, Number>> data_list = new ArrayList<XYChart.Data<String, Number>>();
        for (int i = 0; i < selection_groups.length; i++) {
            String group = selection_groups[i];
            double count_others = 0.0;

            for (String hg : unused_hgs) {
                count_others += tableController.getCountPerHG(hg, group, tableController.getColIndex("Haplogroup"), tableController.getColIndex("Grouping"));
            }
            XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(group, count_others);
            data_list.add(data);

        }
        data_all.put("Others", data_list);

        return data_all;

    }

    /**
     * This method counts the number of items assigned to each category.
     * @param categories
     * @return
     */

    public int[] getNumberOfElementsPerCategory(String[] categories){
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

    public HashMap<String, ArrayList> summarizeHaolpgroups(String[] hgs, String[] coreHGs_variable){

        HashMap<String, ArrayList> hgs_summarized = new HashMap<>();
        hg_core_list = generateHG_core_list(coreHGs_variable);
        used_hgs = new ArrayList<>();

        for(String hg_core : hg_core_list){
            List<String> core_subs = treeMap.get(hg_core);
            for(String hg : hgs){
                if(core_subs.contains(hg)) {
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
     * This method creates based on a list of haplogroups the order they occur in the haplotree (from leaves to root).
     *
     * @param coreHGs
     * @return
     */
    public String[] generateHG_core_list(String[] coreHGs){
        List<Integer> sizes = new ArrayList<>();
        HashMap<Integer, List<String>> count_to_hg = new HashMap<>();
        for(String key : coreHGs){
            if(treeMap.containsKey(key.trim())){
                int count = treeMap.get(key).size();
                sizes.add(count);
                if(count_to_hg.containsKey(count)){
                    List tmp = count_to_hg.get(count);
                    tmp.add(key);
                    count_to_hg.put(count, tmp);
                } else {
                    count_to_hg.put(count,  Arrays.asList(key));
                }
            }

        }
        Collections.sort(sizes);

        String[] hgs_sorted = new String[coreHGs.length];
        for(int i = 0; i < sizes.size(); i++){
            hgs_sorted[i] = count_to_hg.get(sizes.get(i)).get(0);
            if(count_to_hg.get(sizes.get(i)).size() > 1){
                for(int j = 1; j < count_to_hg.get(sizes.get(i)).size(); j++){
                    hgs_sorted[i+j] = count_to_hg.get(sizes.get(i)).get(j);
                    i += j;

                }
            }
        }

        return hgs_sorted;

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

    public String[] getCoreHGs() {
        return coreHGs;
    }
}
