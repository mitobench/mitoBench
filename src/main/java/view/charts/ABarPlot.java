package view.charts;

import javafx.scene.chart.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.util.HashMap;


/**
 * Created by neukamm on 09.11.16.
 */
public abstract class ABarPlot {

    protected BarChart<String, Number> bc;



    public ABarPlot(String title, String xlabel, String ylabel, VBox scene){

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        //bc = new BarChart<String, Number>(xAxis, yAxis);
        bc = new BarChart(xAxis,yAxis);
        bc.setTitle(title);
        xAxis.setLabel(xlabel);
        yAxis.setLabel(ylabel);
        //yAxis.setTickUnit(3);
        yAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override public String toString(Number object) {
                if(object.intValue()!=object.doubleValue())
                    return "";

                return ""+(object.intValue());
            }

            @Override public Number fromString(String string) {
                Number val = Double.parseDouble(string);
                return val.intValue();
            }
        });


        yAxis.setMinorTickVisible(false);

        bc.prefWidthProperty().bind(scene.widthProperty());
        bc.autosize();

    }



    public abstract void addData(HashMap<String, Integer> dataNew);

    public BarChart<String,Number> getBarChart() {
        return bc;
    }

    public void clearData(){this.bc.getData().clear();}




}
