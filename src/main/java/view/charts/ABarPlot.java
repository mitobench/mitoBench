package view.charts;

import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 09.11.16.
 */
public abstract class ABarPlot {

    protected BarChart<String, Number> bc;


    public ABarPlot(String title, String xlabel, String ylabel, VBox scene){

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        bc = new BarChart<String, Number>(xAxis, yAxis);
        bc.setTitle(title);
        xAxis.setLabel(xlabel);
        yAxis.setLabel(ylabel);

        bc.prefWidthProperty().bind(scene.widthProperty());
        //bc.prefHeightProperty().bind(scene.heightProperty());


    }


    public abstract void addData(String name, HashMap<String, Integer> dataNew);


    public BarChart<String,Number> getBarChart() {
        return bc;
    }


}
