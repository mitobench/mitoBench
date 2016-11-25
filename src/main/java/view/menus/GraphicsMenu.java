package view.menus;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.charts.BarPlotHaplo;
import view.table.TableController;
import view.table.TableSelectionFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neukamm on 23.11.16.
 */
public class GraphicsMenu {


    private Menu menuGraphics;
    private TableController tableController;
    private BarPlotHaplo barPlotHaplo;

    public GraphicsMenu(TableController tableController, BarPlotHaplo barPlotHaplo){
        menuGraphics = new Menu("Graphics");
        this.tableController = tableController;
        this.barPlotHaplo = barPlotHaplo;
        addSubMenus();
    }


    private void addSubMenus() {


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
                    TableSelectionFilter tableFilter = new TableSelectionFilter();

                    barPlotHaplo.clearData();

                    if (seletcion_haplogroups.length !=0) {
                        tableFilter.haplogroupFilter(tableController, seletcion_haplogroups, tableController.getColIndex("Haplogroup"));
                        barPlotHaplo.addData(tableController.getDataHist());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        menuGraphics.getItems().addAll(plotHGfreq);
    }

    public Menu getMenuGraphics() {
        return menuGraphics;
    }
}
