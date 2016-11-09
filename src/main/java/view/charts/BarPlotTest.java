package view.charts;

import javafx.scene.chart.XYChart;

import java.util.List;

/**
 * Created by neukamm on 03.11.16.
 */
public class BarPlotTest extends ABarPlot {


    public BarPlotTest(String title, String xlabel, String ylabel) {
        super(title, xlabel, ylabel);
    }

    @Override
    public void addData(String name, List<double[]> data) {
        XYChart.Series series = new XYChart.Series();

        series.setName(name);

        for(double[] d : data){
            series.getData().add(new XYChart.Data(String.valueOf(d[0]), d[1]));
        }

        this.bc.getData().add(series);
    }



}
