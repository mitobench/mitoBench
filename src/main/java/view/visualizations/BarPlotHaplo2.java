package view.visualizations;

import Logging.LogClass;
import controller.ChartController;
import controller.TableControllerUserBench;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BarPlotHaplo2 extends AChart {

    private final TableControllerUserBench tableController;
    private final Stage stage;
    private XYChart.Series series;
    private BarChartExt<String, Number> bc;


    public BarPlotHaplo2(String title, String ylabel, String xLabel, Stage stage,
                         TableControllerUserBench tc, TabPane tabPane, LogClass logClass) throws MalformedURLException {

        super(xLabel, ylabel, logClass);

        this.stage = stage;

        bc = new BarChartExt<>(xAxis, yAxis);
        bc.setLegendVisible(false);
        bc.setTitle(title);

        tableController = tc;

        URL url = this.getClass().getResource("/css/ColorsBarchart.css");
        stage.getScene().getStylesheets().add(url.toExternalForm());

        TableColumn haplo_col = tableController.getTableColumnByName("Haplogroup");
        List<String> columnDataHG = new ArrayList<>();
        tableController.getTable().getItems().stream().forEach((o)
                -> columnDataHG.add((String)haplo_col.getCellData(o)));

        setContextMenu(bc, tabPane);


    }

    public void addData(HashMap<Integer, List<String>> data) throws MalformedURLException {

        series = new XYChart.Series();
        series.setName("");

        for( Integer count : data.keySet()) {

            XYChart.Data<String, Number> d = new XYChart.Data(count+"", data.get(count).size());

            series.getData().add(d);
        }

        this.bc.getData().clear();
        this.bc.getData().add(series);

        for (XYChart.Series<String, Number> s : bc.getData()) {
            for (XYChart.Data d : s.getData()) {
                int key = Integer.parseInt(d.getXValue().toString());
                List<String> hgs = data.get(key);
                String hgs_string = "";
                int i = 0;
                int add = 20;
                while( i < hgs.size()){
                    if(i+add > hgs.size()){
                        hgs_string += hgs.subList(i, hgs.size());
                    } else {
                        hgs_string += hgs.subList(i, i+add) + "\n";
                    }

                    i = i+add;
                }
                Tooltip tooltip = new Tooltip("Haplogroups:" + hgs_string);

                hackTooltipStartTiming(tooltip);
                Tooltip.install(d.getNode(), tooltip);

                //Adding class on hover
                d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHover"));

                //Removing class on exit
                d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHover"));
            }
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


    public static void hackTooltipStartTiming(Tooltip tooltip) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(250)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}



