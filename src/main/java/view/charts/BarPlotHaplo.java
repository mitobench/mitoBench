package view.charts;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;

/**
 * Created by neukamm on 03.11.16.
 */
public class BarPlotHaplo extends ABarPlot {

    private XYChart.Series series;
    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;


    public BarPlotHaplo(String title, String xlabel, String ylabel) {
        super(title, xlabel, ylabel);

    }

    @Override
    public void addData(String name, HashMap<String, Integer> data) {

        series = new XYChart.Series();
        series.setName(name);

        for (String haplo : data.keySet()) {
            series.getData().add(new XYChart.Data(haplo, data.get(haplo)));
        }

        this.bc.getData().clear();
        this.bc.getData().add(series);


    }


    public void setDragAndMove(){
        this.bc.setCursor(Cursor.HAND);
        this.bc.setOnMousePressed(circleOnMousePressedEventHandler);
        this.bc.setOnMouseDragged(circleOnMouseDraggedEventHandler);

    }


    public Parent createZoomPane(final Pane group) {
        final double SCALE_DELTA = 1.1;
        final StackPane zoomPane = new StackPane();

        zoomPane.getChildren().add(group);
        zoomPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                event.consume();

                if (event.getDeltaY() == 0) {
                    return;
                }

                double scaleFactor =
                        (event.getDeltaY() > 0)
                                ? SCALE_DELTA
                                : 1 / SCALE_DELTA;

                group.setScaleX(group.getScaleX() * scaleFactor);
                group.setScaleY(group.getScaleY() * scaleFactor);
            }
        });

        zoomPane.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable, Bounds oldBounds, Bounds bounds) {
                zoomPane.setClip(new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight()));
            }
        });

        return zoomPane;
    }




    EventHandler<MouseEvent> circleOnMousePressedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();
                    orgTranslateX = ((BarChart)(t.getSource())).getTranslateX();
                    orgTranslateY = ((BarChart)(t.getSource())).getTranslateY();
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

                    ((BarChart)(t.getSource())).setTranslateX(newTranslateX);
                    ((BarChart)(t.getSource())).setTranslateY(newTranslateY);
                }
            };

}

