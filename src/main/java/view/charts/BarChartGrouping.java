package view.charts;

import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by neukamm on 12.01.17.
 */
public class BarChartGrouping extends ABarPlot {

    private XYChart.Series series;
    /**
     * Constructor which sets axes, title and context menu
     *
     * @param title
     * @param ylabel
     * @param scene
     * @param stage
     */
    public BarChartGrouping(String title, String ylabel, TabPane scene, Stage stage) {
        super(title, ylabel, scene, stage);
        this.bc.setLegendVisible(false);
    }

    @Override
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

    public void setColor(Stage stage) throws MalformedURLException {

        File file = new File("src/main/java/view/charts/css/ColorsBarchart.css");
        URL url = file.toURI().toURL();
        stage.getScene().getStylesheets().clear();
        stage.getScene().getStylesheets().add(url.toExternalForm());

        for (Node node : this.bc.lookupAll(".series")) {
            node.getStyleClass().remove("default-color0");
        }

    }
}
