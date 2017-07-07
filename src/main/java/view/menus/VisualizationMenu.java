package view.menus;

import Logging.LogClass;
import controller.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import view.MitoBenchWindow;
import view.charts.*;
import view.dialogues.information.GroupingWarningDialogue;
import view.dialogues.settings.AdvancedStackedBarchartDialogue;
import view.table.controller.TableControllerUserBench;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by neukamm on 23.11.16.
 */
public class VisualizationMenu {


    private MitoBenchWindow mito;
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
    private int profilePlotID=1;
    private MapViewController mapViewController;


    public VisualizationMenu(MitoBenchWindow mitoBenchWindow){

        mito = mitoBenchWindow;
        menuGraphics = new Menu("Visualization");
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


    public void initHaploBarchart(String titlePart) throws MalformedURLException {
        LOG.info("Visualize data: Haplogroup frequency (Barchart)");

        Text t = new Text();
        t.setText("Haplogroup occurrences " + titlePart);
        t.setFont(Font.font(25));

        this.barPlotHaplo = new BarPlotHaplo(t.getText(), "Counts", stage, chartController,
                tableController, logClass);
        barPlotHaplo.setStyleSheet(stage);
        Tab tab = new Tab();
        tab.setId("tab_haplo_barchart");
        tab.setText("Haplogroup occurrences");
        tab.setContent(barPlotHaplo.getBarChart());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

    }

    public void initGroupBarChart() throws MalformedURLException {
        LOG.info("Visualize data: Group frequency (Barchart)");

        Text t = new Text();
        t.setText("Number of samples per group");
        t.setFont(Font.font(25));

        barChartGrouping = new BarChartGrouping(t.getText(), "# of Samples", tabPane, logClass);
        barChartGrouping.setStyleSheet(stage);

        Tab tab = new Tab();
        tab.setId("tab_group_barchart");
        tab.setText("Bar Chart Grouping");
        tab.setContent(barChartGrouping.getBarChart());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }


    public void initStackedBarchart() throws MalformedURLException {
        LOG.info("Visualize data: Haplogroup frequency per group (Stacked Barchart)");

        Text t = new Text();
        t.setText("Haplogroup frequency per group");
        t.setFont(Font.font(25));

        this.stackedBar = new StackedBar(t.getText(), tabPane, this);
        stackedBar.setStyleSheet(stage);
        Tab tab = new Tab();
        tab.setId("tab_stacked_bar_chart");
        tab.setText("Haplogroup frequency per group");
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


    private void initPieChart(String title) throws MalformedURLException {
        LOG.info("Visualize data: Haplotypes in Group " + title + " (PieChart)");

        Text t = new Text(title);
        t.setText("Haplogroup frequency per group");
        t.setFont(Font.font(25));

        pieChartViz = new PieChartViz(t.getText(), tabPane, logClass);
        pieChartViz.setStyleSheet(stage);
        Tab tab = new Tab();
        tab.setId("tab_piechart");
        tab.setText("Pie Chart");
        pieChartViz.getChart().prefHeightProperty().bind(stage.heightProperty());
        pieChartViz.getChart().prefWidthProperty().bind(stage.widthProperty());
        tab.setContent(pieChartViz.getChart());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

    }


    private void initProfilePlot() throws MalformedURLException {
        LOG.info("Visualize data: Haplotypes per Group (Profile Plot)");

        Text t = new Text();
        t.setText("Haplogroup profile");
        t.setFont(Font.font(100));

        profilePlot = new ProfilePlot(t.getText(), "Haplogroup", "Frequency in %", tabPane,
                logClass, profilePlotID);
        profilePlot.setStyleSheet(stage);

        Tab tab = new Tab();
        tab.setId("tab_profilePlot_" + profilePlotID);
        tab.setText("Profile Plot (" + profilePlotID + ")");
        tab.setContent(profilePlot.getPlot());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        profilePlotID++;

    }


    private void initMap(String title){
        LOG.info("Visualize data: Visualize all samples on map");

//        MapViewController mapViewController = new MapViewController(tableController.getTableColumnByName("ID"),
//                tableController.getTableColumnByName("Location"),
//                tableController.getTable().getItems());

        LeafletController mapViewController = null;
        try {
            mapViewController = new LeafletController(mito,
                    groupController
            );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Tab tab = new Tab();
        tab.setId("tab_map");
        tab.setText("Map");
        tab.setContent(mapViewController.getMap());
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
                            initHaploBarchart("(all data)");
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
                            try {
                                initStackedBarchart();
                            } catch (MalformedURLException e1) {
                                e1.printStackTrace();
                            }


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
                } else {
                    GroupingWarningDialogue groupingWarningDialogue = new GroupingWarningDialogue(
                            "No groups defined",
                            "Please define a grouping first.",
                            null,
                            "groupWarning");
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
                    } else {
                        GroupingWarningDialogue groupingWarningDialogue = new GroupingWarningDialogue(
                                "No groups defined",
                                "Please define a grouping first.",
                                null,
                                "groupWarning");
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

                        profilePlot.create(tableController, treeController, chartController, logClass, scene, statsTabpane);

                    } else {
                        GroupingWarningDialogue groupingWarningDialogue = new GroupingWarningDialogue(
                                "No groups defined",
                                "Please define a grouping first.",
                                null,
                                "groupWarning");
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
                    } else {
                        GroupingWarningDialogue groupingWarningDialogue = new GroupingWarningDialogue(
                                "No groups defined",
                                "Please define a grouping first.",
                                null,
                                "groupWarning");
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
                        } else {
                            GroupingWarningDialogue groupingWarningDialogue = new GroupingWarningDialogue(
                                    "No groups defined",
                                    "Please define a grouping first.",
                                    null,
                                    "groupWarning");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Menu maps = new Menu("Map view");
        maps.setId("maps_menu");
        MenuItem mapsItem = new MenuItem("Visualize data on map (internet connection needed)");
        mapsItem.setId("maps_item");
        mapsItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                if(!tableController.isTableEmpty()){
                    initMap("Map");
                }

            }
        });

        // add menu items
        grouping_graphics.getItems().add(grouping_barchart);
        barchart.getItems().addAll(plotHGfreq, plotHGfreqGroup);
        haplo_graphics.getItems().addAll(barchart, sunburstChartItem, profilePlotItem, pieCcart);
        maps.getItems().add(mapsItem);

        menuGraphics.getItems().addAll(haplo_graphics, grouping_graphics, maps, new SeparatorMenuItem(), clearPlotBox);
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
