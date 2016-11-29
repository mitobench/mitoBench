package view.menus;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.VBox;
import view.charts.BarPlotHaplo;
import view.charts.StackedBar;
import view.table.TableController;
import view.table.TableSelectionFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by neukamm on 23.11.16.
 */
public class GraphicsMenu {


    private Menu menuGraphics;
    private TableController tableController;
    private BarPlotHaplo barPlotHaplo;
    private StackedBar stackedBar;

    public GraphicsMenu(TableController tableController, VBox vBox){
        menuGraphics = new Menu("Graphics");
        this.tableController = tableController;
        this.barPlotHaplo = new BarPlotHaplo("Haplogroup frequency", "Frequency", vBox);
        this.stackedBar = new StackedBar("Haplogroup frequency per group", vBox);
        addSubMenus();
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

                    TableColumn haplo_col = tableController.getTableColumnByName("Haplogroup");
                    TableColumn grouping_col = tableController.getTableColumnByName("Grouping");


                    List<String> columnDataHG = new ArrayList<>();
                    List<String> columnDataGroup = new ArrayList<>();
                    for (Object item : tableController.getTable().getItems()) {
                        columnDataHG.add((String)haplo_col.getCellObservableValue(item).getValue());
                        columnDataGroup.add((String)grouping_col.getCellObservableValue(item).getValue());
                    }
                    String[] seletcion_haplogroups = columnDataHG.toArray(new String[columnDataHG.size()]);
                    String[] seletcion_groups = columnDataGroup.toArray(new String[columnDataHG.size()]);


                    // parse selection to tablefilter
                    TableSelectionFilter tableFilter = new TableSelectionFilter();

                    stackedBar.clearData();

                    stackedBar.setCategories(new HashSet<String>(Arrays.asList(seletcion_groups)));

                    if (seletcion_haplogroups.length != 0) {
                        tableFilter.haplogroupFilter(tableController, seletcion_haplogroups, tableController.getColIndex("Haplogroup"));
                        for(int i = 0; i < seletcion_haplogroups.length; i++){
                            List< XYChart.Data<String, Number> > data_list = new ArrayList<XYChart.Data<String, Number>>();
                            XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(seletcion_groups[i], 5);
                            data_list.add(data);
                            stackedBar.addSerie(data_list, seletcion_haplogroups[i]);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        barchart.getItems().addAll(plotHGfreq, plotHGfreqGroup);

        menuGraphics.getItems().add(barchart);
    }

    public Menu getMenuGraphics() {
        return menuGraphics;
    }
}
