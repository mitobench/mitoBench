package view.menus;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.charts.BarPlotHaplo;
import view.charts.StackedBar;
import view.charts.SunburstChart;
import view.table.TableController;
import view.tree.TreeHaploController;

import java.util.*;


/**
 * Created by neukamm on 23.11.16.
 */
public class GraphicsMenu {


    private Menu menuGraphics;
    private TableController tableController;
    private BarPlotHaplo barPlotHaplo;
    private StackedBar stackedBar;
    private SunburstChart sunburstChart;
    private HashMap<String, HashMap<String, Integer>> weights;
    private TabPane tabPane;
    private HashMap<String, List<String>> treeMap;
    private HashMap<String, List<String>> treeMap_path_to_root;
    private TreeItem<String> tree_root;
    private TreeView treeView;
    private Stage stage;


    public GraphicsMenu(TableController tableController, TabPane vBox, TreeHaploController treeController, Stage stage){
        menuGraphics = new Menu("Graphics");
        this.tableController = tableController;
        tabPane = vBox;
        treeMap = treeController.getTreeMap();
        treeMap_path_to_root = treeController.getTreeMap_leaf_to_root();
        tree_root = treeController.deepcopy(treeController.getTree().getTree().getRoot());
        treeView = treeController.getTree().getTree();
        this.stage = stage;
        addSubMenus();
    }

    private void initBarchart(){
        this.barPlotHaplo = new BarPlotHaplo("Haplogroup frequency", "Frequency", tabPane, stage);
        Tab tab = new Tab();
        tab.setText("Bar Chart");
        tab.setContent(barPlotHaplo.getBarChart());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

    }

    private void initStackedBarchart(){
        this.stackedBar = new StackedBar("Haplogroup frequency per group", tabPane, stage);
        Tab tab = new Tab();
        tab.setText("Bar Chart per group");
        tab.setContent(stackedBar.getSbc());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

    }

    private void initSunburst(){
        sunburstChart = new SunburstChart(new BorderPane(), stage, tabPane);
        Tab tab = new Tab();
        tab.setText("Sunburst Chart");
        tab.setContent(sunburstChart.getBorderPane());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);


    }

    public void clearCharts(){
        stackedBar = null;
        barPlotHaplo = null;
        tabPane.getTabs().clear();
    }


    private void addSubMenus() {

        Menu barchart = new Menu("Barchart");


        /*
                        Plot HG frequency

         */

        MenuItem plotHGfreq = new MenuItem("Plot haplogroup frequency");
        plotHGfreq.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {

                    initBarchart();

                    TableColumn haplo_col = tableController.getTableColumnByName("Haplogroup");
                    List<String> columnData = new ArrayList<>();
                    for (Object item : tableController.getTable().getItems()) {
                        columnData.add((String)haplo_col.getCellObservableValue(item).getValue());
                    }
                    String[] seletcion_haplogroups = columnData.toArray(new String[columnData.size()]);


                    // parse selection to tablefilter
                    //TableSelectionFilter tableFilter = new TableSelectionFilter();

                    barPlotHaplo.clearData();

                    if (seletcion_haplogroups.length !=0) {
                        //tableFilter.haplogroupFilter(tableController, seletcion_haplogroups, tableController.getColIndex("Haplogroup"));
                        barPlotHaplo.addData(tableController.getDataHist());

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /*

                    Plot Hg frequency for each group

         */

        MenuItem plotHGfreqGroup = new MenuItem("Plot haplogroup frequency per group(StackedBarchart)");
        plotHGfreqGroup.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    if(tableController.getTableColumnByName("Grouping") != null){
                        initStackedBarchart();

                        String[][] cols = prepareColumns(new String[]{"Haplogroup", "Grouping"}, tableController.getSelectedRows());
                        String[] seletcion_haplogroups = cols[0];
                        String[] seletcion_groups = cols[1];

                        stackedBar.clearData();
                        stackedBar.setCategories(seletcion_groups);

                        // consider Hgs only once per group
                        //Set<String> haplogroups = new HashSet<String>(Arrays.asList(seletcion_haplogroups));

                        if (seletcion_haplogroups.length != 0) {
                            for(int i = 0; i < seletcion_haplogroups.length; i++){

                                List< XYChart.Data<String, Number> > data_list = new ArrayList<XYChart.Data<String, Number>>();
                                // fill data_list : <group(i), countHG >
                                for(int j = 0; j < seletcion_groups.length; j++){

                                    int count_per_HG = tableController.getCountPerHG(seletcion_haplogroups[i], seletcion_groups[j], tableController.getColIndex("Haplogroup"),
                                            tableController.getColIndex("Grouping"));
                                    XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(seletcion_groups[j], count_per_HG);
                                    data_list.add(data);
                                }
                                stackedBar.addSeries(data_list, seletcion_haplogroups[i]);
                            }
                        }
                        stackedBar.getSbc().getData().addAll(stackedBar.getSeriesList());
                        stackedBar.addTooltip(t);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



         /*

                    Plot HG frequency for each group

         */

        MenuItem sunburstChartItem = new MenuItem("Create Sunburst chart");
        sunburstChartItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {

                    // makes only sense if grouping exists.
                    if(tableController.getTableColumnByName("Grouping")!=null){
                        initSunburst();
                        // get selected rows
                        ObservableList<ObservableList> selectedTableItems = tableController.getSelectedRows();
                        HashMap<String, List<String>> hg_to_group = getHG_to_group(selectedTableItems);

                        sunburstChart.create(hg_to_group, weights, treeMap_path_to_root, tree_root, treeView);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

         /*

                    Plot HG frequency for each group

         */

        MenuItem clearPlotBox = new MenuItem("Clear barcharts");
        clearPlotBox.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    clearCharts();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        barchart.getItems().addAll(plotHGfreq, plotHGfreqGroup);

        menuGraphics.getItems().addAll(barchart, sunburstChartItem, new SeparatorMenuItem(), clearPlotBox);
    }

    public Menu getMenuGraphics() {
        return menuGraphics;
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
     * per group. Counts are later used as weights for sunburst
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
                weights.put(group, new HashMap<String, Integer>());
            }
            HashMap<String, Integer> hash_tmp = weights.get(group);

            for(String hg : haplogroups){

                // get number of occurrences of this hg within this group
                int count_per_HG = tableController.getCountPerHG(
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

    public HashMap<String, HashMap<String, Integer>> getWeights() {
        return weights;
    }
}
