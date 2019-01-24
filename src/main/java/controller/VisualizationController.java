package controller;

import Logging.LogClass;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import view.MitoBenchWindow;
import view.menus.VisualizationMenu;
import view.visualizations.*;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;

public class VisualizationController {

    private final Logger LOG;
    private MitoBenchWindow mito;
    private Stage stage;
    private Scene scene;

    private TableControllerUserBench tableController;
    private ChartController chartController;
    private HaplotreeController treeController;
    private GroupController groupController;

    private BarPlotHaplo barPlotHaplo;
    private BarPlotHaplo2 barPlotHaplo2;
    private BarChartGrouping barChartGrouping;
    private StackedBar stackedBar;
   // private SunburstChartCreator sunburstChart;
    private TreeView treeView;
    private ProfilePlot profilePlot;
    private PieChartViz pieChartViz;
    private ColorSchemeStackedBarChart colorScheme;

    private TabPane tabPane;
    private TabPane statsTabpane;

    private HashMap<String, List<String>> treeMap_path_to_root;
    private int profilePlotID=1;
    private TreeItem<String> tree_root;

    private LogClass logClass;



    public VisualizationController(MitoBenchWindow mitoBenchWindow){
        this.mito = mitoBenchWindow;

        treeController = mitoBenchWindow.getTreeController();
        this.tableController = mitoBenchWindow.getTableControllerUserBench();
        chartController = mitoBenchWindow.getChartController();
        groupController = mitoBenchWindow.getGroupController();

        tabPane = mitoBenchWindow.getTabpane_visualization();
        treeMap_path_to_root = treeController.getTreeMap_leaf_to_root();
        tree_root = treeController.deepcopy(treeController.getTree().getTree().getRoot());
        treeView = treeController.getTree().getTree();
        statsTabpane = mitoBenchWindow.getTabpane_statistics();

        this.LOG = mitoBenchWindow.getLogClass().getLogger(this.getClass());
        this.logClass = mitoBenchWindow.getLogClass();

        this.stage = mitoBenchWindow.getPrimaryStage();
    }

    public void initHaploBarchart(String titlePart) {
        LOG.info("Visualize data: Haplogroup frequency " + titlePart + " (Barchart)");
        Text t = new Text();
        t.setText("Haplogroup occurrences " + titlePart);
        t.setFont(Font.font(25));

        barPlotHaplo = new BarPlotHaplo(
                t.getText(),
                "Counts",
                stage,
                tableController,
                tabPane,
                logClass
        );
        //barPlotHaplo.setStyleSheet(stage);
        Tab tab = new Tab();
        tab.setId("tab_haplo_barchart");
        tab.setText("Haplogroup occurrences");
        tab.setContent(barPlotHaplo.getBarChart());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

    }

    public void initHaploBarchart2(String titlePart) throws MalformedURLException {
        LOG.info("Visualize data: Haplogroup frequency " + titlePart + " (Barchart)");
        Text t = new Text();
        t.setText("Haplogroup occurrences " + titlePart);
        t.setFont(Font.font(25));

        this.barPlotHaplo2 = new BarPlotHaplo2(
                t.getText(),
                "Number of samples",
                "Occurrences of haplogroups",
                stage,
                tableController,
                tabPane,
                logClass
        );

        //barPlotHaplo2.setStyleSheet(stage);
        Tab tab = new Tab();
        tab.setId("tab_haplo_barchart");
        tab.setText("Haplogroup occurrences");
        tab.setContent(barPlotHaplo2.getBarChart());
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


    public void initStackedBarchart(VisualizationMenu visualizationMenu) throws MalformedURLException {
        LOG.info("Visualize data: Haplogroup frequency per group (Stacked Barchart)");

        Text t = new Text();
        t.setText("Haplogroup frequency per group");
        t.setFont(Font.font(25));

        this.stackedBar = new StackedBar(t.getText(), tabPane, visualizationMenu, chartController, tableController, this, groupController);
        stackedBar.setStyleSheet(stage);
        Tab tab = new Tab();
        tab.setId("tab_stacked_bar_chart");
        tab.setText("Haplogroup frequency per group");
        tab.setContent(stackedBar.getSbc());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

    }

//    private void initSunburst(){
//        LOG.info("Visualize data: Sunburst Chart");
//
//        sunburstChart = new SunburstChartCreator(stage, tabPane, logClass);
//        Tab tab = new Tab();
//        tab.setId("tab_sunburst");
//        tab.setText("Sunburst Chart");
//        sunburstChart.getBorderPane().prefHeightProperty().bind(stage.heightProperty());
//        sunburstChart.getBorderPane().prefWidthProperty().bind(stage.widthProperty());
//        tab.setContent(sunburstChart.getBorderPane());
//        tabPane.getTabs().add(tab);
//        tabPane.getSelectionModel().select(tab);
//
//    }


    public void initPieChart(String title) throws MalformedURLException {
        LOG.info("Visualize data: Haplotypes in Group " + title + " (PieChart)");

        Text t = new Text(title);
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


    public void initProfilePlot() throws MalformedURLException {
        LOG.info("Visualize data: Haplotypes per Group (Profile Plot)");

        Text t = new Text();
        t.setText("Haplogroup profile");
        t.setFont(Font.font(100));

        profilePlot = new ProfilePlot(t.getText(), "Haplogroup", "Frequency in %", tabPane,
                logClass, profilePlotID);
        profilePlot.setStyleSheet(stage);

        Tab tab = new Tab();
        tab.setId("tab_profilePlot_" + profilePlotID);
        tab.setText("Profile Plot (pp " + profilePlotID + ")");
        tab.setContent(profilePlot.getPlot());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        profilePlotID++;

    }


    public void initMap(){
        LOG.info("Visualize data: Visualize all samples on map");

        GeographicalMapViz geographicalMapViz = new GeographicalMapViz();

        GeographicalMapController mapViewController = new GeographicalMapController(
                mito,
                groupController,
                geographicalMapViz
        );

        Tab tab = new Tab();
        tab.setId("tab_map");
        tab.setText("Map");
        tab.setContent(geographicalMapViz.getMapBasicPane());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

    }


    public void clearCharts(){
        stackedBar = null;
        barPlotHaplo = null;
        tabPane.getTabs().clear();
    }


    public Logger getLOG() {
        return LOG;
    }

    public MitoBenchWindow getMito() {
        return mito;
    }

    public Stage getStage() {
        return stage;
    }

    public Scene getScene() {
        return scene;
    }

    public TableControllerUserBench getTableController() {
        return tableController;
    }

    public ChartController getChartController() {
        return chartController;
    }

    public HaplotreeController getTreeController() {
        return treeController;
    }

    public GroupController getGroupController() {
        return groupController;
    }

    public BarPlotHaplo getBarPlotHaplo() {
        return barPlotHaplo;
    }

    public BarPlotHaplo2 getBarPlotHaplo2() {
        return barPlotHaplo2;
    }

    public BarChartGrouping getBarChartGrouping() {
        return barChartGrouping;
    }

    public StackedBar getStackedBar() {
        return stackedBar;
    }

    public TreeView getTreeView() {
        return treeView;
    }

    public ProfilePlot getProfilePlot() {
        return profilePlot;
    }

    public PieChartViz getPieChartViz() {
        return pieChartViz;
    }

    public ColorSchemeStackedBarChart getColorScheme() {
        return colorScheme;
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public TabPane getStatsTabpane() {
        return statsTabpane;
    }
    
    public HashMap<String, List<String>> getTreeMap_path_to_root() {
        return treeMap_path_to_root;
    }

    public int getProfilePlotID() {
        return profilePlotID;
    }

    public TreeItem<String> getTree_root() {
        return tree_root;
    }

    public LogClass getLogClass() {
        return logClass;
    }
}
