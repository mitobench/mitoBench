package view.charts;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;

import java.util.List;

/**
 * Created by neukamm on 09.11.16.
 */
public abstract class ABarPlot {

    protected BarChart<String, Number> bc;


    public ABarPlot(String title, String xlabel, String ylabel){

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        bc = new BarChart<String, Number>(xAxis, yAxis);
        bc.setTitle(title);
        xAxis.setLabel(xlabel);
        yAxis.setLabel(ylabel);
    }


    public abstract void addData(String name, List<double[]> data);


    public BarChart<String,Number> getBarChart() {
        return bc;
    }


}
