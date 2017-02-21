package view.charts;

import io.Exceptions.ImageException;
import io.writer.ImageWriter;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Created by neukamm on 17.02.17.
 */
public abstract class AChart {

    protected CategoryAxis xAxis = new CategoryAxis();
    protected NumberAxis yAxis = new NumberAxis();

    public AChart(String title, String lable_xaxis, String label_yaxis){

        xAxis.tickLabelFontProperty().set(Font.font(15));
        xAxis.setLabel(lable_xaxis);
        xAxis.setTickMarkVisible(false);
        yAxis.setTickUnit(5);
        yAxis.setLabel(label_yaxis);
        yAxis.setMinorTickVisible(false);
        yAxis.tickLabelFontProperty().set(Font.font(15));


    }


}
