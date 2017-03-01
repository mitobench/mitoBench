package view.charts;

import Logging.LogClass;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by neukamm on 12.01.17.
 */
public class BarChartGrouping extends AChart {

    private XYChart.Series series;
    private BarChartExt<String, Number> bc;
    /**
     * Constructor which sets axes, title and context menu
     *  @param title
     * @param ylabel
     * @param scene
     * @param logClass
     */
    public BarChartGrouping(String title, String ylabel, TabPane scene, LogClass logClass) {
        super(ylabel, null, logClass);
        bc = new BarChartExt<String, Number>(xAxis, yAxis);
        bc.setLegendVisible(false);
        bc.setTitle(title);

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

        setContextMenu(bc, scene);
    }

    public void addData(HashMap<String, Integer> data) {
        this.series = new XYChart.Series();
        series.setName("");

        for(String group : data.keySet()){
            XYChart.Data data_node = new XYChart.Data<>(group, data.get(group));
            series.getData().add(data_node);
        }
        this.bc.getData().clear();
        this.bc.getData().add(series);
    }

    /**
     * This method set specified colors to bar chart
     * @param stage
     * @throws MalformedURLException
     */
    public void setColor(Stage stage) throws MalformedURLException {

        File file = new File("src/main/java/view/charts/css/ColorsBarchart.css");
        URL url = file.toURI().toURL();
        //stage.getScene().getStylesheets().clear();
        stage.getScene().getStylesheets().add(url.toExternalForm());

        for (Node node : this.bc.lookupAll(".series")) {
            node.getStyleClass().remove("default-color0");
        }
    }

    /**
     * This method returns the BarChart object
     * @return
     */
    public BarChart<String,Number> getBarChart() {
        return bc;
    }


    /**
     * This method removes all data.
     */
    public void clearData(){

        for (XYChart.Series<String, Number> series : bc.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                Node node = data.getNode();
                Parent parent = node.parentProperty().get();
                if (parent != null && parent instanceof Group) {
                    Group group = (Group) parent;
                    group.getChildren().clear();
                }
            }
        }
    }

}
