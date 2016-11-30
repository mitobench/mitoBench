package view.menus;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import view.charts.BarPlotHaplo;
import view.charts.StackedBar;
import view.charts.SunburstChart;
import view.table.TableController;
import view.table.TableSelectionFilter;

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
    private StackedBar stackedBar;
    private SunburstChart sunburstChart;
    private VBox chartbox;
    private BorderPane sunburstBorderPane;

    public GraphicsMenu(TableController tableController, VBox vBox){
        menuGraphics = new Menu("Graphics");
        this.tableController = tableController;
        chartbox = vBox;
        sunburstBorderPane = new BorderPane();

        addSubMenus();
    }

    private void initBarchart(){
        this.barPlotHaplo = new BarPlotHaplo("Haplogroup frequency", "Frequency", chartbox);
        chartbox.getChildren().add(barPlotHaplo.getBarChart());
    }

    private void initStackedBarchart(){
        this.stackedBar = new StackedBar("Haplogroup frequency per group", chartbox);
        chartbox.getChildren().add(stackedBar.getSbc());

    }

    private void initSunburst(HashMap<String, List<String>> hg_to_group){
        this.sunburstChart = new SunburstChart(sunburstBorderPane, hg_to_group);
        chartbox.getChildren().add(sunburstChart.getBorderPane());

    }

    public void clearCharts(){
        stackedBar = null;
        barPlotHaplo = null;
        chartbox.getChildren().clear();
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


                    if(barPlotHaplo==null)
                        initBarchart();

                    TableColumn haplo_col = tableController.getTableColumnByName("Haplogroup");
                    List<String> columnData = new ArrayList<>();
                    for (Object item : tableController.getTable().getItems()) {
                        columnData.add((String)haplo_col.getCellObservableValue(item).getValue());
                    }
                    String[] seletcion_haplogroups = columnData.toArray(new String[columnData.size()]);


                    // parse selection to tablefilter
                    //TableSelectionFilter tableFilter = new TableSelectionFilter();

                    barPlotHaplo.clearData();

                    if (seletcion_haplogroups.length !=0) {
                        //tableFilter.haplogroupFilter(tableController, seletcion_haplogroups, tableController.getColIndex("Haplogroup"));
                        barPlotHaplo.addData(tableController.getDataHist());

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /*

                    Plot Hg frequency for each group

         */

        MenuItem plotHGfreqGroup = new MenuItem("Plot haplogroup frequency per group(StackedBarchart)");
        plotHGfreqGroup.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    if(stackedBar==null)
                        initStackedBarchart();

                    TableColumn haplo_col = tableController.getTableColumnByName("Haplogroup");
                    TableColumn grouping_col = tableController.getTableColumnByName("Grouping");


                    List<String> columnDataHG = new ArrayList<>();
                    List<String> columnDataGroup = new ArrayList<>();

                    for (Object item : tableController.getTable().getItems()) {
                        columnDataHG.add((String)haplo_col.getCellObservableValue(item).getValue());
                        columnDataGroup.add((String)grouping_col.getCellObservableValue(item).getValue());
                    }
                    String[] seletcion_haplogroups = tableController.removeDuplicates(columnDataHG.toArray(new String[columnDataHG.size()]));
                    String[] seletcion_groups = tableController.removeDuplicates(columnDataGroup.toArray(new String[columnDataGroup.size()]));


                    // parse selection to tablefilter
                    TableSelectionFilter tableFilter = new TableSelectionFilter();

                    stackedBar.clearData();

                    stackedBar.setCategories(seletcion_groups);

                    if (seletcion_haplogroups.length != 0) {
                        tableFilter.haplogroupFilter(tableController, seletcion_haplogroups, tableController.getColIndex("Haplogroup"));
                        for(int i = 0; i < seletcion_haplogroups.length; i++){

                            List< XYChart.Data<String, Number> > data_list = new ArrayList<XYChart.Data<String, Number>>();
                            // fill data_list : <group(i), countHG >
                            for(int j = 0; j < seletcion_groups.length; j++){

                                int count_per_HG = tableController.getCountPerHG(seletcion_haplogroups[i], seletcion_groups[j], tableController.getColIndex("Haplogroup"),
                                                                 tableController.getColIndex("Grouping"));
                                XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(seletcion_groups[j], count_per_HG);
                                data_list.add(data);

                            }

                            stackedBar.addSerie(data_list, seletcion_haplogroups[i]);
                        }

                    }

                    stackedBar.getSbc().getData().addAll(stackedBar.getSeriesList());



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



         /*

                    Plot HG frequency for each group

         */

        MenuItem sunburstShartItem = new MenuItem("Create Sunburst chart");
        sunburstShartItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    HashMap<String, List<String>> hg_to_group = getHG_to_group();
                    initSunburst(hg_to_group);

                    //sunburstChart.clear();
                    //sunburstChart.addData();
                    //sunburstChart.finishSetup();

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

        menuGraphics.getItems().addAll(barchart, sunburstShartItem, new SeparatorMenuItem(), clearPlotBox);
    }

    public Menu getMenuGraphics() {
        return menuGraphics;
    }



    public HashMap<String, List<String>> getHG_to_group(){

        TableColumn haplo_col = tableController.getTableColumnByName("Haplogroup");
        TableColumn grouping_col = tableController.getTableColumnByName("Grouping");


        List<String> columnDataHG = new ArrayList<>();
        List<String> columnDataGroup = new ArrayList<>();

        for (Object item : tableController.getTable().getItems()) {
            columnDataHG.add((String)haplo_col.getCellObservableValue(item).getValue());
            columnDataGroup.add((String)grouping_col.getCellObservableValue(item).getValue());
        }
        String[] seletcion_haplogroups = tableController.removeDuplicates(columnDataHG.toArray(new String[columnDataHG.size()]));
        String[] seletcion_groups = tableController.removeDuplicates(columnDataGroup.toArray(new String[columnDataGroup.size()]));


        // parse selection to tablefilter
        TableSelectionFilter tableFilter = new TableSelectionFilter();
        HashMap<String, List<String>> hg_to_group = new HashMap<>();


        if (seletcion_haplogroups.length != 0) {

            tableFilter.haplogroupFilter(tableController, seletcion_haplogroups, tableController.getColIndex("Haplogroup"));


            // iteration over grouping
            for(int i = 0; i < seletcion_groups.length; i++){

                // create new hash entry for each group
                if(!hg_to_group.containsKey(seletcion_groups[i])){
                    hg_to_group.put(seletcion_groups[i], new ArrayList<>());
                }

                // add each haplogroup which occurs within this group
                ObservableList<ObservableList> selection = tableController.getTable().getItems();

                // iterate over all table view rows
                for(int k = 0; k < selection.size(); k++){

                    ObservableList list = selection.get(k);

                    if(list.get( tableController.getColIndex("Grouping")).equals(seletcion_groups[i])){
                        List<String> tmp = hg_to_group.get(seletcion_groups[i]);
                        tmp.add((String)list.get(tableController.getColIndex("Haplogroup")));
                        hg_to_group.put(seletcion_groups[i], tmp);

                    }

                }
            }




//                for(int j = 0; j < seletcion_haplogroups.length; j++){
//                    List<String> tmp = hg_to_group.get(seletcion_groups[i]);
//                    tmp.add(seletcion_haplogroups[j]);
//                    hg_to_group.put(seletcion_groups[i], tmp);
//
//                }
            }

        return hg_to_group;
    }
}
