package view.charts;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.chart.*;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.util.*;

/**
 * Created by neukamm on 29.11.16.
 */
public class StackedBar{

    private List< XYChart.Series<String, Number>> seriesList = new ArrayList<>();
    private StackedBarChart<String, Number> sbc;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;
    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;
    private TabPane tabPane;

    public StackedBar(String title, TabPane vBox) {
        tabPane = vBox;

        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        sbc = new StackedBarChart<String, Number>(xAxis, yAxis);

        sbc.setTitle(title);
        sbc.prefWidthProperty().bind(tabPane.widthProperty());
        sbc.setAnimated(false);
        sbc.setCategoryGap(20);
        //sbc.setLegendVisible(true);

        //setDragAndMove();



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
        xAxis.setTickMarkVisible(false);


    }



    public void addSerie( List<XYChart.Data<String, Number>> data, String name){
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        series.setName(name);

        for(int i = 0; i < data.size(); i++){
            series.getData().add(data.get(i));
        }

        this.seriesList.add(series);
    }

    public void clearData(){
        //sbc = new StackedBarChart<String, Number>(xAxis, yAxis);

        sbc.getData().clear();
        seriesList.clear();
        xAxis.getCategories().clear();

//        for (XYChart.Series<String, Number> series : sbc.getData()) {
//            for (XYChart.Data<String, Number> view.data : series.getData()) {
//                Node node = view.data.getNode();
//                Parent parent = node.parentProperty().get();
//                if (parent != null && parent instanceof Group) {
//                    Group group = (Group) parent;
//                    group.getChildren().clear();
//                }
//            }
//        }
    }


    /**
     * This method cares about the drag and drop option of teh barplot
     *
     */

    public void setDragAndMove(){
        sbc.setCursor(Cursor.HAND);
        sbc.setOnMousePressed(circleOnMousePressedEventHandler);
        sbc.setOnMouseDragged(circleOnMouseDraggedEventHandler);

    }


    /**
     *
     * Definitions of mouse events
     *
     */

    EventHandler<MouseEvent> circleOnMousePressedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();
                    orgTranslateX = ((StackedBarChart)(t.getSource())).getTranslateX();
                    orgTranslateY = ((StackedBarChart)(t.getSource())).getTranslateY();
                }
            };

    EventHandler<MouseEvent> circleOnMouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;

                    ((StackedBarChart)(t.getSource())).setTranslateX(newTranslateX);
                    ((StackedBarChart)(t.getSource())).setTranslateY(newTranslateY);
                }
            };


    public void setCategories(String[] groups){
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

    public void addTooltip(Event t){

        for (final XYChart.Series<String, Number> series : sbc.getData()) {
            for (final XYChart.Data<String, Number> data : series.getData()) {
                Tooltip tooltip = new Tooltip();
                data.getNode().setOnMouseMoved(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        // +15 moves the tooltip 15 pixels below the mouse cursor;
                        // if you don't change the y coordinate of the tooltip, you
                        // will see constant screen flicker
                        tooltip.show(data.getNode(), event.getScreenX(), event.getScreenY() + 15);
                        tooltip.setText(series.getName() + " | " + data.getYValue().toString());
                    }
                });
                data.getNode().setOnMouseExited(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event){
                        tooltip.hide();
                    }
                });




//
//
//                tooltip.show(data.getNode(), t.getScreenX(), t.getScreenY() + 15);
//                Tooltip.install(data.getNode(), tooltip);
            }
        }
    }

}
