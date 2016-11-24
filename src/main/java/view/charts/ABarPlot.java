package view.charts;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.util.HashMap;
import java.util.List;

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
        yAxis.setTickUnit(3);
        yAxis.setMinorTickVisible(false);

        bc.prefWidthProperty().bind(scene.widthProperty());

    }


    public abstract void addData(HashMap<String, Integer> dataNew);

    public BarChart<String,Number> getBarChart() {
        return bc;
    }

    public void clearData(){this.bc.getData().clear();}




}
