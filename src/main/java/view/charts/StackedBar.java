package view.charts;

import io.Exceptions.ImageException;
import io.writer.ImageWriter;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.util.*;

/**
 * Created by neukamm on 29.11.16.
 */
public class StackedBar {

    private List< XYChart.Series<String, Number>> seriesList = new ArrayList<>();
    private StackedBarChart<String, Number> sbc;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;
    private TabPane tabPane;
    private Stage stage;

    public StackedBar(String title, TabPane vBox, Stage stage) {
        tabPane = vBox;
        this.stage = stage;

        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();

        xAxis.setTickMarkVisible(false);

        // set autoranging to false to allow manual settings
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(100);
        yAxis.setTickUnit(5);
        yAxis.setMinorTickVisible(false);
        yAxis.setLabel("Frequency in %");

        sbc = new StackedBarChart<>(xAxis, yAxis);
        sbc.setTitle(title);
        sbc.prefWidthProperty().bind(tabPane.widthProperty());
        sbc.setAnimated(false);
        sbc.setCategoryGap(20);
        sbc.setLegendSide(Side.RIGHT);

        setContextMenu(stage);

    }


    /**
     * This method adds data to the barplot as series
     *
     * @param data
     * @param name  name of the data set
     */
    public void addSeries(List<XYChart.Data<String, Number>> data, String name){
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        series.setName(name);

        for(int i = 0; i < data.size(); i++){
            series.getData().add(data.get(i));
        }

        this.seriesList.add(series);
    }


    /**
     * This method cleans up all data
     */
    public void clearData(){
        sbc.getData().clear();
        seriesList.clear();
        xAxis.getCategories().clear();
    }


    /**
     * This method adds a tooltip to the chart, which provides information such as the name of the Haplogroup and their
     * occurrences.
     *
     * @param
     */
    public void addTooltip(){

        for (final XYChart.Series<String, Number> series : sbc.getData()) {
            for (final XYChart.Data<String, Number> data : series.getData()) {
                Tooltip tooltip = new Tooltip();
                data.getNode().setOnMouseMoved(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        // +15 moves the tooltip 15 pixels below the mouse cursor;
                        tooltip.show(data.getNode(), event.getScreenX(), event.getScreenY() + 15);
                        tooltip.setText(series.getName() + " | " + data.getYValue().toString() + "%");
                    }
                });
                data.getNode().setOnMouseExited(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event){
                        tooltip.hide();
                    }
                });

            }
        }
    }



    /*

            GETTER and SETTER


     */

    /**
     * This method initializes a context menu to save chart as image.
     * @param stage
     */
    private void setContextMenu(Stage stage){


        //adding a context menu item to the chart
        final MenuItem saveAsPng = new MenuItem("Save as png");
        saveAsPng.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                ImageWriter imageWriter = new ImageWriter();
                try {
                    imageWriter.saveImage(stage, sbc.snapshot(new SnapshotParameters(), null));
                } catch (ImageException e) {
                    e.printStackTrace();
                }
            }
        });

        final ContextMenu menu = new ContextMenu(
                saveAsPng
        );

        sbc.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                if (MouseButton.SECONDARY.equals(event.getButton())) {
                    menu.show(tabPane, event.getScreenX(), event.getScreenY());
                }
            }
        });



    }

    public void setCategories(String[] groups){
        xAxis.setCategories(FXCollections.observableArrayList(groups));
    }

    public List<XYChart.Series<String, Number>> getSeriesList() {
        return seriesList;
    }

    public StackedBarChart<String, Number> getSbc() {
        return sbc;
    }



}
