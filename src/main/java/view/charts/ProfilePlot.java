package view.charts;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neukamm on 26.01.17.
 */
public class ProfilePlot {

    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    LineChart<String,Number> profilePlot = new LineChart<String,Number>(xAxis,yAxis);
    List<XYChart.Series> seriesList = new ArrayList<>();
    int maxVal = 0;


    public ProfilePlot(){

    }

    /**
     * This method initializes the line chart, sets title and axes labels
     * and sets values on y axis to integer with tick unit 5.
     *
     * @param title
     * @param lable_xaxis
     * @param label_yaxis
     */
    public void createPlot(String title, String lable_xaxis, String label_yaxis){
        xAxis.setLabel(lable_xaxis);
        yAxis.setLabel(label_yaxis);
        yAxis.setAutoRanging(false);
        yAxis.setTickUnit(5);
        yAxis.setMinorTickVisible(false);

        profilePlot.setTitle(title);

    }


    /**
     * This method add all previously creates series to chart.
     *
     * @param hgs
     * @param data
     * @param name
     */
    public void addSeries(List<String> hgs, List<XYChart.Data<String, Number>> data, String name){
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        series.setName(name);

        for(int i = 0; i < hgs.size(); i++){
            series.getData().add(new XYChart.Data(hgs.get(i), data.get(i).getYValue().doubleValue()));
            if(data.get(i).getYValue().doubleValue() > maxVal)
                maxVal = (int)data.get(i).getYValue().doubleValue();

        }

        this.seriesList.add(series);
    }


    /**
     * This method adds a css file to the profile plot to set the line with to 2px.
     */
    public void addListener(){
        File f = new File("src/main/java/view/charts/css/ProfilePlot.css");
        profilePlot.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
        profilePlot.setCreateSymbols(false);
        profilePlot.getStyleClass().add("thick-chart");

    }


    /*
            Getter and Setter
     */

    public LineChart<String,Number>  getPlot(){
        return profilePlot;
    }



    /**
     * This method returns all series as list.
     * @return
     */
    public List<XYChart.Series> getSeriesList() {
        //addListener();
        for(int i = 1; i < 6; i++){
            if((maxVal+i)%5 == 0){
                yAxis.setUpperBound(maxVal+i);
                break;
            }
        }
        return seriesList;
    }



}
