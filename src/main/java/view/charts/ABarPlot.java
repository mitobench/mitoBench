package view.charts;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import view.groups.AddToGroupDialog;
import view.groups.CreateGroupDialog;
import view.table.exportdialogue.ExportDialogue;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;


/**
 * Created by neukamm on 09.11.16.
 */
public abstract class ABarPlot {

    protected BarChartExt<String, Number> bc;
    private ChartController chartController;
    private TabPane scene;



    public ABarPlot(String title, String ylabel, TabPane scene, Stage stage){
        this.scene = scene;
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        bc = new BarChartExt<String, Number>(xAxis, yAxis);
        bc.setTitle(title);
        bc.setAnimated(false);
        yAxis.setLabel(ylabel);
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

        bc.prefWidthProperty().bind(scene.widthProperty());
        bc.autosize();
        //addMouseListener();
        setContextMenu(stage);
        chartController = new ChartController();

    }



    public abstract void addData(HashMap<String, Integer> dataNew);

    public BarChart<String,Number> getBarChart() {
        return bc;
    }

    public void clearData(){

        this.bc.getData().clear();

        for (XYChart.Series<String, Number> series : bc.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                Node node = data.getNode();
                Parent parent = node.parentProperty().get();
                if (parent != null && parent instanceof Group) {
                    Group group = (Group) parent;
                    group.getChildren().clear();
                }
            }
        }}

//
//    public void addMouseListener() {
//        bc.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                MouseButton button = event.getButton();
//                if (button == MouseButton.SECONDARY) {
//                    System.out.println("SECONDARY button clicked on button");
//                    chartController.saveAsPng(bc.snapshot(new SnapshotParameters(), null, ));
//                }
//
//            }
//        });
//    }


    private void setContextMenu(Stage stage){


        //adding a context menu item to the chart
        final MenuItem saveAsPng = new MenuItem("Save as png");
        saveAsPng.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                // todo: select location for png
                FileChooser fileChooser = new FileChooser();
                //Show save file dialog
                File file = fileChooser.showSaveDialog(stage);

                if(file != null){
                    chartController.saveAsPng(bc.snapshot(new SnapshotParameters(), null), file);
                }
                //
            }
        });

        final ContextMenu menu = new ContextMenu(
                saveAsPng
        );

        bc.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                if (MouseButton.SECONDARY.equals(event.getButton())) {
                    menu.show(scene, event.getScreenX(), event.getScreenY());
                }
            }
        });



    }


    }
