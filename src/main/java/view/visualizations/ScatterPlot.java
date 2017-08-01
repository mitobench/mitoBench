package view.visualizations;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;

public class ScatterPlot {

    private ScatterChart<Number, Number> sc;

    public ScatterPlot(){

    }


    public void create(int lowerbound_x, int lowerbound_y, int upperbound_x, int upperbound_y){
        final NumberAxis xAxis = new NumberAxis(lowerbound_x, upperbound_x, 1);
        final NumberAxis yAxis = new NumberAxis(lowerbound_y, upperbound_y, 100);
        sc = new ScatterChart<Number,Number>(xAxis,yAxis);
        xAxis.setLabel("PC 1");
        yAxis.setLabel("PC 2");
        sc.setTitle("PCA");

    }

    public void addSeries(String name, double[] pc1, double[] pc2){

        XYChart.Series series = new XYChart.Series();
        series.setName(name);
        for( int i = 0; i < pc1.length; i++){
            series.getData().add(new XYChart.Data(pc1[i], pc2[i]));
        }

        sc.getData().add(series);

    }

    public ScatterChart<Number, Number> getSc() {
        return sc;
    }




}
