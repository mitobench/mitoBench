package view.charts;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import view.controls.sunburst.*;
import view.data.ISourceStrategy;
import view.data.SourceStrategyHaplogroups;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

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
    private HashMap<String, List<String>> node_to_children;

    public SunburstChart(BorderPane borderPane){

        sunburstBorderPane = borderPane;

        // Create the SunburstJ Control
        sunburstView = new SunburstView();

        // Create all the available color strategies once to be able to use them at runtime.
        colorStrategyRandom = new ColorStrategyRandom();
        colorStrategyShades = new ColorStrategySectorShades();

    }

    public void create(HashMap<String, List<String>> hg_to_group,
                       HashMap<String, HashMap<String, Integer>> weights,
                       HashMap<String, List<String>> treeMap,
                       TreeItem<String> tree,
                       HashMap<String, List<String>> node_to_children,
                       TreeView treeView){

        this.node_to_children = node_to_children;
        loadData(hg_to_group, weights, treeMap, tree, treeView);
        addButtons();
        finishSetup();
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

    private void addButtons(){

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
        rootData = sourceStrategy.getData(hg_to_group, weights, treeMap, tree, node_to_children, treeView);

    }


    public BorderPane getBorderPane(){
        return sunburstBorderPane;
    }

    public void clear(){
        if(rootData.getChildren()==null)
            this.rootData.getChildren().clear();
    }



}
