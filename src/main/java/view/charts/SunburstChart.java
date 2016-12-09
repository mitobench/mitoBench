package view.charts;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.controls.sunburst.*;
import view.data.ISourceStrategy;
import view.data.SourceStrategyHaplogroups;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.File;
import java.util.HashMap;
import java.util.List;


/**
 * Created by neukamm on 30.11.16.
 */
public class SunburstChart {

    private SunburstView sunburstView;
    private BorderPane sunburstBorderPane;
    private WeightedTreeItem<String> rootData;
    private ColorStrategyRandom colorStrategyRandom;
    private ColorStrategySectorShades colorStrategyShades;
    private ChartController chartController;
    private Stage stage;
    private TabPane tabPane;


    public SunburstChart(BorderPane borderPane, ChartController chartController, Stage stage, TabPane tabPane){

        this.chartController = chartController;
        this.stage = stage;
        sunburstBorderPane = borderPane;

        // Create the SunburstJ Control
        sunburstView = new SunburstView();



        // Create all the available color strategies once to be able to use them at runtime.
        colorStrategyRandom = new ColorStrategyRandom();
        colorStrategyShades = new ColorStrategySectorShades();

        this.tabPane = tabPane;

    }

    public void create(HashMap<String, List<String>> hg_to_group,
                       HashMap<String, HashMap<String, Integer>> weights,
                       HashMap<String, List<String>> treeMap,
                       TreeItem<String> tree,
                       TreeView treeView){



        loadData(hg_to_group, weights, treeMap, tree, treeView);
        addEvents();
        finishSetup();
        setContextMenu(stage);


    }

    private void loadData( HashMap<String, List<String>> hg_to_group,
                           HashMap<String, HashMap<String, Integer>> weights,
                           HashMap<String, List<String>> treeMap,
                           TreeItem<String> tree, TreeView treeView){
        // load data
        addData(hg_to_group, weights, treeMap, tree, treeView);

        // Set the view.data as root item
        sunburstView.setRootItem(rootData);
        sunburstView.setColorStrategy(colorStrategyShades);
    }

    private void addEvents(){

        ToggleButton btnCSShades = new ToggleButton("Shades Color Strategy");
        ToggleButton btnCSRandom = new ToggleButton("Random Color Strategy");

        btnCSShades.setOnAction(event -> {
            btnCSRandom.setSelected(false);
            sunburstView.setColorStrategy(colorStrategyShades);
        });


        btnCSRandom.setOnAction(event -> {
            btnCSShades.setSelected(false);
            sunburstView.setColorStrategy(colorStrategyRandom);
        });


        IColorStrategy colorStrategy = sunburstView.getColorStrategy();
        if(colorStrategy instanceof ColorStrategyRandom){
            btnCSRandom.setSelected(true);
        }else if(colorStrategy instanceof  ColorStrategySectorShades){
            btnCSShades.setSelected(true);
        }

        HBox buttons = new HBox();
        buttons.getChildren().addAll(btnCSShades, btnCSRandom);
        sunburstBorderPane.setBottom(buttons);


        sunburstView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                MouseButton button = event.getButton();
                if(button==MouseButton.SECONDARY) {
                    System.out.println("SECONDARY button clicked on button");
                }

            }
        });


    }


    private void setContextMenu(Stage stage){


        //adding a context menu item to the chart
        final MenuItem saveAsPDF = new MenuItem("Save as pdf");
        saveAsPDF.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                //Show save file dialog
                File file = fileChooser.showSaveDialog(stage);

                if(file != null){
                    chartController.saveAsPng(tabPane.snapshot(new SnapshotParameters(), null), file);
                }
                //
            }
        });

        final ContextMenu menu = new ContextMenu(
                saveAsPDF
        );

        sunburstView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                if (MouseButton.SECONDARY.equals(event.getButton())) {
                    menu.show(tabPane, event.getScreenX(), event.getScreenY());
                }
            }
        });



    }


    private void finishSetup(){

        // Zoom level

        Slider zoomSlider = new Slider();
        zoomSlider.setMin(0.1);
        zoomSlider.setMax(3);
        zoomSlider.setValue(sunburstView.getScaleX());
        zoomSlider.setShowTickLabels(true);
        zoomSlider.setShowTickMarks(true);
        zoomSlider.setMajorTickUnit(0.5);
        zoomSlider.setMinorTickCount(1);
        zoomSlider.setBlockIncrement(0.1);

        zoomSlider.valueProperty().addListener(x -> {
            double scale = zoomSlider.getValue();
            sunburstView.setScaleX(scale);
            sunburstView.setScaleY(scale);
        });


        HBox toolbar = new HBox(20);
        BorderPane.setMargin(toolbar, new Insets(10));


        toolbar.getChildren().add(zoomSlider);

        sunburstBorderPane.setTop(toolbar);

        sunburstBorderPane.setCenter(sunburstView);
        BorderPane.setAlignment(sunburstView, Pos.CENTER);
        SunburstLegend myLegend = new SunburstLegend(sunburstView);
        sunburstBorderPane.setRight(myLegend);
        BorderPane.setMargin(myLegend, new Insets(20));
        BorderPane.setAlignment(myLegend, Pos.CENTER_LEFT);

        Event.fireEvent(sunburstView, new SunburstView.VisualChangedEvent());


    }

    /**
     * This method parses data to class where data are added to chart
     * @param hg_to_group
     * @param weights
     */
    public void addData(HashMap<String, List<String>> hg_to_group,
                        HashMap<String, HashMap<String, Integer>> weights,
                        HashMap<String, List<String>> treeMap,
                        TreeItem<String> tree,
                        TreeView treeView) {

        // Define a strategy by which the view.data should be received.
        ISourceStrategy sourceStrategy = new SourceStrategyHaplogroups();
        rootData = sourceStrategy.getData(hg_to_group, weights, treeMap, tree, treeView);

    }


    public BorderPane getBorderPane(){
        return sunburstBorderPane;
    }

    public void clear(){
        if(rootData.getChildren()==null)
            this.rootData.getChildren().clear();
    }



}
