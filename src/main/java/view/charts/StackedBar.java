package view.charts;

import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by neukamm on 29.11.16.
 */
public class StackedBar{

    private List< XYChart.Series<String, Number>> seriesList = new ArrayList<>();
    private StackedBarChart<String, Number> sbc;
    private CategoryAxis xAxis;

    public StackedBar(String title, VBox vBox) {
        xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        sbc = new StackedBarChart<String, Number>(xAxis, yAxis);

        sbc.setTitle(title);
        sbc.prefWidthProperty().bind(vBox.widthProperty());



    }

    public void addSerie( List<XYChart.Data<String, Number>> data, String name){
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        series.setName(name);

        for(int i = 0; i < data.size(); i++){
            series.getData().add(data.get(i));
        }

        this.seriesList.add(series);
        this.sbc.getData().addAll(seriesList);
    }



    public void clearData(){

        sbc.getData().clear();

        for (XYChart.Series<String, Number> series : sbc.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                Node node = data.getNode();
                Parent parent = node.parentProperty().get();
                if (parent != null && parent instanceof Group) {
                    Group group = (Group) parent;
                    group.getChildren().clear();
                }
            }
        }}



    public void setCategories(HashSet<String> groups){
        xAxis.setCategories(FXCollections.observableArrayList(groups));
    }


    public List<XYChart.Series<String, Number>> getSeriesList() {
        return seriesList;
    }

    public StackedBarChart<String, Number> getSbc() {
        return sbc;
    }

    public CategoryAxis getxAxis() {
        return xAxis;
    }
}
