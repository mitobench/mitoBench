package view.visualizations;

import Logging.LogClass;
import javafx.collections.ObservableList;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

import java.net.URL;

public class ScatterPlot extends AChart{

    private final TabPane tabPaneStats;
    private ScatterChart<Number, Number> sc;

    public ScatterPlot(Stage stage, LogClass logClass, TabPane tabpane_statistics){
        super("", "",logClass);
        tabPaneStats = tabpane_statistics;

        URL url = this.getClass().getResource("/css/ColorsPCA.css");
        stage.getScene().getStylesheets().add(url.toExternalForm());

    }


    public void create(double lowerbound_x, double lowerbound_y, double upperbound_x, double upperbound_y, String x_label,
                       String y_label, String title){
        final NumberAxis xAxis = new NumberAxis(lowerbound_x, upperbound_x, 1);
        final NumberAxis yAxis = new NumberAxis(lowerbound_y, upperbound_y, 1);
        sc = new ScatterChart<>(xAxis,yAxis);
        xAxis.setLabel(x_label);
        yAxis.setLabel(y_label);
        sc.setTitle(title);

        setContextMenu(getSc(), tabPaneStats);

    }

    public void addSeries(String name, double[] pc1, double[] pc2, String[] names){
        XYChart.Series series = new XYChart.Series();
        series.setName(name);

        for(int i = 0; i < pc1.length; i++){
            XYChart.Data<Number, Number> data = new XYChart.Data(pc1[i], pc2[i]*(-1));
            series.getData().add(data);
        }

        sc.getData().add(series);
//        int i = 0;
//        ObservableList<XYChart.Data<Number, Number>> data_list = series.getData();
//        for (XYChart.Data<Number, Number> d : data_list) {
//            Tooltip tip = new Tooltip(names[i]);
//            Tooltip.install(d.getNode(), tip);
//            i++;
//        }


    }


    /**
     * GETTER AND SETTER
     *
     */

    public ScatterChart<Number, Number> getSc() {
        return sc;
    }


    @Override
    protected void layoutChartChildren(double v, double v1, double v2, double v3) {

    }
}
