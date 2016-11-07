package view.charts;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;

/**
 * Created by neukamm on 03.11.16.
 */
public class BarPlot {


    private BarChart<String, Number> bc;

    public BarPlot(String title, String xlabel, String ylabel){

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        bc = new BarChart<String, Number>(xAxis, yAxis);
        bc.setTitle(title);
        xAxis.setLabel(xlabel);
        yAxis.setLabel(ylabel);
    }


    public void addData(String name, List<XYChart.Data> data){
        XYChart.Series series = new XYChart.Series();
        series.setName(name);

        for(XYChart.Data d : data){
            series.getData().add(d);
        }

        bc.getData().add(series);


    }


    public BarChart<String,Number> getBarChart() {
        return bc;
    }

}
