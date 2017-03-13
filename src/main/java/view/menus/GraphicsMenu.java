package view.menus;

import Logging.LogClass;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import view.MitoBenchWindow;
import view.charts.*;
import view.dialogues.settings.AdvancedStackedBarchartDialogue;
import view.groups.GroupController;
import view.table.controller.TableControllerUserBench;
import view.tree.HaplotreeController;

import java.net.MalformedURLException;
import java.util.ArrayList;
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
    private HaplotreeController treeController;
    private ProfilePlot profilePlot;
    private Scene scene;
    private TabPane statsTabpane;
    private PieChartViz pieChartViz;
    private GroupController groupController;
    private ColorSchemeStackedBarChart colorScheme;
    private Logger LOG;
    private LogClass logClass;


    public GraphicsMenu(MitoBenchWindow mitoBenchWindow){

        menuGraphics = new Menu("Graphics");
        menuGraphics.setId("graphicsMenu");
        treeController = mitoBenchWindow.getTreeController();
        tableController = mitoBenchWindow.getTableControllerUserBench();
        tabPane = mitoBenchWindow.getTabpane_visualization();
        treeMap_path_to_root = treeController.getTreeMap_leaf_to_root();
        tree_root = treeController.deepcopy(treeController.getTree().getTree().getRoot());
        treeView = treeController.getTree().getTree();
        stage = mitoBenchWindow.getPrimaryStage();
        chartController = new ChartController();
        chartController.init(tableController, treeController.getTreeMap());
        scene = mitoBenchWindow.getScene();
        this.statsTabpane = mitoBenchWindow.getTabpane_statistics();
        this.groupController = mitoBenchWindow.getGroupController();
        LOG = mitoBenchWindow.getLogClass().getLogger(this.getClass());
        logClass = mitoBenchWindow.getLogClass();
        addSubMenus();
    }


    public void initHaploBarchart() throws MalformedURLException {
        LOG.info("Visualize data: Haplogroup frequency (Barchart)");

        this.barPlotHaplo = new BarPlotHaplo("Haplogroup occurrences", "Counts", stage, chartController,
                tableController, logClass);
        Tab tab = new Tab();
        tab.setId("tab_haplo_barchart");
        tab.setText("Bar Chart haplogroups");
        tab.setContent(barPlotHaplo.getBarChart());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

    }

    public void initGroupBarChart(){
        LOG.info("Visualize data: Group frequency (Barchart)");

        barChartGrouping = new BarChartGrouping("Number of samples per group", "", tabPane, logClass);
        Tab tab = new Tab();
        tab.setId("tab_group_barchart");
        tab.setText("Bar Chart Grouping");
        tab.setContent(barChartGrouping.getBarChart());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }


    public void initStackedBarchart(){
        LOG.info("Visualize data: Haplogroup frequency per group (Stacked Barchart)");

        this.stackedBar = new StackedBar("Haplogroup frequency per group", tabPane, this);
        Tab tab = new Tab();
        tab.setId("tab_stacked_bar_chart");
        tab.setText("Bar Chart per group");
        tab.setContent(stackedBar.getSbc());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

    }

    private void initSunburst(){
        LOG.info("Visualize data: Sunburst Chart");

        sunburstChart = new SunburstChartCreator(stage, tabPane, logClass);
        Tab tab = new Tab();
        tab.setId("tab_sunburst");
        tab.setText("Sunburst Chart");
        sunburstChart.getBorderPane().prefHeightProperty().bind(stage.heightProperty());
        sunburstChart.getBorderPane().prefWidthProperty().bind(stage.widthProperty());
        tab.setContent(sunburstChart.getBorderPane());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);


    }


    private void initPieChart(String title){
        LOG.info("Visualize data: Haplotypes in Group " + title + " (PieChart)");

        pieChartViz = new PieChartViz(title, tabPane, logClass);
        Tab tab = new Tab();
        tab.setId("tab_piechart");
        tab.setText("Pie Chart");
        pieChartViz.getChart().prefHeightProperty().bind(stage.heightProperty());
        pieChartViz.getChart().prefWidthProperty().bind(stage.widthProperty());
        tab.setContent(pieChartViz.getChart());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

    }


    private void initProfilePlot(){
        LOG.info("Visualize data: Haplotypes per Group (Profile Plot)");

        profilePlot = new ProfilePlot("Profile Plot", "Haplogroup", "Frequency in %", tabPane,
                logClass, groupController);
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
                            createHaploBarchart(haplo_col, null);
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
                if(tableController.getTableColumnByName("Grouping") != null
                        && tableController.getTable().getItems().size()!=0) {

                    String[][] cols = chartController.prepareColumns(new String[]{"Haplogroup", "Grouping"}, tableController.getSelectedRows());
                    String[] selection_haplogroups = cols[0];
                    String[] selection_groups = cols[1];

                    AdvancedStackedBarchartDialogue advancedStackedBarchartDialogue =
                            new AdvancedStackedBarchartDialogue("Advanced Stacked Barchart Settings", selection_groups, logClass);

                    advancedStackedBarchartDialogue.getApplyBtn().setOnAction(new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent e) {
                            advancedStackedBarchartDialogue.getApplyBtn();
                            initStackedBarchart();


                            //chartController.addDataStackedBarChart(stackedBar, selection_haplogroups, selection_groups);
                            chartController.addDataStackedBarChart(stackedBar, selection_haplogroups, advancedStackedBarchartDialogue.getStackOrder());
                            stackedBar.getSbc().getData().addAll(stackedBar.getSeriesList());

                            // add settings

                            stackedBar.addTooltip();
                            colorScheme = null;
                            try {
                                colorScheme = new ColorSchemeStackedBarChart(stage);
                            } catch (MalformedURLException e1) {
                                e1.printStackTrace();
                            }

                            if(selection_haplogroups.length > 20){
                                colorScheme.setNewColors(stackedBar);
                                stackedBar.addListener();
                            } else {
                                colorScheme.setNewColorsLess20(stackedBar);
                            }

                            advancedStackedBarchartDialogue.close();
                        }
                    });
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

                        profilePlot.create(tableController, chartController, treeController, statsTabpane, scene, logClass);



                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /*


        Pie Chart

         */

        MenuItem pieCcart = new MenuItem("Create Pie Chart");
        pieCcart.setId("piechart");
        pieCcart.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    // makes only sense if grouping exists.
                    if(tableController.getTableColumnByName("Grouping") != null
                            && tableController.getTable().getItems().size() != 0 ){


                        // get selected rows
                        ObservableList<ObservableList> selectedTableItems = tableController.getSelectedRows();
                        HashMap<String, List<String>> hg_to_group = chartController.getHG_to_group(selectedTableItems);


                        String[][] cols = chartController.prepareColumns(new String[]{"Haplogroup", "Grouping"},
                                tableController.getSelectedRows());
                        String[] selection_haplogroups = cols[0];
                        String[] selection_groups = cols[1];

                        HashMap<String, ArrayList> hgs_summed = chartController.summarizeHaolpgroups(selection_haplogroups,
                                chartController.getCoreHGs());
                        HashMap<String, List<XYChart.Data<String, Number>>> data_all =
                              chartController.assignHGs(hgs_summed, selection_haplogroups, selection_groups);

                        for(String group : groupController.getGroupnames()) {
                            initPieChart(group);
                            pieChartViz.createPlot(group, data_all);
                            pieChartViz.setColor(stage);
                        }



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
                            chartController.addDataBarChart(barChartGrouping, haplo_col, null);
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
        haplo_graphics.getItems().addAll(barchart, sunburstChartItem, profilePlotItem, pieCcart);


        menuGraphics.getItems().addAll(haplo_graphics, grouping_graphics, new SeparatorMenuItem(), clearPlotBox);
    }

    public void createHaploBarchart(TableColumn haplo_col, List<String> columnData ) throws MalformedURLException {
        chartController.addDataBarChart(barPlotHaplo, haplo_col, columnData);
    }


    public Menu getMenuGraphics() {
        return menuGraphics;
    }
    public TableControllerUserBench getTableController() { return tableController; }
    public HaplotreeController getTreeController() { return treeController; }
    public LogClass getLogClass() { return logClass; }
}
