package view.menus;

import Logging.LogClass;
import analysis.HaplotypeCaller;
import com.sun.javafx.iio.ImageLoaderFactory;
import controller.*;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import view.MitoBenchWindow;
import view.dialogues.settings.HGlistProfilePlot;
import view.dialogues.settings.PieChartSettingsDialogue;
import view.visualizations.*;
import view.dialogues.information.InformationDialogue;
import view.dialogues.settings.SettingsDialogueStackedBarchart;
import controller.TableControllerUserBench;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;



/**
 * Created by neukamm on 23.11.16.
 */
public class VisualizationMenu {

    private final VisualizationController visualizationController;
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
    //private SunburstChartCreator sunburstChart;
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

        logClass = mitoBenchWindow.getLogClass();

        treeController = mitoBenchWindow.getTreeController();
        tableController = mitoBenchWindow.getTableControllerUserBench();
        chartController = mitoBenchWindow.getChartController();
        groupController = mitoBenchWindow.getGroupController();
        tabPane = mitoBenchWindow.getTabpane_visualization();

        visualizationController = new VisualizationController(
                mitoBenchWindow
        );

        treeMap_path_to_root = treeController.getTreeMap_leaf_to_root();
        tree_root = treeController.deepcopy(treeController.getTree().getTree().getRoot());
        treeView = treeController.getTree().getTree();
        statsTabpane = mitoBenchWindow.getTabpane_statistics();



        treeView = visualizationController.getTreeView();
        tree_root = visualizationController.getTree_root();

        addSubMenus();
    }


//    public void initHaploBarchart(String titlePart) {
//        LOG.info("Visualize data: Haplogroup frequency " + titlePart + " (Barchart)");
//        Text t = new Text();
//        t.setText("Haplogroup occurrences " + titlePart);
//        t.setFont(Font.font(25));
//
//        this.barPlotHaplo = new BarPlotHaplo(
//                t.getText(),
//                "Counts",
//                stage,
//                tableController,
//                tabPane,
//                logClass
//        );
//        //barPlotHaplo.setStyleSheet(stage);
//        Tab tab = new Tab();
//        tab.setId("tab_haplo_barchart");
//        tab.setText("Haplogroup occurrences");
//        tab.setContent(barPlotHaplo.getBarChart());
//        tabPane.getTabs().add(tab);
//        tabPane.getSelectionModel().select(tab);
//
//    }

//    public void initHaploBarchart2(String titlePart) throws MalformedURLException {
//        LOG.info("Visualize data: Haplogroup frequency " + titlePart + " (Barchart)");
//        Text t = new Text();
//        t.setText("Haplogroup occurrences " + titlePart);
//        t.setFont(Font.font(25));
//
//        this.barPlotHaplo2 = new BarPlotHaplo2(
//                t.getText(),
//                "Number of samples",
//                "Occurrences of haplogroups",
//                stage,
//                tableController,
//                tabPane,
//                logClass
//        );
//
//        //barPlotHaplo2.setStyleSheet(stage);
//        Tab tab = new Tab();
//        tab.setId("tab_haplo_barchart");
//        tab.setText("Haplogroup occurrences");
//        tab.setContent(barPlotHaplo2.getBarChart());
//        tabPane.getTabs().add(tab);
//        tabPane.getSelectionModel().select(tab);
//
//    }
//
//    public void initGroupBarChart() throws MalformedURLException {
//        LOG.info("Visualize data: Group frequency (Barchart)");
//
//        Text t = new Text();
//        t.setText("Number of samples per group");
//        t.setFont(Font.font(25));
//
//        barChartGrouping = new BarChartGrouping(t.getText(), "# of Samples", tabPane, logClass);
//        barChartGrouping.setStyleSheet(stage);
//
//        Tab tab = new Tab();
//        tab.setId("tab_group_barchart");
//        tab.setText("Bar Chart Grouping");
//        tab.setContent(barChartGrouping.getBarChart());
//        tabPane.getTabs().add(tab);
//        tabPane.getSelectionModel().select(tab);
//    }

//
//    public void initStackedBarchart() throws MalformedURLException {
//        LOG.info("Visualize data: Haplogroup frequency per group (Stacked Barchart)");
//
//        Text t = new Text();
//        t.setText("Haplogroup frequency per group");
//        t.setFont(Font.font(25));
//
//        this.stackedBar = new StackedBar(t.getText(), tabPane, this, chartController, tableController);
//        stackedBar.setStyleSheet(stage);
//        Tab tab = new Tab();
//        tab.setId("tab_stacked_bar_chart");
//        tab.setText("Haplogroup frequency per group");
//        tab.setContent(stackedBar.getSbc());
//        tabPane.getTabs().add(tab);
//        tabPane.getSelectionModel().select(tab);
//
//    }

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


//    private void initPieChart(String title) throws MalformedURLException {
//        LOG.info("Visualize data: Haplotypes in Group " + title + " (PieChart)");
//
//        Text t = new Text(title);
//        t.setFont(Font.font(25));
//
//        pieChartViz = new PieChartViz(t.getText(), tabPane, logClass);
//        pieChartViz.setStyleSheet(stage);
//        Tab tab = new Tab();
//        tab.setId("tab_piechart");
//        tab.setText("Pie Chart");
//        pieChartViz.getChart().prefHeightProperty().bind(stage.heightProperty());
//        pieChartViz.getChart().prefWidthProperty().bind(stage.widthProperty());
//        tab.setContent(pieChartViz.getChart());
//        tabPane.getTabs().add(tab);
//        tabPane.getSelectionModel().select(tab);
//
//    }


//    private void initProfilePlot() throws MalformedURLException {
//        LOG.info("Visualize data: Haplotypes per Group (Profile Plot)");
//
//        Text t = new Text();
//        t.setText("Haplogroup profile");
//        t.setFont(Font.font(100));
//
//        profilePlot = new ProfilePlot(t.getText(), "Haplogroup", "Frequency in %", tabPane,
//                logClass, profilePlotID);
//        profilePlot.setStyleSheet(stage);
//
//        Tab tab = new Tab();
//        tab.setId("tab_profilePlot_" + profilePlotID);
//        tab.setText("Profile Plot (pp " + profilePlotID + ")");
//        tab.setContent(profilePlot.getPlot());
//        tabPane.getTabs().add(tab);
//        tabPane.getSelectionModel().select(tab);
//        profilePlotID++;
//
//    }


//    private void initMap(){
//        LOG.info("Visualize data: Visualize all samples on map");
//
//        GeographicalMapViz geographicalMapViz = new GeographicalMapViz();
//
//        GeographicalMapController mapViewController = new GeographicalMapController(
//                mito,
//                groupController,
//                geographicalMapViz
//        );
//
//        Tab tab = new Tab();
//        tab.setId("tab_map");
//        tab.setText("Map");
//        tab.setContent(geographicalMapViz.getMapBasicPane());
//        tabPane.getTabs().add(tab);
//        tabPane.getSelectionModel().select(tab);
//
//    }




    public void addSubMenus() {

        Menu haplo_graphics = new Menu("Haplogroups");
        haplo_graphics.setId("haplo_graphics");

        Menu barchart = new Menu("Create Barchart...");
        barchart.setId("barchart");

        Menu grouping_graphics = new Menu("Grouping");
        grouping_graphics.setId("grouping_graphics");

          /*

                Visualize data on map

         */
        Menu maps = new Menu("Map view");
        maps.setId("maps_menu");
        MenuItem mapsItem = new MenuItem("Visualize data on map (internet connection needed)");
        mapsItem.setId("maps_item");
        mapsItem.setOnAction(t -> {
            if(!tableController.isTableEmpty()){
                visualizationController.initMap();
            }

        });

        Menu options = new Menu("Options");
        options.setId("menu_options");

        /*
                        Plot HG frequency

         */

        MenuItem plotHGfreq = new MenuItem("Plot haplogroup frequency as barchart");
        plotHGfreq.setId("plotHGfreq_item");
        plotHGfreq.setOnAction(t -> {
            try {

                if(tableController.getTable().getItems().size() != 0 ){
                    TableColumn haplo_col = tableController.getTableColumnByName("Haplogroup");

                    if(haplo_col!=null){
                        visualizationController.initHaploBarchart("(all data)");
                        createHaploBarchart(haplo_col, null);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        MenuItem plotHGfreqHist = new MenuItem("Plot haplogroup frequency as histogram");
        plotHGfreqHist.setId("plotHGfreq_item");
        plotHGfreqHist.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {

                    if(tableController.getTable().getItems().size() != 0 ){
                        TableColumn haplo_col = tableController.getTableColumnByName("Haplogroup");

                        if(haplo_col!=null){
                            visualizationController.initHaploBarchart2("(all data)");
                            createHaploBarchart2(haplo_col, null);
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
            if(//tableController.getTableColumnByName("Grouping") != null &&
                tableController.getTable().getItems().size()!=0) {

                String[] selection_groups;
                String[] selection_haplogroups;

                if(!tableController.getGroupController().groupingExists()) {
                    String[][] cols = chartController.prepareColumns(new String[]{"Haplogroup"}, tableController.getSelectedRows());
                    selection_haplogroups = cols[0];
                    selection_groups = new String[]{"All data"};
                } else {
                    String[][] cols = chartController.prepareColumns(new String[]{"Haplogroup", "Grouping"}, tableController.getSelectedRows());
                    selection_haplogroups = cols[0];
                    selection_groups = cols[1];
                }


                SettingsDialogueStackedBarchart advancedStackedBarchartDialogue =
                        new SettingsDialogueStackedBarchart("Advanced Stacked Barchart Settings", selection_groups,
                                logClass, mito);

                // add dialog to statsTabPane
                Tab tab = advancedStackedBarchartDialogue.getTab();
                mito.getTabpane_visualization().getTabs().add(tab);
                mito.getTabpane_visualization().getSelectionModel().select(tab);

                advancedStackedBarchartDialogue.getApplyBtn().setOnAction(e -> {
                    advancedStackedBarchartDialogue.getApplyBtn();
                    try {
                        visualizationController.initStackedBarchart(this);
                    } catch (MalformedURLException e1) {
                        e1.printStackTrace();
                    }

                    stackedBar = visualizationController.getStackedBar();
                    //chartController.addDataStackedBarChart(stackedBar, selection_haplogroups, selection_groups);
                    chartController.addDataStackedBarChart(
                            stackedBar,
                            selection_haplogroups,
                            advancedStackedBarchartDialogue.getStackOrder(),
                            advancedStackedBarchartDialogue.getTextField_hgList().getText()
                    );

                    String[] hg_list;
                    if(advancedStackedBarchartDialogue.getDefault_list_checkbox().isSelected()){
                        hg_list = mito.getChartController().getCoreHGs();
                    } else {
                        hg_list = advancedStackedBarchartDialogue.getTextField_hgList().getText().split(",");
                    }


                    stackedBar.setHg_user_selection(hg_list);

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
//        sunburstChartItem.setOnAction(t -> {
//            try {
//
//                // makes only sense if grouping exists.
//                if(tableController.getTableColumnByName("Grouping") != null
//                        && tableController.getTable().getItems().size() != 0 ){
//                    visualizationController.initSunburst();
//                    // get selected rows
//                    ObservableList<ObservableList> selectedTableItems = tableController.getSelectedRows();
//                    HashMap<String, List<String>> hg_to_group = chartController.getHG_to_group(selectedTableItems);
//                    sunburstChart.create(hg_to_group, chartController.getWeights(), treeMap_path_to_root, tree_root, treeView);
//                } else {
//                    InformationDialogue groupingWarningDialogue = new InformationDialogue(
//                            "No groups defined",
//                            "Please define a grouping first.",
//                            null,
//                            "groupWarning");
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });



         /*

                    Create profile plot

         */

        MenuItem profilePlotItem = new MenuItem("Create Profile Plot");
        profilePlotItem.setId("profilePlot");
        profilePlotItem.setOnAction(t -> {
            try {
                // makes only sense if grouping exists.
                if(//tableController.getTableColumnByName("Grouping") != null &&
                      tableController.getTableColumnByName("Haplogroup") != null
                        && tableController.getTable().getItems().size() != 0 ){


                    HGlistProfilePlot hGlistProfilePlot = new HGlistProfilePlot("Profile plot configuration", logClass, mito);
                    hGlistProfilePlot.init();
                    // add dialog to statsTabPane
                    Tab tab = hGlistProfilePlot.getTab();
                    mito.getTabpane_visualization().getTabs().add(tab);
                    mito.getTabpane_visualization().getSelectionModel().select(tab);

                    hGlistProfilePlot.getOkBtn().setOnAction(e -> {
                        try {
                            visualizationController.initProfilePlot();
                            profilePlot = visualizationController.getProfilePlot();
                            profilePlot.create(tableController, treeController, chartController, logClass, statsTabpane, hGlistProfilePlot.getHGsForProfilelotVis());
                            // remove tab from tabpane
                            mito.getTabpane_visualization().getTabs().remove(tab);
                        } catch (MalformedURLException e1) {
                            e1.printStackTrace();

                        }
                    });


                }
                else if(tableController.getTableColumnByName("Haplogroup") == null && tableController.getTableColumnByName("Grouping") != null){
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

                    PieChartSettingsDialogue pieChartSettingsDialogue =
                            new PieChartSettingsDialogue("Advanced Piechart Settings", logClass, mito);

                    // add dialog to statsTabPane
                    Tab tab = pieChartSettingsDialogue.getTab();
                    mito.getTabpane_visualization().getTabs().add(tab);
                    mito.getTabpane_visualization().getSelectionModel().select(tab);


                    pieChartSettingsDialogue.getApplyBtn().setOnAction(e -> {

                        String[] hg_list;
                        if(pieChartSettingsDialogue.getDefault_list_checkbox().isSelected()){
                            hg_list = chartController.getCoreHGs();
                        } else {
                            hg_list = pieChartSettingsDialogue.getTextField_hgList().getText().split(",");
                        }
                        String[] hg_list_trimmed = Arrays.stream(hg_list).map(String::trim).toArray(String[]::new);


                        if(tableController.getTableColumnByName("Grouping") != null){
                            // get selected rows
                            String[][] cols = chartController.prepareColumns(new String[]{"Haplogroup", "Grouping"},
                                    tableController.getSelectedRows());
                            String[] selection_haplogroups = cols[0];
                            String[] selection_groups = cols[1];


                            HashMap<String, ArrayList> hgs_summed = chartController.summarizeHaplogroups(selection_haplogroups,
                                    hg_list_trimmed);
                            HashMap<String, List<XYChart.Data<String, Number>>> data_all =
                                    chartController.assignHGs(hgs_summed, selection_haplogroups, selection_groups);

                            for(String group : groupController.getGroupnames()) {
                                if(!group.equals("Undefined")){
                                    try {
                                        visualizationController.initPieChart(group);
                                    } catch (MalformedURLException e1) {
                                        e1.printStackTrace();
                                    }

                                    pieChartViz = visualizationController.getPieChartViz();
                                    pieChartViz.createPlot(group, data_all);
                                    pieChartViz.setColor(stage);
                                }
                            }
                        } else {

                            if(tableController.getTableColumnByName("Haplogroup") != null){
                                String[][] cols = chartController.prepareColumns(new String[]{"Haplogroup"},
                                        tableController.getSelectedRows());
                                String[] selection_haplogroups = cols[0];

                                HashMap<String, ArrayList> hgs_summed = chartController.summarizeHaplogroups(selection_haplogroups,
                                        hg_list_trimmed);

                                try {
                                    visualizationController.initPieChart("Haplogroup frequency");
                                } catch (MalformedURLException e1) {
                                    e1.printStackTrace();
                                }
                                pieChartViz = visualizationController.getPieChartViz();
                                pieChartViz.createPlotSingle(hgs_summed);
                                pieChartViz.setColor(stage);
                            }
                        }

                        // remove tab from tabpane
                        mito.getTabpane_visualization().getTabs().remove(tab);
                    });



                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Tree visualization of samples (according haplogrep2)
        MenuItem samples_haplo_tree = new MenuItem("Create Sample Tree");
        samples_haplo_tree.setId("menuitem_sampletree");
        samples_haplo_tree.setOnAction(t -> {
            try {
                if(tableController.getTable().getItems().size() != 0 ) {

                    HaplotypeCaller haplotypeCaller = new HaplotypeCaller(tableController, logClass);
                    Task task = new Task() {
                        @Override
                        protected Object call() throws Exception {
                            haplotypeCaller.call("--lineage");
                            return true;
                        }
                    };

                    mito.getProgressBarhandler().activate(task.progressProperty());
                    task.setOnSucceeded((EventHandler<Event>) event -> {
                        haplotypeCaller.deleteTmpFiles();
                        mito.getProgressBarhandler().stop();

                        // read graphviz file

                        SampleTree sampleTree = new SampleTree("","", mito.getLogClass());
                        try {
                            sampleTree.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Tab sampleTree_tab = new Tab();
                        ScrollPane scrollPane_samples_tree = new ScrollPane();

                        ImageView imageView = new ImageView(sampleTree.getImg());

                        scrollPane_samples_tree.setContent(imageView);
                        sampleTree_tab.setContent(scrollPane_samples_tree);

                        mito.getTabpane_visualization().getTabs().add(sampleTree_tab);

                        // delete tmp file
                        try {
                            Files.delete(new File("haplogroups.hsd.dot").toPath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }




                    });

                    new Thread(task).start();


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
                visualizationController.clearCharts();

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
                        visualizationController.initGroupBarChart();
                        barChartGrouping = visualizationController.getBarChartGrouping();
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

        MenuItem showTickLabels = new CheckMenuItem("Show label (x axis)");
        showTickLabels.setId("menuItem_showLabel");
        ((CheckMenuItem) showTickLabels).setSelected(true);

        showTickLabels.setOnAction(t -> {
//            if(((CheckMenuItem) showTickLabels).isSelected()){
//                String id = this.mito.getTabpane_visualization().getSelectionModel().getSelectedItem().getId();
//                Chart c = (Chart)this.mito.getTabpane_visualization().getSelectionModel().getSelectedItem().getContent();
//                if (id.contains("stacked_bar_chart")){
//                    StackedBar ac = (StackedBar) c;
//                    ac.showLabelXAxis();
//                }
//
//                System.out.println(id + " now selected");
//            } else if (!((CheckMenuItem) showTickLabels).isSelected()){
//                String id = this.mito.getTabpane_visualization().getSelectionModel().getSelectedItem().getId();
//                Chart c = (Chart)this.mito.getTabpane_visualization().getSelectionModel().getSelectedItem().getContent();
//                if (id.contains("stacked_bar_chart")){
//                    StackedBar ac = (StackedBar) c;
//                    ac.hideLabelXAxis();
//                }
//                System.out.println(id+ " now NOT selected");
//            }
        });



        // add menu items
        grouping_graphics.getItems().add(grouping_barchart);
        barchart.getItems().addAll(plotHGfreq, plotHGfreqHist, plotHGfreqGroup);
        haplo_graphics.getItems().addAll(barchart, profilePlotItem, pieChart, samples_haplo_tree);
        maps.getItems().add(mapsItem);
        options.getItems().addAll(showTickLabels, clearPlotBox);

        menuGraphics.getItems().addAll(haplo_graphics, grouping_graphics, maps, new SeparatorMenuItem(), options);
    }

    public void createHaploBarchart(TableColumn haplo_col, List<String> columnData ) throws MalformedURLException {
        barPlotHaplo = visualizationController.getBarPlotHaplo();
        chartController.addDataBarChart(barPlotHaplo, haplo_col, columnData);
    }

    public void createHaploBarchart2(TableColumn haplo_col, List<String> columnData ) throws MalformedURLException {
        barPlotHaplo2 = visualizationController.getBarPlotHaplo2();
        chartController.addDataBarChart(barPlotHaplo2, haplo_col, columnData);
    }


    public Menu getMenuGraphics() {
        return menuGraphics;
    }
    public TableControllerUserBench getTableController() { return tableController; }
    public HaplotreeController getTreeController() { return treeController; }
    public LogClass getLogClass() { return logClass; }

}
