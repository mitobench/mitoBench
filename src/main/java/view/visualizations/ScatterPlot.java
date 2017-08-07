package view.visualizations;

import Logging.LogClass;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;

public class ScatterPlot extends AChart{

    private ScatterChart<Number, Number> sc;

    public ScatterPlot(Stage stage, LogClass logClass){
        super("", "",logClass);

        //URL url = this.getClass().getResource("/css/ColorsPCA.css");
        //stage.getScene().getStylesheets().add(url.toExternalForm());

    }


    public void create(double lowerbound_x, double lowerbound_y, double upperbound_x, double upperbound_y){
        final NumberAxis xAxis = new NumberAxis(lowerbound_x, upperbound_x, 1);
        final NumberAxis yAxis = new NumberAxis(lowerbound_y, upperbound_y, 1);
        sc = new ScatterChart<Number,Number>(xAxis,yAxis);
        xAxis.setLabel("PC 1");
        yAxis.setLabel("PC 2");
        sc.setTitle("PCA");

    }

    //public void addSeries(String name, ColorPicker colorPicker, double[] pc1, double[] pc2){
    public void addSeries(String name, Color color, double pc1, double pc2){
//        Color color = colorPicker.getValue();
        XYChart.Series series = new XYChart.Series();
        series.setName(name);

       // for( int i = 0; i < pc1.length; i++){
            series.getData().add(new XYChart.Data(pc1, pc2));
       // }

        sc.getData().add(series);
        series.getNode().setStyle("-fx-background-color:" + " " + color);

//        Node line = series.getNode().lookup(".chart-line-symbol");
//
//        String rgb = String.format("%d, %d, %d",
//                (int) (color.getRed() * 255),
//                (int) (color.getGreen() * 255),
//                (int) (color.getBlue() * 255));
//
//        line.setStyle("-fx-stroke: rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "); ");


    }

    public ScatterChart<Number, Number> getSc() {
        return sc;
    }




}
