package view.visualizations;

import Logging.LogClass;
import controller.TableControllerUserBench;
import io.dialogues.Export.SaveAsDialogue;
import io.writer.StatisticsWriter;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import statistics.HaploStatistics;

import java.io.IOException;
import java.net.URL;


public class PCAPlot extends AChart{

    private final TabPane tabPaneStats;
    private final HaploStatistics stats;
    private final TableControllerUserBench TCBench;
    private ScatterChart<Number, Number> sc;

    public PCAPlot(Stage stage, LogClass logClass, TabPane tabpane_statistics, HaploStatistics stats, TableControllerUserBench
                   tableControllerUserBench){
        super("", "",logClass);
        tabPaneStats = tabpane_statistics;
        this.stats = stats;
        this.TCBench = tableControllerUserBench;

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
        int i = 0;
        ObservableList<XYChart.Data<Number, Number>> data_list = series.getData();
        for (XYChart.Data<Number, Number> d : data_list) {
            Tooltip tip = new Tooltip(names[i]);
            Tooltip.install(d.getNode(), tip);
            i++;
        }


    }

    public HBox getBottomBox (){
        HBox bottom = new HBox();

        Button btn_getdata = new Button("Export Haplogroup frequencies");
        btn_getdata.setOnAction(event -> {


            FileChooser.ExtensionFilter fex = new FileChooser.ExtensionFilter("Text format (*.txt)", "*.txt");
            SaveAsDialogue sad = new SaveAsDialogue(fex);
            sad.start(new Stage());
            StatisticsWriter statisticsWriter = new StatisticsWriter();
            statisticsWriter.parseFreqs(tabPaneStats.getSelectionModel().getSelectedItem());
            try {
                statisticsWriter.writeData(sad.getOutFile(), TCBench);
            } catch (IOException e) {


            }


        });

        bottom.setAlignment(Pos.CENTER_RIGHT);
        bottom.setPadding(new Insets(0.5,0.5,0.5,0.5));
        bottom.getChildren().add(btn_getdata);
        return bottom;
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
