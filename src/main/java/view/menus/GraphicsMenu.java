package view.menus;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.charts.*;
import view.table.TableControllerUserBench;
import view.tree.TreeHaploController;

import java.util.HashMap;
import java.util.List;


/**
 * Created by neukamm on 23.11.16.
 */
public class GraphicsMenu {


    private Menu menuGraphics;
    private TableControllerUserBench tableController;
    private BarPlotHaplo barPlotHaplo;
    private BarChartGrouping barChartGrouping;
    private StackedBar stackedBar;
    private SunburstChartCreator sunburstChart;
    private TabPane tabPane;
    private HashMap<String, List<String>> treeMap_path_to_root;
    private TreeItem<String> tree_root;
    private TreeView treeView;
    private Stage stage;
    private ChartController chartController;
    private TreeHaploController treeController;
    private ProfilePlot profilePlot;
    private Scene scene;
    private TabPane statsTabpane;


    public GraphicsMenu(TableControllerUserBench tableController, TabPane vBox, TreeHaploController treeController, Stage stage,
                        Scene scene, TabPane statsTabpane){

        menuGraphics = new Menu("Graphics");
        menuGraphics.setId("graphicsMenu");
        this.treeController = treeController;
        this.tableController = tableController;
        tabPane = vBox;
        treeMap_path_to_root = treeController.getTreeMap_leaf_to_root();
        tree_root = treeController.deepcopy(treeController.getTree().getTree().getRoot());
        treeView = treeController.getTree().getTree();
        this.stage = stage;
        this.chartController = new ChartController();
        chartController.init(tableController, treeController.getTreeMap());
        this.scene = scene;
        this.statsTabpane = statsTabpane;
        addSubMenus();
    }


    public void initHaploBarchart(){
        this.barPlotHaplo = new BarPlotHaplo("Haplogroup frequency", "Frequency", tabPane, stage);
        Tab tab = new Tab();
        tab.setId("tab_haplo_barchart");
        tab.setText("Bar Chart haplogroups");
        tab.setContent(barPlotHaplo.getBarChart());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

    }

    public void initGroupBarChart(){
        barChartGrouping = new BarChartGrouping("Group frequency", "Frequency", tabPane, stage);
        Tab tab = new Tab();
        tab.setId("tab_group_barchart");
        tab.setText("Bar Chart Grouping");
        tab.setContent(barChartGrouping.getBarChart());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }


    public void initStackedBarchart(){
        this.stackedBar = new StackedBar("Haplogroup frequency per group", tabPane, this);
        Tab tab = new Tab();
        tab.setId("tab_stacked_bar_chart");
        tab.setText("Bar Chart per group");
        tab.setContent(stackedBar.getSbc());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

    }

    private void initSunburst(){
        sunburstChart = new SunburstChartCreator(new BorderPane(), stage, tabPane);
        Tab tab = new Tab();
        tab.setId("tab_sunburst");
        tab.setText("Sunburst Chart");
        tab.setContent(sunburstChart.getBorderPane());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);


    }


    private void initProfilePlot(){
        profilePlot = new ProfilePlot("Profile Plot", "Haplogroup", "Count");
        Tab tab = new Tab();
        tab.setId("tab_profilePlot");
        tab.setText("Profile Plot");
        tab.setContent(profilePlot.getPlot());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

    }


    public void clearCharts(){
        stackedBar = null;
        barPlotHaplo = null;
        tabPane.getTabs().clear();
    }


    private void addSubMenus() {

        Menu haplo_graphics = new Menu("Haplogroups");
        haplo_graphics.setId("haplo_graphics");
        Menu barchart = new Menu("Create Barchart...");
        barchart.setId("barchart");
        Menu grouping_graphics = new Menu("Grouping");
        grouping_graphics.setId("grouping_graphics");

        /*
                        Plot HG frequency

         */

        MenuItem plotHGfreq = new MenuItem("Plot haplogroup frequency");
        plotHGfreq.setId("plotHGfreq_item");
        plotHGfreq.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {

                    if(tableController.getTable().getItems().size() != 0 ){
                        TableColumn haplo_col = tableController.getTableColumnByName("Haplogroup");

                        if(haplo_col!=null){
                            initHaploBarchart();
                            createHaploBarchart(haplo_col, "", null);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /*

                    Plot Hg frequency for each group

         */

        MenuItem plotHGfreqGroup = new MenuItem("Plot haplogroup frequency per group (Stacked Barchart)");
        plotHGfreqGroup.setId("plotHGfreqGroup_item");
        plotHGfreqGroup.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    if(tableController.getTableColumnByName("Grouping") != null
                            && tableController.getTable().getItems().size()!=0) {

                        initStackedBarchart();
                        String[][] cols = chartController.prepareColumns(new String[]{"Haplogroup", "Grouping"}, tableController.getSelectedRows());
                        String[] selection_haplogroups = cols[0];
                        String[] selection_groups = cols[1];

                        chartController.addDataStackedBarChart(stackedBar, selection_haplogroups, selection_groups);
                        stackedBar.getSbc().getData().addAll(stackedBar.getSeriesList());

                        // add settings
                        stackedBar.addListener();
                        stackedBar.addTooltip();

                        ColorSchemeStackedBarChart colorScheme = new ColorSchemeStackedBarChart(stage);
                        colorScheme.setNewColors(stackedBar);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



         /*

                    Plot HG frequency for each group

         */

        MenuItem sunburstChartItem = new MenuItem("Create Sunburst chart...");
        sunburstChartItem.setId("sunburstChart_item");
        sunburstChartItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {

                    // makes only sense if grouping exists.
                    if(tableController.getTableColumnByName("Grouping") != null
                            && tableController.getTable().getItems().size() != 0 ){
                        initSunburst();
                        // get selected rows
                        ObservableList<ObservableList> selectedTableItems = tableController.getSelectedRows();
                        HashMap<String, List<String>> hg_to_group = chartController.getHG_to_group(selectedTableItems);
                        sunburstChart.create(hg_to_group, chartController.getWeights(), treeMap_path_to_root, tree_root, treeView);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



         /*

                    Create profile plot

         */

        MenuItem profilePlotItem = new MenuItem("Create Profile Plot");
        profilePlotItem.setId("profilePlot");
        profilePlotItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    // makes only sense if grouping exists.
                    if(tableController.getTableColumnByName("Grouping") != null
                            && tableController.getTable().getItems().size() != 0 ){
                        initProfilePlot();
                        // get selected rows
                        ObservableList<ObservableList> selectedTableItems = tableController.getSelectedRows();
                        HashMap<String, List<String>> hg_to_group = chartController.getHG_to_group(selectedTableItems);

                        profilePlot.create(tableController, chartController, treeController, statsTabpane, scene);



                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });




         /*

                    Plot HG frequency for each group

         */

        MenuItem clearPlotBox = new MenuItem("Clear Charts");
        clearPlotBox.setId("clear_plots");
        clearPlotBox.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    clearCharts();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        MenuItem grouping_barchart = new MenuItem("Grouping bar chart");
        grouping_barchart.setId("grouping_barchart_item");
        grouping_barchart.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    if(tableController.getTable().getItems().size() != 0 ) {

                        TableColumn haplo_col = tableController.getTableColumnByName("Grouping");
                        if(haplo_col != null){
                            initGroupBarChart();
                            chartController.addDataBarChart(barChartGrouping, haplo_col, "", null);
                            barChartGrouping.setColor(stage);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        // add menu items
        grouping_graphics.getItems().add(grouping_barchart);
        barchart.getItems().addAll(plotHGfreq, plotHGfreqGroup);
        haplo_graphics.getItems().addAll(barchart, sunburstChartItem, profilePlotItem);


        menuGraphics.getItems().addAll(haplo_graphics, grouping_graphics, new SeparatorMenuItem(), clearPlotBox);
    }

    public void createHaploBarchart(TableColumn haplo_col, String filter, TableColumn col2){
        chartController.addDataBarChart(barPlotHaplo, haplo_col, filter, col2);
    }


    public Menu getMenuGraphics() {
        return menuGraphics;
    }
    public TableControllerUserBench getTableController() { return tableController; }
    public TreeHaploController getTreeController() { return treeController; }
}
