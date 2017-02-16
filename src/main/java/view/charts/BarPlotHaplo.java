package view.charts;

import controls.sunburst.ColorStrategyGroups;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import view.table.TableControllerUserBench;


import java.net.MalformedURLException;
import java.util.*;

/**
 * Created by neukamm on 03.11.16.
 */
public class BarPlotHaplo extends ABarPlot {

    private final ChartController chartController;
    private final TableControllerUserBench tableController;
    private final HashMap<String, ArrayList> hgs_summed;
    private final Stage stage;
    private XYChart.Series series;
    private ColorSchemeBarchart colorSchemeBarchart;


    public BarPlotHaplo(String title, String ylabel, TabPane scene, Stage stage, ChartController cc,
                        TableControllerUserBench tc) {
        super(title, ylabel, scene, stage);
        this.stage = stage;
        this.bc.setLegendVisible(false);
        chartController = cc;
        tableController = tc;

        TableColumn haplo_col = tableController.getTableColumnByName("Haplogroup");
        List<String> columnDataHG = new ArrayList<>();
        tableController.getTable().getItems().stream().forEach((o)
                -> columnDataHG.add((String)haplo_col.getCellData(o)));
        hgs_summed = chartController.summarizeHaolpgroups(columnDataHG.stream().toArray(String[]::new), chartController.getCoreHGs());


    }


    @Override
    public void addData(HashMap<String, Integer> data) throws MalformedURLException {

        series = new XYChart.Series();
        series.setName("");

        colorSchemeBarchart = new ColorSchemeBarchart(stage);
        for (String haplo : data.keySet()) {
            XYChart.Data<String, Number> d = new XYChart.Data(haplo, data.get(haplo).intValue());

            series.getData().add(setColor(d, hgs_summed));
        }

        this.bc.getData().clear();
        this.bc.getData().add(series);

    }

    private XYChart.Data setColor(XYChart.Data<String, Number> d, HashMap<String, ArrayList> hgs_summed) {

        d.nodeProperty().addListener(new ChangeListener<Node>() {
            @Override public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) {
                if (newNode != null) {

                    for(String key : hgs_summed.keySet()){
                        ArrayList hg_sub = hgs_summed.get(key);
                        if(hg_sub.contains(d.getXValue())){
                            newNode = colorSchemeBarchart.setColor(newNode, key);
                        }
                    }
                }
            }
        });

        return d;
    }


}

