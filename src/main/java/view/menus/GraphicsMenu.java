package view.menus;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.charts.*;
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
    private TabPane tabPane;
    private HashMap<String, List<String>> treeMap_path_to_root;
    private TreeItem<String> tree_root;
    private TreeView treeView;
    private Stage stage;
    private ChartController chartController;


    public GraphicsMenu(TableController tableController, TabPane vBox, TreeHaploController treeController, Stage stage){
        menuGraphics = new Menu("Graphics");
        this.tableController = tableController;
        tabPane = vBox;
        treeMap_path_to_root = treeController.getTreeMap_leaf_to_root();
        tree_root = treeController.deepcopy(treeController.getTree().getTree().getRoot());
        treeView = treeController.getTree().getTree();
        this.stage = stage;
        this.chartController = new ChartController(tableController, treeController.getTreeMap());
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
        sunburstChart = new SunburstChart(new BorderPane(), stage, tabPane, tableController);
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

                    if(tableController.getTable().getItems().size() != 0 ){
                        initBarchart();

                        TableColumn haplo_col = tableController.getTableColumnByName("Haplogroup");
                        List<String> columnData = new ArrayList<>();

                        chartController.addDataBarChart(barPlotHaplo, columnData, haplo_col);
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
                        stackedBar.addTooltip();

                        ColorScheme colorScheme = new ColorScheme(stage);
                        colorScheme.setNewColors(stackedBar, selection_haplogroups);

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


        barchart.getItems().addAll(plotHGfreq, plotHGfreqGroup);

        menuGraphics.getItems().addAll(barchart, sunburstChartItem, new SeparatorMenuItem(), clearPlotBox);
    }



    public Menu getMenuGraphics() {
        return menuGraphics;
    }




}
