package controller;

import Logging.LogClass;
import io.writer.ImageWriter;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import view.MitoBenchWindow;
import view.menus.VisualizationMenu;
import view.visualizations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class VisualizationController {

    private final Logger LOG;
    private final GridPane grid_piecharts;
    private MitoBenchWindow mito;
    private Stage stage;

    private TableControllerUserBench tableController;
    private ChartController chartController;
    private HaplotreeController treeController;
    private GroupController groupController;

    private BarPlotHaplo barPlotHaplo;
    private BarPlotHaplo2 barPlotHaplo2;
    private BarChartGrouping barChartGrouping;
    private StackedBar stackedBar;
    private TreeView treeView;
    private ProfilePlot profilePlot;
    private PieChartViz pieChartViz;

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

        grid_piecharts = new GridPane();

    }

    public void initHaploBarchart(String titlePart) {
        LOG.info("Visualize data: Haplogroup frequency " + titlePart + " (Barchart)");
        Text t = new Text();
        t.setText("Haplogroup occurrences of "  + titlePart);
        t.setFont(Font.font(25));

        barPlotHaplo = new BarPlotHaplo(
                t.getText(),
                "Counts",
                stage,
                tableController,
                tabPane,
                logClass
        );
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

        Tab tab = new Tab();
        tab.setId("tab_haplo_barchart");
        tab.setText("Haplogroup occurrences");
        tab.setContent(barPlotHaplo2.getBarChart());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

    }

    public void initGroupBarChart() {
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


    public void initStackedBarchart(VisualizationMenu visualizationMenu) {
        LOG.info("Visualize data: Haplogroup frequency per group (Stacked Barchart)");

        Text t = new Text();
        t.setText("Haplogroup frequency per group");
        t.setFont(Font.font(25));

        this.stackedBar = new StackedBar(t.getText(), tabPane, visualizationMenu,
                chartController, tableController, this);
        //stackedBar.setStyleSheet(stage);
        URL url = this.getClass().getResource("/css/ColorsStackedBarChart.css");
        stage.getScene().getStylesheets().add(url.toExternalForm());

        Tab tab = new Tab();
        tab.setId("tab_stacked_bar_chart");
        tab.setText("Haplogroup frequency per group");
        tab.setContent(stackedBar.getSbc());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

    }


    public void initPieChart(String title, int curr_row, int curr_col) {
        LOG.info("Visualize data: Haplotypes in Group " + title + " (PieChart)");

        Text t = new Text(title);
        t.setFont(Font.font(25));

        pieChartViz = new PieChartViz(t.getText(), tabPane, logClass);
        URL url = this.getClass().getResource("/css/ColorsPieChart.css");
        stage.getScene().getStylesheets().add(url.toExternalForm());
        pieChartViz.setStyleSheet(stage);
        grid_piecharts.setGridLinesVisible(true);
        grid_piecharts.add(pieChartViz.getChart(),curr_col,curr_row,1,1);


    }

    public void visualizePiechart(){
        Tab tab = new Tab();
        tab.setId("tab_piechart");
        tab.setText("Pie Chart");
        ScrollPane scrollpane = new ScrollPane(grid_piecharts);
        tab.setContent(scrollpane);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        setContextMenuPie(grid_piecharts);

    }

    /**
     * This method initializes the context menu to save chart as image
     */
    protected void setContextMenuPie(Node node){

        //adding a context menu item to the chart
        final MenuItem saveAsPng = new MenuItem("Save as png");
        saveAsPng.setOnAction(event -> {
            ImageWriter imageWriter = new ImageWriter(mito.getLogClass());

            imageWriter.saveImage(node);

        });

        final ContextMenu menu = new ContextMenu(saveAsPng);

        node.setOnMouseClicked(event -> {
            if (MouseButton.SECONDARY.equals(event.getButton())) {
                menu.show(node, event.getScreenX(), event.getScreenY());
            }
        });

    }


    public void initProfilePlot() {
        LOG.info("Visualize data: Haplotypes per Group (Profile Plot)");

        Text t = new Text();
        t.setText("Haplogroup profile");
        t.setFont(Font.font(100));

        profilePlot = new ProfilePlot(t.getText(), "Haplogroup", "Occurrences", tabPane,
                logClass, profilePlotID, mito);
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
        tab.setContent(geographicalMapViz.getBasicPane());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

    }


    public void clearCharts(){
        stackedBar = null;
        barPlotHaplo = null;
        grid_piecharts.getChildren().clear();
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

    public GridPane getGrid_piecharts() {
        return grid_piecharts;
    }
}
