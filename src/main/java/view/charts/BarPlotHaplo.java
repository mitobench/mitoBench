package view.charts;

import javafx.scene.chart.XYChart;

import java.util.HashMap;

/**
 * Created by neukamm on 03.11.16.
 */
public class BarPlotHaplo extends ABarPlot {

    private XYChart.Series series;


    public BarPlotHaplo(String title, String xlabel, String ylabel) {
        super(title, xlabel, ylabel);

    }

    @Override
    public void addData(String name, HashMap<String, Integer> data) {

        series = new XYChart.Series();
        series.setName(name);

        for(String haplo : data.keySet()){
            series.getData().add(new XYChart.Data(haplo, data.get(haplo)));
        }

        this.bc.getData().clear();
        this.bc.getData().add(series);

    }



}
