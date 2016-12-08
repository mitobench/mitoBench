package view.charts;

import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import java.util.HashMap;

/**
 * Created by neukamm on 03.11.16.
 */
public class BarPlotHaplo extends ABarPlot {

    private XYChart.Series series;


    public BarPlotHaplo(String title, String ylabel, TabPane scene, Stage stage) {
        super(title, ylabel, scene, stage);
        this.bc.setLegendVisible(false);


    }


    @Override
    public void addData(HashMap<String, Integer> data) {

        series = new XYChart.Series();
        series.setName("");

        for (String haplo : data.keySet()) {
            series.getData().add(new XYChart.Data(haplo, data.get(haplo)));
        }

        this.bc.getData().clear();
        this.bc.getData().add(series);

    }


}

