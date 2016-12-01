package view.charts;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.util.HashMap;


/**
 * Created by neukamm on 09.11.16.
 */
public abstract class ABarPlot {

    protected BarChartExt<String, Number> bc;



    public ABarPlot(String title, String ylabel, TabPane scene){

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        bc = new BarChartExt<String, Number>(xAxis, yAxis);
        bc.setTitle(title);
        bc.setAnimated(false);
        yAxis.setLabel(ylabel);
        yAxis.setTickUnit(1);
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

    public void clearData(){

        this.bc.getData().clear();

        for (XYChart.Series<String, Number> series : bc.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                Node node = data.getNode();
                Parent parent = node.parentProperty().get();
                if (parent != null && parent instanceof Group) {
                    Group group = (Group) parent;
                    group.getChildren().clear();
                }
            }
        }}




}
