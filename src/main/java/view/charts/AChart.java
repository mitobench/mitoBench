package view.charts;

import Logging.LogClass;
import io.Exceptions.ImageException;
import io.writer.ImageWriter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

/**
 * Created by neukamm on 17.02.17.
 */
public abstract class AChart {

    private final LogClass lc;
    protected Node node;

    protected CategoryAxis xAxis = new CategoryAxis();
    protected NumberAxis yAxis = new NumberAxis();

    public AChart(String lable_xaxis, String label_yaxis, LogClass logClass){

        xAxis.tickLabelFontProperty().set(Font.font(15));
        xAxis.setLabel(lable_xaxis);
        xAxis.setTickMarkVisible(false);

        yAxis.setTickUnit(5);
        yAxis.setLabel(label_yaxis);
        yAxis.setMinorTickVisible(false);
        yAxis.tickLabelFontProperty().set(Font.font(15));

        lc = logClass;

    }


    /**
     * This method initializes the context menu to save chart as image
     */
    protected void setContextMenu(Node node, Node pane){
        this.node = node;

        //adding a context menu item to the chart
        final MenuItem saveAsPng = new MenuItem("Save as png");
        saveAsPng.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                ImageWriter imageWriter = new ImageWriter(lc);
                try {
                    imageWriter.saveImage(node);
                } catch (ImageException e) {
                    e.printStackTrace();
                }
            }
        });

        final ContextMenu menu = new ContextMenu(saveAsPng);

        node.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                if (MouseButton.SECONDARY.equals(event.getButton())) {
                    menu.show(pane, event.getScreenX(), event.getScreenY());
                }
            }
        });

    }

}
