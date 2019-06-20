package view.visualizations;

import Logging.LogClass;
import io.Exceptions.ImageException;
import io.writer.ImageWriter;
import io.writer.PDFExporter;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by neukamm on 17.02.17.
 */
public abstract class AChart extends Chart {

    private final LogClass lc;
    protected Node node;

    protected CategoryAxis xAxis = new CategoryAxis();
    protected NumberAxis yAxis = new NumberAxis();

    public AChart(String lable_xaxis, String label_yaxis, LogClass logClass){

        xAxis.tickLabelFontProperty().set(Font.font(15));
        xAxis.setLabel(lable_xaxis);
        xAxis.setTickMarkVisible(false);
        xAxis.setTickLabelRotation(90);

        yAxis.setTickUnit(5);
        yAxis.setLabel(label_yaxis);
        yAxis.setMinorTickVisible(false);
        yAxis.tickLabelFontProperty().set(Font.font(15));

        lc = logClass;


    }


    /**
     * This method initializes the context menu to save chart as image
     */
    protected void setContextMenu(Node node, Node pane){
        this.node = node;

        //adding a context menu item to the chart
        final MenuItem saveAsPng = new MenuItem("Save as png");
        saveAsPng.setOnAction(event -> {
            ImageWriter imageWriter = new ImageWriter(lc);
            try {
                imageWriter.saveImage(node);
            } catch (ImageException e) {
                e.printStackTrace();
            }
        });


        final MenuItem saveAsPdf = new MenuItem("Save as pdf");
        saveAsPdf.setOnAction(event -> {

            PDFExporter pdfExporter = new PDFExporter(node, new Stage(), lc);
            pdfExporter.print();

        });



        //final ContextMenu menu = new ContextMenu(saveAsPng, saveAsPdf);
        final ContextMenu menu = new ContextMenu(saveAsPng);

        node.setOnMouseClicked(event -> {
            if (MouseButton.SECONDARY.equals(event.getButton())) {
                menu.show(pane, event.getScreenX(), event.getScreenY());
            }
        });


    }
    public void setStyleSheet(Stage stage) throws MalformedURLException {
        URL url = this.getClass().getResource("/css/chart.css");
        stage.getScene().getStylesheets().add(url.toExternalForm());
    }


    public void hideLabelXAxis(){
        xAxis.setTickLabelsVisible(false);
    }

    public void showLabelXAxis(){
        xAxis.setTickLabelsVisible(true);
    }


    public void hideLabelYAxis(){
        yAxis.setTickLabelsVisible(false);
    }
}

