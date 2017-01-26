package view.charts;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neukamm on 26.01.17.
 */
public class ProfilePlot {

    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    LineChart<String,Number> profilePlot =
            new LineChart<String,Number>(xAxis,yAxis);
    List<XYChart.Series> seriesList = new ArrayList<>();
    private final Glow glow = new Glow(.5);
    int maxVal=0;


    public ProfilePlot(){

    }

    public void createPlot(String title, String lable_xaxis, String label_yaxis){
        xAxis.setLabel(lable_xaxis);
        yAxis.setLabel(label_yaxis);
        yAxis.setAutoRanging(false);
        //yAxis.setLowerBound(1000);
        //yAxis.setUpperBound(100);
        yAxis.setTickUnit(5);
        yAxis.setMinorTickVisible(false);

        profilePlot.setTitle(title);

    }

    public LineChart<String,Number>  getPlot(){
        return profilePlot;
    }


    /**
     * This method add all previously creates series to chart.
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


    public void addListener(){

        for (XYChart.Series<String,Number> serie: profilePlot.getData()){
            for (XYChart.Data<String, Number> item: serie.getData()) {
                Node n = item.getNode();
                n.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        ((Node)(e.getSource())).setCursor(Cursor.HAND);
                    }
                });
            }
        }

    }


    private void setOnMouseEventsOnSeries(Node node,
                                          final LineChart chart, final String label) {

        node.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                chart.setTitle(label);
            }
        });

    }

}
