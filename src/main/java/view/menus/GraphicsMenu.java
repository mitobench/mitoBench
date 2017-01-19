package view.menus;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.charts.*;
import view.table.TableController;
import view.tree.TreeHaploController;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by neukamm on 23.11.16.
 */
public class GraphicsMenu {


    private Menu menuGraphics;
    private TableController tableController;
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
    private Scene scene;


    public GraphicsMenu(TableController tableController, TabPane vBox, TreeHaploController treeController, Stage stage, Scene scene){
        menuGraphics = new Menu("Graphics");
        menuGraphics.setId("graphicsMenu");
        this.tableController = tableController;
        tabPane = vBox;
        treeMap_path_to_root = treeController.getTreeMap_leaf_to_root();
        tree_root = treeController.deepcopy(treeController.getTree().getTree().getRoot());
        treeView = treeController.getTree().getTree();
        this.stage = stage;
        this.chartController = new ChartController(tableController, treeController.getTreeMap());
        this.scene = scene;
        addSubMenus();
    }


    private void initHaploBarchart(){
        this.barPlotHaplo = new BarPlotHaplo("Haplogroup frequency", "Frequency", tabPane, stage);
        Tab tab = new Tab();
        tab.setText("Bar Chart haplogroups");
        tab.setContent(barPlotHaplo.getBarChart());
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

    }

    private void initGroupBarChart(){
        barChartGrouping = new BarChartGrouping("Group frequency", "Frequency", tabPane, stage);
        Tab tab = new Tab();
        tab.setText("Bar Chart Grouping");
        tab.setContent(barChartGrouping.getBarChart());
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
        sunburstChart = new SunburstChartCreator(new BorderPane(), stage, tabPane, scene);
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

        Menu haplo_graphics = new Menu("Haolpgroups");

        Menu barchart = new Menu("Create Barchart...");


        /*
                        Plot HG frequency

         */

        MenuItem plotHGfreq = new MenuItem("Plot haplogroup frequency");
        plotHGfreq.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {

                    if(tableController.getTable().getItems().size() != 0 ){
                        TableColumn haplo_col = tableController.getTableColumnByName("Haplogroup");

                        if(haplo_col!=null){
                            initHaploBarchart();
                            chartController.addDataBarChart(barPlotHaplo, haplo_col, "Haplogroup");
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

        plotHGfreqGroup.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    if(tableController.getTableColumnByName("Grouping") != null
                            && tableController.getTable().getItems().size()!=0) {

                        initStackedBarchart();

                        String[][] cols = chartController.prepareColumns(new String[]{"Haplogroup", "Grouping"}, tableController.getSelectedRows());
                        String[] selection_haplogroups = cols[0];
                        String[] selection_groups = cols[1];


                        chartController.addDataStackedBarChart(stackedBar, cols, selection_haplogroups, selection_groups);

                        stackedBar.getSbc().getData().addAll(stackedBar.getSeriesList());

                        // add settings
                        stackedBar.addListener();
                        stackedBar.addTooltip();

                        ColorScheme colorScheme = new ColorScheme(stage);
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

        Menu grouping_graphics = new Menu("Grouping");
        MenuItem grouping_barchart = new MenuItem("Grouping bar chart");
        grouping_barchart.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    if(tableController.getTable().getItems().size() != 0 ) {


                        TableColumn haplo_col = tableController.getTableColumnByName("Grouping");
                        if(haplo_col != null){
                            initGroupBarChart();
                            chartController.addDataBarChart(barChartGrouping, haplo_col, "Grouping");
                            barChartGrouping.setColor(stage);
                        }



                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        grouping_graphics.getItems().add(grouping_barchart);
        barchart.getItems().addAll(plotHGfreq, plotHGfreqGroup);
        haplo_graphics.getItems().addAll(barchart, sunburstChartItem);


        menuGraphics.getItems().addAll(haplo_graphics, grouping_graphics, new SeparatorMenuItem(), clearPlotBox);
    }



    public Menu getMenuGraphics() {
        return menuGraphics;
    }




}
