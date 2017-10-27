package view.visualizations;

import Logging.LogClass;
import controller.ChartController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import controller.TableControllerUserBench;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by neukamm on 03.11.16.
 */
public class BarPlotHaplo extends AChart {

    private final TableControllerUserBench tableController;
    private final Stage stage;
    private XYChart.Series series;
    private BarChartExt<String, Number> bc;


    public BarPlotHaplo(String title, String ylabel, Stage stage,
                        TableControllerUserBench tc, TabPane tabPane, LogClass logClass) throws MalformedURLException {

        super("", ylabel, logClass);

        this.stage = stage;

        bc = new BarChartExt<String, Number>(xAxis, yAxis);
        bc.setLegendVisible(false);
        bc.setTitle(title);

        tableController = tc;

        URL url = this.getClass().getResource("/css/ColorsBarchart.css");
        stage.getScene().getStylesheets().add(url.toExternalForm());

        TableColumn haplo_col = tableController.getTableColumnByName("Haplogroup");
        List<String> columnDataHG = new ArrayList<>();
        tableController.getTable().getItems().stream().forEach((o)
                -> columnDataHG.add((String)haplo_col.getCellData(o)));

        setContextMenu(bc, tabPane);


    }

    public void addData(HashMap<String, Integer> data) throws MalformedURLException {

        series = new XYChart.Series();
        series.setName("");

        for (String haplo : data.keySet()) {
            XYChart.Data<String, Number> d = new XYChart.Data(haplo, data.get(haplo).intValue());
            series.getData().add(d);
        }

        this.bc.getData().clear();
        this.bc.getData().add(series);

    }



    /**
     * This method returns the BarChart object
     * @return
     */
    public BarChart<String,Number> getBarChart() {
        return bc;
    }

    /**
     * This method removes all data.
     */
    public void clearData(){

        for (XYChart.Series<String, Number> series : bc.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                Node node = data.getNode();
                Parent parent = node.parentProperty().get();
                if (parent != null && parent instanceof Group) {
                    Group group = (Group) parent;
                    group.getChildren().clear();
                }
            }
        }
    }

}

