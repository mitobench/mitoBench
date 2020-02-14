package view.menus;

import Logging.LogClass;
import analysis.HaplotypeCaller;
import controller.*;
import guru.nidi.graphviz.engine.Format;
import io.dialogues.Export.SaveAsDialogue;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.MitoBenchWindow;
import view.dialogues.settings.HGlistProfilePlot;
import view.dialogues.settings.PieChartSettingsDialogue;
import view.visualizations.*;
import view.dialogues.information.InformationDialogue;
import view.dialogues.settings.SettingsDialogueStackedBarchart;
import controller.TableControllerUserBench;

import java.io.*;
import java.net.MalformedURLException;
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
    private TreeView treeView;
    private ProfilePlot profilePlot;
    private PieChartViz pieChartViz;
    private ColorSchemeStackedBarChart colorScheme;

    private TabPane tabPane;
    private TabPane statsTabpane;
    private Menu menuGraphics;

    private LogClass logClass;

    private static final int MIN_PIXELS = 10;
    private SampleTree sampleTree;


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

        treeView = treeController.getTree().getTree();
        statsTabpane = mitoBenchWindow.getTabpane_statistics();
        treeView = visualizationController.getTreeView();


        addSubMenus();
    }



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
        plotHGfreqHist.setOnAction(t -> {
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
                    String[][] cols = chartController.prepareColumnsAsList(new String[]{"Haplogroup"}, tableController.getSelectedRows());
                    selection_haplogroups = cols[0];
                    selection_groups = new String[]{"All data"};
                } else {
                    String[][] cols = chartController.prepareColumns(new String[]{"Haplogroup", "Grouping"}, tableController.getSelectedRows());
                    selection_haplogroups = cols[0];
                    selection_groups = cols[1];
                }
                if(selection_haplogroups.length != 0){

                    SettingsDialogueStackedBarchart advancedStackedBarchartDialogue =
                            new SettingsDialogueStackedBarchart("Advanced Stacked Barchart Settings", selection_groups,
                                    logClass);
                    advancedStackedBarchartDialogue.init(mito);

                    // add dialog to statsTabPane
                    Tab tab = advancedStackedBarchartDialogue.getTab();
                    mito.getTabpane_visualization().getTabs().add(tab);
                    mito.getTabpane_visualization().getSelectionModel().select(tab);

                    advancedStackedBarchartDialogue.getOkBtn().setOnAction(e -> {

                        visualizationController.initStackedBarchart(this);

                        stackedBar = visualizationController.getStackedBar();
                        advancedStackedBarchartDialogue.calculateTrimmedHGList();
                        chartController.addDataStackedBarChart(
                                stackedBar,
                                selection_haplogroups,
                                advancedStackedBarchartDialogue.getStackOrder(),
                                advancedStackedBarchartDialogue.getHg_list_trimmed()
                        );

                        advancedStackedBarchartDialogue.calculateTrimmedHGList();
                        String[] hg_list = advancedStackedBarchartDialogue.getHg_list_trimmed();

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
                            stackedBar.addListener();
                        }

                        //advancedStackedBarchartDialogue.close();
                        // remove tab from tabpane
                        mito.getTabpane_visualization().getTabs().remove(tab);
                    });

                } else {
                    InformationDialogue groupingWarningDialogue = new InformationDialogue(
                            "No haplogroups",
                            "Please determine haplogroups first.",
                            null,
                            "hgWarning");
                    }


            } else {
                InformationDialogue groupingWarningDialogue = new InformationDialogue(
                        "Empty Table",
                        "Please add data first.",
                        null,
                        "dataWarning");
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
                if(//tableController.getTableColumnByName("Grouping") != null &&
                      tableController.getTableColumnByName("Haplogroup") != null
                        && tableController.getTable().getItems().size() != 0 ){


                    HGlistProfilePlot hGlistProfilePlot = new HGlistProfilePlot("Profile plot configuration", logClass);
                    hGlistProfilePlot.init(mito);
                    // add dialog to statsTabPane
                    Tab tab = hGlistProfilePlot.getTab();
                    mito.getTabpane_visualization().getTabs().add(tab);
                    mito.getTabpane_visualization().getSelectionModel().select(tab);

                    hGlistProfilePlot.getOkBtn().setOnAction(e -> {
                        visualizationController.initProfilePlot();
                        profilePlot = visualizationController.getProfilePlot();
                        hGlistProfilePlot.calculateTrimmedHGList();
                        profilePlot.create(tableController, chartController, logClass, statsTabpane, hGlistProfilePlot.getHg_list_trimmed());

                        // remove tab from tabpane
                        mito.getTabpane_visualization().getTabs().remove(tab);


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
                            new PieChartSettingsDialogue("Advanced Piechart Settings", logClass);
                    pieChartSettingsDialogue.init(mito);

                    // add dialog to statsTabPane
                    Tab tab = pieChartSettingsDialogue.getTab();
                    mito.getTabpane_visualization().getTabs().add(tab);
                    mito.getTabpane_visualization().getSelectionModel().select(tab);


                    pieChartSettingsDialogue.getOkBtn().setOnAction(e -> {

                        visualizationController.getGrid_piecharts().getChildren().clear();
                        pieChartSettingsDialogue.calculateTrimmedHGList();
                        String[] hg_list_trimmed = pieChartSettingsDialogue.getHg_list_trimmed();

                        if(tableController.getTableColumnByName("Grouping") != null){
                            // get selected rows
                            String[][] cols = chartController.prepareColumns(new String[]{"Haplogroup", "Grouping"},
                                    tableController.getSelectedRows());
                            String[] selection_haplogroups = cols[0];
                            String[] selection_groups = cols[1];


                            HashMap<String, ArrayList> hgs_summed = chartController.summarizeHaplogroups(Arrays.asList(selection_haplogroups),
                                    hg_list_trimmed);
                            HashMap<String, List<XYChart.Data<String, Number>>> data_all =
                                    chartController.assignHGs(hgs_summed, selection_haplogroups, selection_groups);

                            int max_number_cols = (int) Math.round(Math.sqrt(groupController.getGroupnames().size()));

                            int curr_col = 0;
                            int curr_row = 0;

                            for(String group : groupController.getGroupnames()) {
                                if(!group.equals("")){

                                    visualizationController.initPieChart(group, curr_row, curr_col);

                                    pieChartViz = visualizationController.getPieChartViz();
                                    pieChartViz.createPlot(group, data_all);
                                    pieChartViz.setColor(stage);

                                    if(curr_col < max_number_cols){
                                        curr_col++;
                                    } else {
                                        curr_col=0;
                                        curr_row++;
                                    }
                                }
                            }
                            visualizationController.visualizePiechart();
                        } else {

                            if(tableController.getTableColumnByName("Haplogroup") != null){
                                String[][] cols = chartController.prepareColumns(new String[]{"Haplogroup"},
                                        tableController.getSelectedRows());
                                String[] selection_haplogroups = cols[0];

                                HashMap<String, ArrayList> hgs_summed = chartController.summarizeHaplogroups(Arrays.asList(selection_haplogroups),
                                        hg_list_trimmed);


                                visualizationController.initPieChart("Haplogroup frequency (all data)", 0, 0);

                                pieChartViz = visualizationController.getPieChartViz();
                                pieChartViz.createPlotSingle(hgs_summed);
                                pieChartViz.setColor(stage);
                                visualizationController.visualizePiechart();
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

                    mito.getProgressBarhandler().activate(task);

                    task.setOnCancelled((EventHandler<Event>) event -> {
                        haplotypeCaller.deleteTmpFiles();
                        mito.getProgressBarhandler().stop();
                    });

                    task.setOnSucceeded((EventHandler<Event>) event -> {
                        Tab sampleTree_tab = new Tab("Sample tree");

                        sampleTree = new SampleTree("","", mito.getLogClass());

                        // read graphviz file
                        BorderPane tab_content = new BorderPane();
                        HBox bottom_content = new HBox();
                        bottom_content.setPadding(new Insets(10,10,10,10));

                        Button save_as_svg = new Button("Save as SVG");
                        save_as_svg.setOnAction(e -> {
                            FileChooser.ExtensionFilter fex = new FileChooser.ExtensionFilter("SVG format (*.svg)", "*.svg");
                            SaveAsDialogue saveAsDialogue = new SaveAsDialogue(fex);
                            saveAsDialogue.start(mito.getPrimaryStage());

                            String svgfile = saveAsDialogue.getOutFile();
                            if(!svgfile.endsWith(".svg") && !svgfile.endsWith(".SVG"))
                                svgfile += ".svg";


                            try {
                                sampleTree.start(svgfile);
                                //sampleTree.getViz().render(Format.SVG).toFile(new File(svgfile));
                                mito.getTabpane_visualization().getTabs().remove(sampleTree_tab);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                        });

                        ScrollPane scrollPane_samples_tree = new ScrollPane();

                        bottom_content.getChildren().addAll(save_as_svg);
                        scrollPane_samples_tree.setContent(bottom_content);

                        // set contents
                        tab_content.setCenter(scrollPane_samples_tree);
                        tab_content.setBottom(bottom_content);
                        sampleTree_tab.setContent(tab_content);
                        mito.getTabpane_visualization().getTabs().add(sampleTree_tab);
                        mito.getTabpane_visualization().getSelectionModel().select(sampleTree_tab);

                        haplotypeCaller.deleteTmpFiles();
                        mito.getProgressBarhandler().stop();

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
            // todo
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
        haplo_graphics.getItems().addAll(barchart, profilePlotItem, pieChart,samples_haplo_tree);
        maps.getItems().add(mapsItem);
        //options.getItems().addAll(showTickLabels, clearPlotBox);
        options.getItems().addAll(clearPlotBox);

        menuGraphics.getItems().addAll(haplo_graphics, grouping_graphics, maps, new SeparatorMenuItem(), options);
    }

    public void createHaploBarchart(TableColumn haplo_col, List<String> columnData ) {
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
