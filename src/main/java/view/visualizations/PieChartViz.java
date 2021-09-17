package view.visualizations;

import Logging.LogClass;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

/**
 * Created by neukamm on 22.02.17.
 */
public class PieChartViz extends AChart {

    private final javafx.scene.chart.PieChart chart;

    public PieChartViz(String title, TabPane tabPane, LogClass logClass) {
        super(null, null, logClass);
        chart = new PieChart();
        chart.setTitle(title);
    }

    public void setColor(Stage stage) {
        URL url = this.getClass().getResource("/css/ColorsPieChart.css");
        stage.getScene().getStylesheets().add(url.toExternalForm());

        int i = 0;
        for (PieChart.Data data : chart.getData()) {
            String hg = data.getName();
            Node node = data.getNode();
            //node.getStyleClass().remove("default-color" + (i % 8));
            //node.getStyleClass().add("default-color"+i);
            node.getStyleClass().add("default-color"+hg);
            i++;
        }

        //Set Legend items color
        int j = 0;
        for (Node node : chart.lookupAll(".chart-legend-item")) {
            if (node instanceof Label && ((Label) node).getGraphic() != null) {
                String hg = ((Label) node).getText();
                //((Label) node).getGraphic().getStyleClass().remove("default-color" + (j % 8));
                //((Label) node).getGraphic().getStyleClass().add("default-color" + j);
                ((Label) node).getGraphic().getStyleClass().add("default-color" + hg);
            }
            j++;
        }
    }



    public void createPlot(String group, HashMap<String, List<XYChart.Data<String, Number>>> data_all) {

        HashMap<String, Integer> hg_count = new HashMap<>();

        for(String hg : data_all.keySet()){
            List<XYChart.Data<String, Number>> data_entry = data_all.get(hg);
            for(XYChart.Data<String, Number> data : data_entry){
                String g = data.getXValue();
                if(g.equals(group) && !g.equals("")){
                    int count = data.getYValue().intValue();
                    if(count!=0){
                        if(hg_count.containsKey(hg)){
                            int count_tmp = hg_count.get(hg) + count;
                            hg_count.put(hg, count_tmp);
                        } else {
                            hg_count.put(hg, count);
                        }
                    }
                }
            }
        }

        List<String> labels = new ArrayList<>();
        labels.addAll(hg_count.keySet());
        Collections.sort(labels);
        if(labels.contains("Others")){
            labels.remove("Others");
            labels.add("Others");
        }

        for(String hg : labels){
            PieChart.Data slice = new PieChart.Data(hg, hg_count.get(hg));
            chart.getData().add(slice);
        }
    }


    public void createPlotSingle(HashMap<String, ArrayList> hgs_summed) {
        List<String> labels = new ArrayList<>();
        labels.addAll(hgs_summed.keySet());
        Collections.sort(labels);

        if(labels.contains("Others")){
            labels.remove("Others");
            labels.add("Others");
        }

        for(String hg : labels){
            PieChart.Data slice = new PieChart.Data(hg, hgs_summed.get(hg).size());
            chart.getData().add(slice);
        }
    }

    @Override
    protected void layoutChartChildren(double v, double v1, double v2, double v3) {

    }


    /*
            Setter and Getter
     */
    public PieChart getChart() {
        return chart;
    }
}
