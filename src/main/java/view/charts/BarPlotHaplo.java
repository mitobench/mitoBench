package view.charts;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.*;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.HashMap;

/**
 * Created by neukamm on 03.11.16.
 */
public class BarPlotHaplo extends ABarPlot {

    private XYChart.Series series;
    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;


    public BarPlotHaplo(String title, String ylabel, VBox scene) {
        super(title, ylabel, scene);
        this.bc.setLegendVisible(false);
        setDragAndMove();
    }



    @Override
    public void addData(HashMap<String, Integer> data) {

        series = new XYChart.Series();
        series.setName("");

        for (String haplo : data.keySet()) {
            series.getData().add(new XYChart.Data(haplo, data.get(haplo)));
        }

        this.bc.getData().clear();
        this.bc.getData().add(series);

    }




    /**
     * This method cares about the drag and drop option of teh barplot
     *
     */

    public void setDragAndMove(){
        this.bc.setCursor(Cursor.HAND);
        this.bc.setOnMousePressed(circleOnMousePressedEventHandler);
        this.bc.setOnMouseDragged(circleOnMouseDraggedEventHandler);

    }


    /**
     * This method allows to zoom in the barplot
     *  --> just zoom in, no re-rendering!
     *
     * @param group
     * @return
     */
    public StackPane createZoomPane(final Pane group) {
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


    /** places a text label with a bar's value above a bar node for a given XYChart.Data */
    private void displayLabelForData(XYChart.Data<String, Number> data) {
        final Node node = data.getNode();
        final Text dataText = new Text(String.valueOf(data.getYValue()));
        node.parentProperty().addListener(new ChangeListener<Parent>() {
            @Override public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) {
                Group parentGroup = (Group) parent;
                if(parent!=null){
                    parentGroup.getChildren().add(dataText);
                }

            }
        });

        node.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
            @Override public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                dataText.setLayoutX(
                        Math.round(
                                bounds.getMinX() + bounds.getWidth() / 2 - dataText.prefWidth(-1) / 2
                        )
                );
                dataText.setLayoutY(
                        Math.round(
                                bounds.getMinY() - dataText.prefHeight(-1) * 0.5
                        )
                );
            }
        });
    }


}

