package view.charts;

import io.Exceptions.ImageException;
import io.writer.ImageWriter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import statistics.HaploStatistics;
import view.table.TableControllerUserBench;
import view.tree.HaplotreeController;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 26.01.17.
 */
public class ProfilePlot {

    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    LineChart<String,Number> profilePlot = new LineChart<String,Number>(xAxis,yAxis);
    List<XYChart.Series> seriesList = new ArrayList<>();
    int maxVal = 0;


    public ProfilePlot(String title, String lable_xaxis, String label_yaxis, TabPane tabpane, Stage stage){
        xAxis.setLabel(lable_xaxis);
        yAxis.setLabel(label_yaxis);
        yAxis.setAutoRanging(false);
        yAxis.setTickUnit(5);
        yAxis.setMinorTickVisible(false);

        profilePlot.setTitle(title);
        setContextMenu(stage, tabpane);
    }



    public void create(TableControllerUserBench tableController, ChartController chartController, HaplotreeController treeController,
                       TabPane statsTabpane, Scene scene){

        String[][] cols = chartController.prepareColumns(new String[]{"Haplogroup", "Grouping"}, tableController.getSelectedRows());
        String[] selection_haplogroups = cols[0];
        String[] selection_groups = cols[1];
        HashMap<String, ArrayList> hgs_summed = chartController.summarizeHaolpgroups(selection_haplogroups, chartController.getCoreHGs());
        HashMap<String, List<XYChart.Data<String, Number>>> data_all;
        data_all = chartController.assignHGs(hgs_summed, selection_haplogroups, selection_groups);

        // sort list alphabetically
        List<String> hg_core_curr = chartController.getHg_core_list();
        java.util.Collections.sort(hg_core_curr);

        HashMap<String, List<XYChart.Data<String, Number>>> group_hg = new HashMap<>();

        for(String key : hg_core_curr){
            if(data_all.containsKey(key)) {
                for(int i = 0; i < selection_groups.length; i++){
                    String group = data_all.get(key).get(i).getXValue();
                    if(!group_hg.containsKey(group)){
                        List<XYChart.Data<String, Number>> hg = new ArrayList<>();
                        hg.add(data_all.get(key).get(i));
                        group_hg.put(group, hg);
                    } else {
                        List<XYChart.Data<String, Number>>hg_tmp = new ArrayList<>();
                        hg_tmp.addAll(group_hg.get(group));
                        hg_tmp.add(data_all.get(key).get(i));
                        group_hg.put(group, hg_tmp);
                    }

                    data_all.get(key).get(i).setYValue(chartController.roundValue(data_all.get(key).get(i).getYValue().doubleValue()));
                }
            } else {

            }
        }

        for(String group : group_hg.keySet()){
            addSeries(hg_core_curr, group_hg.get(group), group);
        }

        for(XYChart.Series series : seriesList)
            profilePlot.getData().add(series);

        addListener();
        setMaxBoundary();

        HaploStatistics haploStatistics = new HaploStatistics(tableController, treeController);

        haploStatistics.count(hg_core_curr.toArray(new String[hg_core_curr.size()]));
        TableView table = haploStatistics.writeToTable(haploStatistics.getData_all(), scene);
        haploStatistics.addListener(table, this);
        Tab tab = new Tab();
        tab.setId("tab_table_stats");
        tab.setText("Count statistics");
        tab.setContent(table);
        statsTabpane.getTabs().add(tab);
    }

    /**
     * This method add all previously creates series to chart.
     *
     * @param hgs
     * @param data
     * @param name
     */
    public void addSeries(List<String> hgs, List<XYChart.Data<String, Number>> data, String name){
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        series.setName(name);

        for(int i = 0; i < hgs.size(); i++){
            series.getData().add(new XYChart.Data(hgs.get(i), data.get(i).getYValue().doubleValue()));
            if(data.get(i).getYValue().doubleValue() > maxVal)
                maxVal = (int)data.get(i).getYValue().doubleValue();

        }

        this.seriesList.add(series);
    }


    /**
     * This method adds a css file to the profile plot to set the line with to 2px.
     */
    public void addListener(){
        File f = new File("src/main/java/view/charts/css/ProfilePlot.css");
        profilePlot.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
        profilePlot.setCreateSymbols(false);
        profilePlot.getStyleClass().add("thick-chart");

    }


    /*
            Getter and Setter
     */

    public LineChart<String,Number>  getPlot(){
        return profilePlot;
    }

    public void setMaxBoundary(){
        for(int i = 1; i < 6; i++){
            if((maxVal+i)%5 == 0){
                yAxis.setUpperBound(maxVal+i);
                break;
            }
        }
    }


    /**
     * This method initializes a context menu to save chart as image.
     * @param stage
     */
    private void setContextMenu(Stage stage, TabPane tabPane){


        //adding a context menu item to the chart
        final MenuItem saveAsPng = new MenuItem("Save as png");
        saveAsPng.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                ImageWriter imageWriter = new ImageWriter();
                try {
                    imageWriter.saveImage(stage, profilePlot.snapshot(new SnapshotParameters(), null));
                } catch (ImageException e) {
                    e.printStackTrace();
                }
            }
        });

        final ContextMenu menu = new ContextMenu(
                saveAsPng
        );

        profilePlot.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                if (MouseButton.SECONDARY.equals(event.getButton())) {
                    menu.show(tabPane, event.getScreenX(), event.getScreenY());
                }
            }
        });



    }




}
