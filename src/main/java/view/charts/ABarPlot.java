package view.charts;

import io.Exceptions.ImageException;
import io.writer.ImageWriter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.MalformedURLException;
import java.util.HashMap;


/**
 * Created by neukamm on 09.11.16.
 */
public abstract class ABarPlot {

    protected BarChartExt<String, Number> bc;
    private TabPane scene;


    /**
     * Constructor which sets axes, title and context menu
     * @param title
     * @param ylabel
     * @param scene
     * @param stage
     */

    public ABarPlot(String title, String ylabel, TabPane scene, Stage stage){
        this.scene = scene;
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        bc = new BarChartExt<String, Number>(xAxis, yAxis);
        bc.setTitle(title);
        bc.setStyle("-fx-font-size: " + 20 + "px;");
        bc.setAnimated(false);
        xAxis.tickLabelFontProperty().set(Font.font(15));
        xAxis.setTickMarkVisible(false);
        xAxis.tickLabelFontProperty().set(Font.font(15));

        yAxis.tickLabelFontProperty().set(Font.font(15));
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

        bc.prefWidthProperty().bind(scene.widthProperty());
        bc.autosize();
        setContextMenu(stage);

    }

    public abstract void addData(HashMap<String, Integer>  data) throws MalformedURLException;


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



    /*

            GETTER AND SETTER

     */


    /**
     * This method returns the BarChart object
     * @return
     */
    public BarChart<String,Number> getBarChart() {
        return bc;
    }


    /**
     * This method initializes the context menu to save chart as image
     * @param stage
     */
    private void setContextMenu(Stage stage){

        //adding a context menu item to the chart
        final MenuItem saveAsPng = new MenuItem("Save as png");
        saveAsPng.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                int scale = 6; //6x resolution should be enough, users should downscale if required
                final SnapshotParameters spa = new SnapshotParameters();
                spa.setTransform(javafx.scene.transform.Transform.scale(scale, scale));
                ImageWriter imageWriter = new ImageWriter();
                try {
                    imageWriter.saveImage(stage, bc.snapshot(spa, null));
                } catch (ImageException e) {
                    e.printStackTrace();
                }
            }
        });

        final ContextMenu menu = new ContextMenu(saveAsPng);

        bc.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                if (MouseButton.SECONDARY.equals(event.getButton())) {
                    menu.show(scene, event.getScreenX(), event.getScreenY());
                }
            }
        });



    }


}
