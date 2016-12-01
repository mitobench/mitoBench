package view.charts;
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

    public SunburstChart(BorderPane borderPane, HashMap<String, List<String>> hg_to_group,
                         HashMap<String, HashMap<String, Integer>> weights){

        sunburstBorderPane = borderPane;

        // Create the SunburstJ Control
        sunburstView = new SunburstView();

        // Create all the available color strategies once to be able to use them at runtime.
        ColorStrategyRandom colorStrategyRandom = new ColorStrategyRandom();
        ColorStrategySectorShades colorStrategyShades = new ColorStrategySectorShades();


        addData(hg_to_group, weights);

//        System.out.println("root children: ");
//        for (WeightedTreeItem<String> eatable : rootData.getChildrenWeighted()){
//            System.out.println(eatable.getValue() + ": " + eatable.getRelativeWeight());
//        }


        // Set the view.data as root item
        sunburstView.setRootItem(rootData);
        sunburstView.setColorStrategy(colorStrategyShades);

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
        btnShowLegend.setOnAction(event -> {
            //sunburstView.setLegendVisibility(true);
        });

        ToggleButton btnHideLegend = new ToggleButton("Hide Legend");
        btnHideLegend.setOnAction(event -> {
            //sunburstView.setLegendVisibility(false);
        });



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


    public void addData(HashMap<String, List<String>> hg_to_group, HashMap<String, HashMap<String, Integer>> weights) {

        clear();

        // Define a strategy by which the view.data should be received.
        ISourceStrategy sourceStrategy = new SourceStrategyHaplogroups();
        rootData = sourceStrategy.getData(hg_to_group, weights);


    }



    public BorderPane getBorderPane(){
        return sunburstBorderPane;
    }


    public void clear(){
        this.rootData = null;
    }


    public void finishSetup(){


        // Max Level drawn

        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(10);
        slider.setValue(sunburstView.getMaxDeepness());
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(5);
        slider.setMinorTickCount(1);
        slider.setBlockIncrement(1);

        slider.valueProperty().addListener(x -> sunburstView.setMaxDeepness((int)slider.getValue()));

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


        toolbar.getChildren().addAll(slider, zoomSlider);

        sunburstBorderPane.setTop(toolbar);

        sunburstBorderPane.setCenter(sunburstView);
        BorderPane.setAlignment(sunburstView, Pos.CENTER);
        SunburstLegend myLegend = new SunburstLegend(sunburstView);
        sunburstBorderPane.setRight(myLegend);
        BorderPane.setMargin(myLegend, new Insets(20));
        BorderPane.setAlignment(myLegend, Pos.CENTER_LEFT);


        Event.fireEvent(sunburstView, new SunburstView.VisualChangedEvent());

    }
}
