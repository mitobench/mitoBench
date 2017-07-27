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
import view.dialogues.information.InformationDialogue;
import view.dialogues.settings.SettingsDialogueStackedBarchart;
import controller.TableControllerUserBench;

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
    private Stage stage;
    private Scene scene;

    private TableControllerUserBench tableController;
    private ChartController chartController;
    private HaplotreeController treeController;
    private GroupController groupController;

    private BarPlotHaplo barPlotHaplo;
    private BarChartGrouping barChartGrouping;
    private StackedBar stackedBar;
    private SunburstChartCreator sunburstChart;
    private TreeView treeView;
    private ProfilePlot profilePlot;
    private PieChartViz pieChartViz;
    private ColorSchemeStackedBarChart colorScheme;

    private TabPane tabPane;
    private TabPane statsTabpane;
    private Menu menuGraphics;

    private HashMap<String, List<String>> treeMap_path_to_root;
    private int profilePlotID=1;
    private TreeItem<String> tree_root;

    private Logger LOG;
    private LogClass logClass;


    public VisualizationMenu(MitoBenchWindow mitoBenchWindow){

        menuGraphics = new Menu("Visualization");
        menuGraphics.setId("graphicsMenu");

        mito = mitoBenchWindow;
        scene = mitoBenchWindow.getScene();
        stage = mitoBenchWindow.getPrimaryStage();

        treeController = mitoBenchWindow.getTreeController();
        tableController = mitoBenchWindow.getTableControllerUserBench();
        chartController = new ChartController();
        chartController.init(tableController, treeController.getTreeMap());
        groupController = mitoBenchWindow.getGroupController();

        tabPane = mitoBenchWindow.getTabpane_visualization();
        treeMap_path_to_root = treeController.getTreeMap_leaf_to_root();
        tree_root = treeController.deepcopy(treeController.getTree().getTree().getRoot());
        treeView = treeController.getTree().getTree();
        statsTabpane = mitoBenchWindow.getTabpane_statistics();

        LOG = mitoBenchWindow.getLogClass().getLogger(this.getClass());
        logClass = mitoBenchWindow.getLogClass();
        addSubMenus();
    }


    public void initHaploBarchart(String titlePart) throws MalformedURLException {
        LOG.info("Visualize data: Haplogroup frequency " + titlePart + " (Barchart)");
        Text t = new Text();
        t.setText("Haplogroup occurrences " + titlePart);
        t.setFont(Font.font(25));

        this.barPlotHaplo = new BarPlotHaplo(t.getText(), "Counts", stage, chartController,
                tableController, tabPane, logClass);
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


    private void initMap(){
        LOG.info("Visualize data: Visualize all samples on map");

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
        plotHGfreqGroup.setOnAction(t -> {
            if(tableController.getTableColumnByName("Grouping") != null
                    && tableController.getTable().getItems().size()!=0) {

                String[][] cols = chartController.prepareColumns(new String[]{"Haplogroup", "Grouping"}, tableController.getSelectedRows());
                String[] selection_haplogroups = cols[0];
                String[] selection_groups = cols[1];

                SettingsDialogueStackedBarchart advancedStackedBarchartDialogue =
                        new SettingsDialogueStackedBarchart("Advanced Stacked Barchart Settings", selection_groups, logClass);

                // add dialog to statsTabPane
                Tab tab = advancedStackedBarchartDialogue.getTab();
                mito.getTabpane_visualization().getTabs().add(tab);

                advancedStackedBarchartDialogue.getApplyBtn().setOnAction(e -> {
                    advancedStackedBarchartDialogue.getApplyBtn();
                    try {
                        initStackedBarchart();
                    } catch (MalformedURLException e1) {
                        e1.printStackTrace();
                    }


                    //chartController.addDataStackedBarChart(stackedBar, selection_haplogroups, selection_groups);
                    chartController.addDataStackedBarChart(
                            stackedBar,
                            selection_haplogroups,
                            advancedStackedBarchartDialogue.getStackOrder(),
                            advancedStackedBarchartDialogue.getTextField_hgList().getText()
                    );

                    stackedBar.getSbc().getData().addAll(stackedBar.getSeriesList());

                    // add settings

                    stackedBar.addTooltip();
                    colorScheme = null;
                    try {
                        colorScheme = new ColorSchemeStackedBarChart(stage);
                    } catch (MalformedURLException e1) {
                        e1.printStackTrace();
                    }

                    //if(selection_haplogroups.length > 20){
                        colorScheme.setNewColors(stackedBar);
                        stackedBar.addListener();
//                    } else {
//                        colorScheme.setNewColorsLess20(stackedBar);
//                    }

                    //advancedStackedBarchartDialogue.close();
                    // remove tab from tabpane
                    mito.getTabpane_visualization().getTabs().remove(tab);
                });
            } else {
                InformationDialogue groupingWarningDialogue = new InformationDialogue(
                        "No groups defined",
                        "Please define a grouping first.",
                        null,
                        "groupWarning");
            }
        });



         /*

                    Plot HG frequency for each group

         */

        MenuItem sunburstChartItem = new MenuItem("Create Sunburst chart...");
        sunburstChartItem.setId("sunburstChart_item");
        sunburstChartItem.setOnAction(t -> {
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
                    InformationDialogue groupingWarningDialogue = new InformationDialogue(
                            "No groups defined",
                            "Please define a grouping first.",
                            null,
                            "groupWarning");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });



         /*

                    Create profile plot

         */

        MenuItem profilePlotItem = new MenuItem("Create Profile Plot");
        profilePlotItem.setId("profilePlot");
        profilePlotItem.setOnAction(t -> {
            try {
                // makes only sense if grouping exists.
                if(tableController.getTableColumnByName("Grouping") != null
                        && tableController.getTableColumnByName("Haplogroup") != null
                        && tableController.getTable().getItems().size() != 0 ){
                    initProfilePlot();
                    // get selected rows
//                    ObservableList<ObservableList> selectedTableItems = tableController.getSelectedRows();
//                    HashMap<String, List<String>> hg_to_group = chartController.getHG_to_group(selectedTableItems);

                    profilePlot.create(tableController, treeController, chartController, logClass, statsTabpane);

                } else if(tableController.getTableColumnByName("Grouping") == null && tableController.getTableColumnByName("Haplogroup") != null){
                    InformationDialogue groupingWarningDialogue = new InformationDialogue(
                            "No groups defined",
                            "Please define a grouping first.",
                            null,
                            "groupWarning");
                } else if(tableController.getTableColumnByName("Haplogroup") == null && tableController.getTableColumnByName("Grouping") != null){
                    InformationDialogue haplogroupWarningDialogue = new InformationDialogue(
                            "No haplogroups",
                            "Please assign haplogroups to your data first.",
                            null,
                            "haplogroupWarning");
                } else {
                    InformationDialogue warningDialogue = new InformationDialogue(
                            "No groups and haplogroups defined",
                            "Please define a grouping and add haplogroups to your data first.",
                            null,
                            "groupWarning");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        /*


        Pie Chart

         */

        MenuItem pieChart = new MenuItem("Create Pie Chart");
        pieChart.setId("piechart");
        pieChart.setOnAction(t -> {
            try {
                // makes only sense if grouping exists.
                if(tableController.getTable().getItems().size() != 0 ){

                    if(tableController.getTableColumnByName("Grouping") != null){
                        // get selected rows

                        String[][] cols = chartController.prepareColumns(new String[]{"Haplogroup", "Grouping"},
                                tableController.getSelectedRows());
                        String[] selection_haplogroups = cols[0];
                        String[] selection_groups = cols[1];

                        HashMap<String, ArrayList> hgs_summed = chartController.summarizeHaplogroups(selection_haplogroups,
                                chartController.getCoreHGs());
                        HashMap<String, List<XYChart.Data<String, Number>>> data_all =
                                chartController.assignHGs(hgs_summed, selection_haplogroups, selection_groups);

                        for(String group : groupController.getGroupnames()) {
                            if(!group.equals("Undefined")){
                                initPieChart(group);
                                pieChartViz.createPlot(group, data_all);
                                pieChartViz.setColor(stage);
                            }
                        }
                    } else {
                        String[][] cols = chartController.prepareColumns(new String[]{"Haplogroup"},
                                tableController.getSelectedRows());
                        String[] selection_haplogroups = cols[0];

                        HashMap<String, ArrayList> hgs_summed = chartController.summarizeHaplogroups(selection_haplogroups,
                                chartController.getCoreHGs());

                        initPieChart("Haplogroup frequency");
                        pieChartViz.createPlotSingle(hgs_summed);
                        pieChartViz.setColor(stage);

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        /*
                Clear visualization panel
         */

        MenuItem clearPlotBox = new MenuItem("Clear Charts");
        clearPlotBox.setId("clear_plots");
        clearPlotBox.setOnAction(t -> {
            try {
                clearCharts();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });


          /*
                Create grouping bar plot
         */

        MenuItem grouping_barchart = new MenuItem("Grouping bar chart");
        grouping_barchart.setId("grouping_barchart_item");
        grouping_barchart.setOnAction(t -> {
            try {
                if(tableController.getTable().getItems().size() != 0 ) {

                    TableColumn haplo_col = tableController.getTableColumnByName("Grouping");
                    if(haplo_col != null){
                        initGroupBarChart();
                        chartController.addDataBarChart(barChartGrouping, haplo_col, null);
                        barChartGrouping.setColor(stage);
                    } else {
                        InformationDialogue groupingWarningDialogue = new InformationDialogue(
                                "No groups defined",
                                "Please define a grouping first.",
                                null,
                                "groupWarning");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        /*

                Visualize data on map

         */
        Menu maps = new Menu("Map view");
        maps.setId("maps_menu");
        MenuItem mapsItem = new MenuItem("Visualize data on map (internet connection needed)");
        mapsItem.setId("maps_item");
        mapsItem.setOnAction(t -> {
            if(!tableController.isTableEmpty()){
                initMap();
            }

        });


        // add menu items
        grouping_graphics.getItems().add(grouping_barchart);
        barchart.getItems().addAll(plotHGfreq, plotHGfreqGroup);
        //haplo_graphics.getItems().addAll(barchart, sunburstChartItem, profilePlotItem, pieChart);
        // TODO removed sunburst chart
        haplo_graphics.getItems().addAll(barchart, profilePlotItem, pieChart);
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
