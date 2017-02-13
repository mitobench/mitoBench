package view.charts;

import controls.skin.SunburstViewSkin;
import controls.sunburst.*;
import data.ISourceStrategy;
import data.SourceStrategyHaplogroup;
import io.Exceptions.ImageException;
import io.writer.ImageWriter;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.apache.poi.ss.formula.functions.T;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/**
 * Created by neukamm on 30.11.16.
 */
public class SunburstChartCreator {

    private SunburstView sunburstView;
    private BorderPane sunburstBorderPane;
    private WeightedTreeItem<String> rootData;
    private ColorStrategyRandom colorStrategyRandom;
    private ColorStrategySectorShades colorStrategyShades;
    //private ColorStrategyGroups colorStrategyGroups;
    private Stage stage;
    private TabPane tabPane;
    private Button goBack;


    public SunburstChartCreator(BorderPane borderPane, Stage stage, TabPane tabPane){

        this.stage = stage;
        this.tabPane = tabPane;
        this.sunburstBorderPane = borderPane;
        this.getBorderPane().setId("borderpane_sunburst");
        // Create the SunburstJ Control
        sunburstView = new SunburstView();
        File f = new File("src/main/java/view/charts/css/sunburstview.css");
        stage.getScene().getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

        // Create all the available color strategies once to be able to use them at runtime.
        colorStrategyRandom = new ColorStrategyRandom();
        colorStrategyShades = new ColorStrategySectorShades();
        //colorStrategyGroups = new ColorStrategyGroups();


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
        setContextMenu(stage);


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


        // set legend
        SunburstLegend myLegend = new SunburstLegend(sunburstView);
        // todo: order legend items in columns

        // set color buttons
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


        // set legend buttons

        ToggleButton btnShowLegend = new ToggleButton("Show Legend");
        ToggleButton btnHideLegend = new ToggleButton("Hide Legend");

        btnShowLegend.setSelected(true);
        btnShowLegend.setOnAction(event -> {
            btnHideLegend.setSelected(false);
            myLegend.updateLegend();
        });


        btnHideLegend.setOnAction(event ->{
            btnShowLegend.setSelected(false);
            myLegend.clearLegend();
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

        HBox toolbar = new HBox(20);

        zoomSlider.valueProperty().addListener(x -> {
            toolbar.toFront();
            double scale = zoomSlider.getValue();
            sunburstView.setScaleX(scale);
            sunburstView.setScaleY(scale);
        });

        // set slider to change level
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(sunburstView.getMaxDeepness());
        slider.setValue(sunburstView.getMaxDeepness());
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(1);
        slider.setBlockIncrement(1);
        slider.valueProperty().addListener(x -> sunburstView.setMaxDeepness((int)slider.getValue()));

        BorderPane.setMargin(toolbar, new Insets(10));

        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);

        toolbar.getChildren().addAll(zoomSlider, slider, btnCSShades, btnCSRandom, separator, btnShowLegend, btnHideLegend);

        sunburstBorderPane.setTop(toolbar);
        sunburstBorderPane.setCenter(sunburstView);
        sunburstBorderPane.setRight(myLegend);
        BorderPane.setAlignment(sunburstView, Pos.CENTER);
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
                        HashMap<String, HashMap<String, Double>> weights,
                        HashMap<String, List<String>> treeMap,
                        TreeItem<String> tree,
                        TreeView treeView) {


        // Define a strategy by which the view.data should be received.
        ISourceStrategy sourceStrategy = new SourceStrategyHaplogroup();
        rootData = sourceStrategy.getData(hg_to_group, weights, treeMap, tree, treeView);

    }



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

    private void setContextMenu(Stage stage){


        //adding a context menu item to the chart
        final MenuItem saveAsPDF = new MenuItem("Save as png");
        saveAsPDF.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {

//                PrinterJob job = PrinterJob.createPrinterJob();
//                if(job != null){
//                    job.showPrintDialog(stage); // Window must be your main Stage
//                    job.printPage(sunburstView);
//                    job.endJob();
//                }
                ImageWriter imageWriter = new ImageWriter();
                try {
                    imageWriter.saveImage(stage, tabPane.snapshot(new SnapshotParameters(), null));
                } catch (ImageException e) {
                    e.printStackTrace();
                }
            }
        });

//        final MenuItem showLevel = new MenuItem("Show only this level");
//        showLevel.setOnAction(new EventHandler<ActionEvent>() {
//            @Override public void handle(ActionEvent event) {
//                goBack.setVisible(true);
//                //sunburstView.setOnlySelectedLevelVisible();
//
//            }
//        });

        final ContextMenu menu = new ContextMenu();
        menu.getItems().addAll(saveAsPDF);

        sunburstView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                if (MouseButton.SECONDARY.equals(event.getButton())) {
                    menu.show(tabPane, event.getScreenX(), event.getScreenY());
                }
            }
        });



    }






}
