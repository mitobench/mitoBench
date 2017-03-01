package view.charts;

import Logging.LogClass;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import view.table.controller.TableControllerUserBench;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by neukamm on 03.11.16.
 */
public class BarPlotHaplo extends AChart {

    private final ChartController chartController;
    private final TableControllerUserBench tableController;
    private final HashMap<String, ArrayList> hgs_summed;
    private final Stage stage;
    private XYChart.Series series;
    private ColorSchemeBarchart colorSchemeBarchart;
    private BarChartExt<String, Number> bc;


    public BarPlotHaplo(String title, String ylabel, Stage stage, ChartController cc,
                        TableControllerUserBench tc, LogClass logClass) throws MalformedURLException {

        super(ylabel, "", logClass);

        this.stage = stage;

        bc = new BarChartExt<String, Number>(xAxis, yAxis);
        bc.setLegendVisible(false);

        chartController = cc;
        tableController = tc;


        File file = new File("src/main/java/view/charts/css/ColorsBarchart.css");
        URL url = file.toURI().toURL();
        //stage.getScene().getStylesheets().clear();
        stage.getScene().getStylesheets().add(url.toExternalForm());

        for (Node node : this.bc.lookupAll(".series")) {
            node.getStyleClass().remove("default-color0");
        }

        TableColumn haplo_col = tableController.getTableColumnByName("Haplogroup");
        List<String> columnDataHG = new ArrayList<>();
        tableController.getTable().getItems().stream().forEach((o)
                -> columnDataHG.add((String)haplo_col.getCellData(o)));
        hgs_summed = chartController.summarizeHaolpgroups(columnDataHG.stream().toArray(String[]::new), chartController.getCoreHGs());


    }

    public void addData(HashMap<String, Integer> data) throws MalformedURLException {

        series = new XYChart.Series();
        series.setName("");

        colorSchemeBarchart = new ColorSchemeBarchart(stage);
        for (String haplo : data.keySet()) {
            XYChart.Data<String, Number> d = new XYChart.Data(haplo, data.get(haplo).intValue());

            //series.getData().add(setColor(d, hgs_summed));
            series.getData().add(d);
        }

        this.bc.getData().clear();
        this.bc.getData().add(series);

    }

    private XYChart.Data setColor(XYChart.Data<String, Number> d, HashMap<String, ArrayList> hgs_summed) {

        d.nodeProperty().addListener(new ChangeListener<Node>() {
            @Override public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) {
                if (newNode != null) {

                    for(String key : hgs_summed.keySet()){
                        ArrayList hg_sub = hgs_summed.get(key);
                        if(hg_sub.contains(d.getXValue())){
                            newNode = colorSchemeBarchart.setColor(newNode, key);
                        }
                    }
                }
            }
        });

        return d;
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

