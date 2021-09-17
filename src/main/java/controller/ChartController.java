package controller;

import io.Exceptions.HaplogroupException;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import org.jetbrains.annotations.NotNull;
import view.dialogues.error.HaplogroupErrorDialogue;
import view.visualizations.BarChartGrouping;
import view.visualizations.BarPlotHaplo;
import view.visualizations.BarPlotHaplo2;
import view.visualizations.StackedBar;

import java.net.MalformedURLException;
import java.util.*;

/**
 * Created by neukamm on 19.12.16.
 */
public class ChartController {

    private ATableController tableController;
    private List<String> used_hgs;
    private HashMap<String, List<String>> treeMap;
    private List<String> hg_core_list;
    private String[] customHGList;
    private String[] groupOrder;


    public ChartController() {

    }

    public void init(ATableController tableController, HashMap<String, List<String>> treeMap){
        this.tableController = tableController;
        this.treeMap = treeMap;
    }


    /**
     *
     * @param barPlot
     * @param column
     */
    public void addDataBarChart(BarPlotHaplo barPlot, TableColumn column, List<String> column_data) {

        if(column_data == null){
            column_data = new ArrayList<>();
            for (Object item : tableController.getTable().getItems()) {
                column_data.add((String)column.getCellObservableValue(item).getValue());
            }
        }

        String[] selected_data = column_data.toArray(new String[column_data.size()]);
        barPlot.clearData();

        if (selected_data.length != 0) {
            barPlot.addData(tableController.getDataHist(selected_data));
        }
    }


    /**
     *
     * @param barPlot
     * @param column
     */
    public void addDataBarChart(BarChartGrouping barPlot, TableColumn column, List<String> column_data) {

        if(column_data == null){
            column_data = new ArrayList<>();
            for (Object item : tableController.getTable().getItems()) {
                column_data.add((String)column.getCellObservableValue(item).getValue());
            }
        }


        String[] selected_data = column_data.toArray(new String[column_data.size()]);
        barPlot.clearData();

        if (selected_data.length !=0) {
            barPlot.addData(tableController.getDataHist(selected_data));
        }
    }

    /**
     *
     * @param barPlot
     * @param column
     */
    public void addDataBarChart(BarPlotHaplo2 barPlot, TableColumn column, List<String> column_data) throws MalformedURLException {

        if(column_data == null){
            column_data = new ArrayList<>();
            for (Object item : tableController.getTable().getItems()) {
                column_data.add((String)column.getCellObservableValue(item).getValue());
            }
        }


        String[] selected_data = column_data.toArray(new String[column_data.size()]);
        barPlot.clearData();

        if (selected_data.length !=0) {
            barPlot.addData(tableController.getDataHist2(selected_data));
        }
    }


    /**
     * This method adds all data to the stacked bar chart.
     *  @param stackedBar
     * @param selection_haplogroups
     * @param selection_groups
     * @param hgs
     */

    public void addDataStackedBarChart(StackedBar stackedBar, String[] selection_haplogroups, @NotNull String[] selection_groups, String[] hgs) {

        // get number of elements per group
        int[] numberOfElementsPerCaregory;
        if(selection_groups.length==1){
            numberOfElementsPerCaregory=new int[]{selection_haplogroups.length};
        } else {
            numberOfElementsPerCaregory = getNumberOfElementsPerCategory(selection_groups);
        }

        stackedBar.clearData();
        stackedBar.setCategories(selection_groups);
        HashMap<String, ArrayList> hgs_summed = summarizeHaplogroups(Arrays.asList(selection_haplogroups), hgs);

        HashMap<String, List<XYChart.Data<String, Number>>> data_all;
        if(numberOfElementsPerCaregory.length==1){
            data_all = assignHGsNoGrouping(hgs_summed, Arrays.asList(selection_haplogroups));
        } else {
            data_all = assignHGs(hgs_summed, selection_haplogroups, selection_groups);
        }

        int norm = tableController.getSelectedRows().size();

        // sort 'hg_core_list' according 'customHGList'
        List<String> tmp_hg_core_list = new ArrayList<>();
        for(String hg : customHGList){
            for(String k : hg_core_list){
                if(hg.equals(k)){
                    tmp_hg_core_list.add(k);
                }
            }
        }
        hg_core_list = tmp_hg_core_list;

        for(String key : hg_core_list){
            if(data_all.containsKey(key)) {
                if(selection_groups.length==1){
                    data_all.get(key).get(0).setYValue(roundValue(
                            (data_all.get(key).get(0).getYValue().doubleValue() / (double)norm) * 100));
                } else {
                    for(int i = 0; i < selection_groups.length; i++){
                        data_all.get(key).get(i).setYValue(roundValue(
                                (data_all.get(key).get(i).getYValue().doubleValue() / (double)numberOfElementsPerCaregory[i]) * 100));
                    }
                }

                stackedBar.addSeries(data_all.get(key), key);

            }
        }

        if(data_all.containsKey("Others")){

            if(selection_groups.length==1){
                data_all.get("Others").get(0).setYValue(roundValue(
                        (data_all.get("Others").get(0).getYValue().doubleValue() / (double)norm) * 100));

            } else {

                for(int i = 0; i < selection_groups.length; i++){
                    data_all.get("Others").get(i).setYValue(roundValue(
                            (data_all.get("Others").get(i).getYValue().doubleValue() / (double)numberOfElementsPerCaregory[i]) * 100));
                }

            }

            stackedBar.addSeries(data_all.get("Others"), "Others");

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
     * @return
     */
    public HashMap<String, List<XYChart.Data<String, Number>>> assignHGs(HashMap<String, ArrayList> hgs_summed,
                                                                         String[] selection_haplogroups,
                                                                         String[] selection_groups) {

        // todo: improve efficiency!

        groupOrder = new String[selection_groups.length];
        groupOrder = selection_groups.clone();

        HashMap<String, List<XYChart.Data<String, Number>>> data_all = new HashMap<>();

        if(hg_core_list.size()>0){

            for(String hg_core : hg_core_list){
                System.out.println("\t\tGetting hgs for " + hg_core);
                if(hgs_summed.containsKey(hg_core)){
                    for (String key : hgs_summed.keySet()) {

                        ArrayList<String> subHGs = hgs_summed.get(key);
                        List<XYChart.Data<String, Number>> data_list = new ArrayList<>();
                        for (int i = 0; i < selection_groups.length; i++) {
                            String group = selection_groups[i];

                            if(!group.equals("")){
                                double count = 0.0;
                                for (String hg : subHGs) {

                                    List<String> hgs = tableController.getCountPerHG(
                                            hg,
                                            group,
                                            tableController.getColIndex("Haplogroup"),
                                            tableController.getColIndex("Grouping"));
                                    count += hgs.size();
                                }
                                XYChart.Data<String, Number> data = new XYChart.Data<>(group, roundValue(count));
                                data_list.add(data);
                            }
                        }
                        data_all.put(key, data_list);
                    }
                } else {
                    List<XYChart.Data<String, Number>> data_list = new ArrayList<>();
                    for (int i = 0; i < selection_groups.length; i++) {
                        String group = selection_groups[i];
                        if(!group.equals("")){
                            XYChart.Data<String, Number> data = new XYChart.Data<>(group, 0.0);
                            data_list.add(data);
                        }
                    }
                    data_all.put(hg_core, data_list);
                }
            }

            // iterate over used hgs and check if there are some unused hgs
            List<String> unused_hgs = new ArrayList<>();
            for (String hg : selection_haplogroups) {
                if (!used_hgs.contains(hg)) {
                    unused_hgs.add(hg);
                }
            }

            // filter unused for "+" HGs
            List<String> unused_hgs_tmp = new ArrayList<>();
            for (String hg : unused_hgs) {
                List<XYChart.Data<String, Number>> data_list = new ArrayList<>();
                for (int i = 0; i < selection_groups.length; i++) {
                    String group = selection_groups[i];
                    if(hg.contains("+")){
                        int count = tableController.getCountPerHG(hg,
                                group,
                                tableController.getColIndex("Haplogroup"),
                                tableController.getColIndex("Grouping")).size();
                        XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(group, roundValue(count));
                        data_list.add(data);

                    }
                }

                String hg_old = hg;
                hg = hg.split("\\+")[0];

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
            List<XYChart.Data<String, Number>> data_list = new ArrayList<>();
            for (int i = 0; i < selection_groups.length; i++) {
                String group = selection_groups[i];
                double count_others = 0.0;
                if(!group.equals("")){
                    for (String hg : unused_hgs) {
                        count_others += tableController.getCountPerHG(hg, group, tableController.getColIndex("Haplogroup"),
                                tableController.getColIndex("Grouping")).size();
                    }
                    XYChart.Data<String, Number> data = new XYChart.Data<>(group, count_others);
                    data_list.add(data);
                }
            }
            data_all.put("Others", data_list);

            return data_all;
        } else {
            try {
                throw new Exception("hg core list is empty");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This method counts the number of items assigned to each category.
     * @param categories
     * @return
     */

    public int[] getNumberOfElementsPerCategory(String[] categories){
        int[] counts = new int[categories.length];
        Arrays.fill(counts, 0);
        ObservableList<ObservableList> selectedTableItems = tableController.getTable().getSelectionModel().getSelectedItems();
        if(selectedTableItems.size()==0){
            selectedTableItems = tableController.getTable().getItems();
        }
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

    public HashMap<String, ArrayList> summarizeHaplogroups(List<String> hgs, String[] coreHGs_variable){

        HashMap<String, ArrayList> hgs_summarized = new HashMap<>();
        hg_core_list = generateHG_core_list(coreHGs_variable);
        used_hgs = new ArrayList<>();

        for(String hg_core : hg_core_list){
            List<String> core_subs;
            hg_core = hg_core.trim();
            if(hg_core.equals("L")){
                core_subs = treeMap.get("L0");
                core_subs.addAll(treeMap.get("L1"));
                core_subs.addAll(treeMap.get("L2"));
                core_subs.addAll(treeMap.get("L3"));
                core_subs.addAll(treeMap.get("L4"));
                core_subs.addAll(treeMap.get("L5"));
                core_subs.addAll(treeMap.get("L6"));
            } else if (hg_core.equals("B")){
                core_subs = treeMap.get("B2");
                core_subs.addAll(treeMap.get("B4"));
                core_subs.addAll(treeMap.get("B5"));
                core_subs.addAll(treeMap.get("B6"));
            } else {
                core_subs = treeMap.get(hg_core);
            }


            for(String hg : hgs){

                if(hg.contains("+")){
                    hg = hg.split("\\+")[0];
                }

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

    public double roundValue(double val){
        return  (double)Math.round(val * 100d) / 100d;

    }


    /**
     * This method creates based on a list of haplogroups the order they occur in the haplotree (from leaves to root).
     *
     * @param coreHGs
     * @return
     */
    public List<String> generateHG_core_list(String[] coreHGs){
        List<Integer> sizes = new ArrayList<>();
        HashMap<Integer, List<String>> count_to_hg = new HashMap<>();
        for(String key : coreHGs){
            if(key.equals("L")){
                List<String> grL0 = treeMap.get("L0");
                List<String> grL1 = treeMap.get("L1");
                List<String> grL2 = treeMap.get("L2");
                List<String> grL3 = treeMap.get("L3");
                List<String> grL4 = treeMap.get("L4");
                List<String> grL5 = treeMap.get("L5");
                List<String> grL6 = treeMap.get("L5");

                int count = grL0.size() + grL1.size() + grL2.size() + grL3.size() + grL4.size() + grL5.size() + grL6.size();

                sizes.add(count);
                if(count_to_hg.containsKey(count)){
                    List<String> tmp = new ArrayList<>(count_to_hg.get(count));
                    tmp.add(key);
                    count_to_hg.put(count, tmp);
                } else {
                    count_to_hg.put(count,  Arrays.asList(key));
                }

            } else if(key.equals("B")){
                List<String> grB2 = treeMap.get("B2");
                List<String> grB4 = treeMap.get("B4");
                List<String> grB5 = treeMap.get("B5");
                List<String> grB6 = treeMap.get("B6");

                int count = grB2.size() + grB4.size() +grB5.size() +grB6.size();

                sizes.add(count);
                if(count_to_hg.containsKey(count)){
                    List<String> tmp = new ArrayList<>(count_to_hg.get(count));
                    tmp.add(key);
                    count_to_hg.put(count, tmp);
                } else {
                    count_to_hg.put(count,  Arrays.asList(key));
                }
            } else
            if(treeMap.containsKey(key)){
                int count = treeMap.get(key).size();
                sizes.add(count);
                if(count_to_hg.containsKey(count)){
                    List<String> tmp = new ArrayList<>(count_to_hg.get(count));
                    tmp.add(key);
                    count_to_hg.put(count, tmp);
                } else {
                    count_to_hg.put(count,  Arrays.asList(key));
                }
            }
        }
        Collections.sort(sizes);

        List<String> hgs_sorted = new ArrayList<>();
        for(int i = 0; i < sizes.size(); i++){
            hgs_sorted.add(i, count_to_hg.get(sizes.get(i)).get(0).trim());
            if(count_to_hg.get(sizes.get(i)).size() > 1){
                for(int j = 1; j < count_to_hg.get(sizes.get(i)).size(); j++){
                    hgs_sorted.add((i+j),  count_to_hg.get(sizes.get(i)).get(j).trim());
                    i += j;

                }
            }
        }

        return hgs_sorted;

    }



    /**
     * This method gets all entries of column "Haplogroups" and "Grouping" as set of unique entries.
     *
     * @param names
     * @param selectedTableItems
     * @return
     */
    public String[][] prepareColumns(String[] names, ObservableList<ObservableList> selectedTableItems) {

        List tmp = new ArrayList<>(Arrays.asList(names));
        if(tmp.contains("Haplogroup") && !containsHGCol(tableController.getCurrentColumnNames())){

            HaplogroupException haplogroupException = new HaplogroupException("No haplogroups defined!");
            HaplogroupErrorDialogue haplogroupErrorDialogue = new HaplogroupErrorDialogue(haplogroupException);

        } else {
            String[][] res = new String[names.length][];
            for(int i = 0; i < names.length; i++){
                TableColumn col = tableController.getTableColumnByName(names[i]);

                Set<String> columnData = new HashSet<>();
                selectedTableItems.stream().forEach((o)
                        -> columnData.add((String)col.getCellData(o)));

                res[i] = columnData.toArray(new String[columnData.size()]);
            }

            return res;

        }
        return null;

    }


    /**
     * This method gets all entries of column "Haplogroups" and "Grouping" as set of unique entries.
     *
     * @param names
     * @param selectedTableItems
     * @return
     */
    public String[][] prepareColumnsAsList(String[] names, ObservableList<ObservableList> selectedTableItems) {

        List tmp = new ArrayList<>(Arrays.asList(names));
        if(tmp.contains("Haplogroup") && !containsHGCol(tableController.getCurrentColumnNames())){

            HaplogroupException haplogroupException = new HaplogroupException("No haplogroups defined!");
            new HaplogroupErrorDialogue(haplogroupException);

        } else {
            String[][] res = new String[names.length][];
            for(int i = 0; i < names.length; i++){
                TableColumn col = tableController.getTableColumnByName(names[i]);

                List<String> columnData = new ArrayList<>();
                selectedTableItems.stream().forEach((o)
                        -> columnData.add((String)col.getCellData(o)));

                res[i] = columnData.toArray(new String[columnData.size()]);
            }

            return res;

        }
        return null;

    }


    private boolean containsHGCol(List<String> currentColumnNames){
        for (String s : currentColumnNames){
            if(s.contains("Haplogroup")){
                return true;
            }
        }

        return false;

    }

    public HashMap<String, List<XYChart.Data<String, Number>>> assignHGsNoGrouping(HashMap<String, ArrayList> hgs_summed, List<String> selection_haplogroups) {

        HashMap<String, List<XYChart.Data<String, Number>>> data_all = new HashMap<>();

        if(hg_core_list.size() > 0){

            for(String hg_core : hg_core_list){

                if(hgs_summed.keySet().contains(hg_core)){
                    for (String key : hgs_summed.keySet()) {

                        ArrayList<String> subHGs = hgs_summed.get(key);
                        List<XYChart.Data<String, Number>> data_list = new ArrayList<>();
                        String group = "All data";

                        double count = 0.0;
                        for (String hg : subHGs) {
                            List<String> hgs = tableController.getCountPerHG(hg, group, tableController.getColIndex("Haplogroup"), -1);
                            count += hgs.size();
                        }

                        XYChart.Data<String, Number> data = new XYChart.Data<>(group, roundValue(count));
                        data_list.add(data);

                        data_all.put(key, data_list);
                    }
                } else {
                    List<XYChart.Data<String, Number>> data_list = new ArrayList<>();
                    String group = "All data";
                    XYChart.Data<String, Number> data = new XYChart.Data<>(group, 0.0);
                    data_list.add(data);
                    data_all.put(hg_core, data_list);
                }
            }


            // iterate over used hgs and check if there are some unused hgs
            List<String> unused_hgs = new ArrayList<String>();
            //for (String hg : tableController.getColumnData(tableController.getTableColumnByName("Haplogroup"))) {
            for (String hg : selection_haplogroups) {
                if(hg.contains("+")){
                    hg = hg.split("\\+")[0];
                }
                if (!used_hgs.contains(hg)) {
                    unused_hgs.add(hg);
                }
            }


//            List<String> unused_hgs_now_used = new ArrayList<>();
//            for (String hg : unused_hgs) {
//                //check beginning of HG, assign according this
//                List<String> list_hg_keyset = new ArrayList<>(data_all.keySet());
//
//                Collections.sort(list_hg_keyset, Comparator.comparing(String::length));
//
//                for(int i = list_hg_keyset.size()-1; i >= 0; i--){
//                    String hg_key = list_hg_keyset.get(i);
//                    if (hg.startsWith(hg_key)){
//                        int number_of_hgs_in_hg_key = data_all.get(hg_key).get(0).getYValue().intValue();
//                        data_all.get(hg_key).get(0).setYValue(number_of_hgs_in_hg_key + 1);
//                        unused_hgs_now_used.add(hg);
//                    }
//                }
//
//            }
//            for(String usedhg : used_hgs)
//                System.out.println(usedhg);
//
//            for(String usedhg : unused_hgs_now_used)
//                System.out.println(usedhg);

            List<XYChart.Data<String, Number>> data_list = new ArrayList<>();
           // unused_hgs.removeAll(unused_hgs_now_used);

            XYChart.Data<String, Number> data = new XYChart.Data<>("All data", unused_hgs.size());
            data_list.add(data);
            data_all.put("Others", data_list);

            return data_all;
        } else {
            try {
                throw new Exception("hg core list is empty");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;

    }

    public String[] getGroupOrder() {
        return groupOrder;
    }

    public String[] getCustomHGList() {
        return customHGList;
    }

    public void setCustomHGList(String[] customHGList) {
        this.customHGList = customHGList;
    }
}