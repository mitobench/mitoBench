package view.charts;


import Logging.LogClass;
import controls.sunburst.*;
import data.ISourceStrategy;
import data.SourceStrategyHaplogroup;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.controlsfx.control.SegmentedButton;

import java.io.File;
import java.util.HashMap;
import java.util.List;


/**
 * Created by neukamm on 30.11.16.
 */
public class SunburstChartCreator extends AChart{

    private SunburstView sunburstView;
    private BorderPane sunburstBorderPane;
    private WeightedTreeItem<String> rootData;
    private ColorStrategyRandom colorStrategyRandom;
    private ColorStrategySectorShades colorStrategyShades;
    private ColorStrategyGroups colorStrategyGroups;
    private Stage stage;
    private SunburstLegend myLegend;

    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;


    public SunburstChartCreator(Stage stage, TabPane tabPane, LogClass logClass){
        super( null, null, logClass);

        this.stage = stage;
        this.sunburstBorderPane = new BorderPane();
        this.sunburstBorderPane.setMinSize(0,0);

        this.getBorderPane().setId("borderpane_sunburst");
        // Create the SunburstJ Control
        sunburstView = new SunburstView();
        //sunburstView.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth()/3);
        File f = new File("src/main/java/view/charts/css/sunburstview.css");
        stage.getScene().getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

        // Create all the available color strategies once to be able to use them at runtime.
        colorStrategyRandom = new ColorStrategyRandom();
        colorStrategyShades = new ColorStrategySectorShades();
        colorStrategyGroups = new ColorStrategyGroups();

        setDragAndMove();
        myLegend = new SunburstLegend(sunburstView);
        // pair chart and legend
        HBox chart_legend = new HBox();
        chart_legend.getChildren().addAll(sunburstView, myLegend);
        setContextMenu(sunburstBorderPane, tabPane);

    }

    /**
     * This
     *
     * @param hg_to_group
     * @param weights
     * @param treeMap
     * @param tree
     * @param treeView
     */
    public void create(HashMap<String, List<String>> hg_to_group,
                       HashMap<String, HashMap<String, Double>> weights,
                       HashMap<String, List<String>> treeMap,
                       TreeItem<String> tree,
                       TreeView treeView){

        loadData(hg_to_group, weights, treeMap, tree, treeView);
        finishSetup();


    }

    private void loadData( HashMap<String, List<String>> hg_to_group,
                           HashMap<String, HashMap<String, Double>> weights,
                           HashMap<String, List<String>> treeMap,
                           TreeItem<String> tree, TreeView treeView){
        // load data
        addData(hg_to_group, weights, treeMap, tree, treeView);

        // Set the view.data as root item
        sunburstView.setRootItem(rootData);
        //sunburstView.setColorStrategy(colorStrategyGroups);
        sunburstView.setColorStrategy(colorStrategyShades);
    }

    private void finishSetup(){
        // Example Controls

        ToggleButton btnCSShades = new ToggleButton("Shades Color Strategy");
        btnCSShades.setOnAction(event -> {
            sunburstView.setColorStrategy(colorStrategyShades);
        });

        ToggleButton btnCSRandom = new ToggleButton("Random Color Strategy");
        btnCSRandom.setOnAction(event -> {
            sunburstView.setColorStrategy(colorStrategyRandom);
        });

        IColorStrategy colorStrategy = sunburstView.getColorStrategy();
        if(colorStrategy instanceof ColorStrategyRandom){
            btnCSRandom.setSelected(true);
        }else if(colorStrategy instanceof  ColorStrategySectorShades){
            btnCSShades.setSelected(true);
        }


        ToggleButton btnShowLegend = new ToggleButton("Show Legend");
        btnShowLegend.setSelected(true);
        ToggleButton btnHideLegend = new ToggleButton("Hide Legend");

        btnHideLegend.setOnAction(event -> {
            btnShowLegend.setSelected(false);
            myLegend.clearLegend();
        });
        btnShowLegend.setOnAction(event -> {
            btnHideLegend.setSelected(false);
            myLegend.updateLegend();
        });

        HBox toolbar = new HBox(20);
        BorderPane.setMargin(toolbar, new Insets(10));

        // Max Level drawn

        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(sunburstView.getMaxDeepness());
        slider.setValue(3);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(1);
        slider.setBlockIncrement(1);
        slider.valueProperty().addListener(x -> sunburstView.setMaxDeepness((int)slider.getValue()));

        // Zoom level

        Slider zoomSlider = new Slider();
        zoomSlider.setMin(0.1);
        zoomSlider.setMax(3);
        zoomSlider.setValue(sunburstView.getScaleX()/1.5);
        //zoomSlider.setValue(0.5);
        zoomSlider.setShowTickLabels(true);
        zoomSlider.setShowTickMarks(true);
        zoomSlider.setMajorTickUnit(0.5);
        zoomSlider.setMinorTickCount(1);
        zoomSlider.setBlockIncrement(0.1);

        // set initial size
        sunburstView.setScaleX(zoomSlider.getValue());
        sunburstView.setScaleY(zoomSlider.getValue());

        zoomSlider.valueProperty().addListener(x -> {
            toolbar.toFront();
            double scale = zoomSlider.getValue();
            sunburstView.setScaleX(scale);
            sunburstView.setScaleY(scale);
        });



        SegmentedButton colorStrategies = new SegmentedButton();
        colorStrategies.getButtons().addAll(btnCSShades, btnCSRandom);

        SegmentedButton legendVisibility = new SegmentedButton();
        legendVisibility.getButtons().addAll(btnShowLegend, btnHideLegend);

        toolbar.getChildren().addAll(colorStrategies, slider, legendVisibility, zoomSlider);

        sunburstBorderPane.setTop(toolbar);

        sunburstBorderPane.setCenter(sunburstView);
        BorderPane.setAlignment(sunburstView, Pos.CENTER_RIGHT);

        sunburstBorderPane.setRight(myLegend);
        BorderPane.setMargin(myLegend, new Insets(20));
        BorderPane.setAlignment(myLegend, Pos.CENTER_LEFT);
        stage.show();

        Event.fireEvent(sunburstView, new SunburstView.VisualChangedEvent());


    }

    /**
     * This method parses data to class where data are added to chart
     * @param hg_to_group
     * @param weights
     */
    public void addData(HashMap<String, List<String>> hg_to_group,
                        HashMap<String, HashMap<String, Double>> weights,
                        HashMap<String, List<String>> treeMap,
                        TreeItem<String> tree,
                        TreeView treeView) {


        // Define a strategy by which the view.data should be received.
        ISourceStrategy sourceStrategy = new SourceStrategyHaplogroup();
        rootData = sourceStrategy.getData(hg_to_group, weights, treeMap, tree, treeView);

    }

    /**
     * This method allows drag and drop of sunburst view
     */
    public void setDragAndMove(){
        sunburstView.setCursor(Cursor.HAND);
        sunburstView.setOnMousePressed(circleOnMousePressedEventHandler);
        sunburstView.setOnMouseDragged(circleOnMouseDraggedEventHandler);

    }


    EventHandler<MouseEvent> circleOnMousePressedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();
                    orgTranslateX = ((SunburstView)(t.getSource())).getTranslateX();
                    orgTranslateY = ((SunburstView)(t.getSource())).getTranslateY();
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

                    ((SunburstView)(t.getSource())).setTranslateX(newTranslateX);
                    ((SunburstView)(t.getSource())).setTranslateY(newTranslateY);
                }
            };



    public void clear(){
        if(rootData.getChildren()==null)
            this.rootData.getChildren().clear();
    }



    /*

            GETTER and SETTER


     */
    public BorderPane getBorderPane(){
        return sunburstBorderPane;
    }

    public SunburstView getSunburstView() {
        return sunburstView;
    }
}
