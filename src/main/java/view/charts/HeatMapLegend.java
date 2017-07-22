package view.charts;


import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by neukamm on 13.07.17.
 */
public class HeatMapLegend {

    private static double MIN = 0 ;
    private static double MAX = 1 ;
    private final VBox root;
    private final HeatChart heatmap;
    private int height;
    private int width;

    public HeatMapLegend(double width, HeatChart heat){
        heatmap = heat;

        MAX = heat.getHighValue();
        MIN = heat.getLowValue();

        Image colorScale = createColorScaleImage((int)width, 50, Orientation.HORIZONTAL);
        ImageView imageView = new ImageView(colorScale);

        // create "value-bar"
        HBox valueBar = createValuesBar();

        root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(imageView, valueBar);
    }


    private Image createColorScaleImage(int width, int height, Orientation orientation) {
        this.height = height;
        this.width = width;
        WritableImage image = new WritableImage(width, height);
        PixelWriter pixelWriter = image.getPixelWriter();

        if (orientation == Orientation.HORIZONTAL) {
            for (int x=0; x<width; x++) {
                double value = MIN + (MAX - MIN) * x / width;
                Color color = getColorForValue(value);

                for (int y=0; y<height; y++) {
                    pixelWriter.setColor(x, y, color);
                }
            }
        } else {
            for (int y=0; y<height; y++) {
                double value = MAX - (MAX - MIN) * y / height ;
                Color color = getColorForValue(value);
                for (int x=0; x<width; x++) {
                    pixelWriter.setColor(x, y, color);
                }
            }
        }
        return image ;
    }



    private Color getColorForValue(double value) {

        if (value < MIN || value > MAX) {
            return Color.BLACK ;
        }

        java.awt.Color awtColor = heatmap.getCellColour(value, MIN,MAX);
        int r = awtColor.getRed();
        int g = awtColor.getGreen();
        int b = awtColor.getBlue();
        int a = awtColor.getAlpha();
        double opacity = a / 255.0 ;
        javafx.scene.paint.Color color = javafx.scene.paint.Color.rgb(r, g, b, opacity);

        return color;

    }


    private HBox createValuesBar() {

        HBox pane = new HBox();
        pane.setMaxWidth(width);

        //pane.setPadding(new Insets(10,10,10,10));

        //double[] steps = new double[]{0.0,0.2,0.4,0.6,0.8,1.0};
        double[] steps = new double[4];
        double add = MAX / 3.0;
        double val = MIN;
        steps[0] = round(val,2);
        steps[3] = round(MAX, 2);
        for(int d = 1; d < steps.length-1; d++){
            steps[d] = round((val + add), 2);
            val += add;

        }
        int minWidth = width/steps.length;
        //TextField tf[] = new TextField[steps.length];
        Label tf[] = new Label[steps.length];
        // Add first spacer
        pane.getChildren().add(createSpacer());
        for(int i=0;i<tf.length;i++)
        {
            tf[i] = new Label(""+steps[i]);
            tf[i].setAlignment(Pos.CENTER);
            tf[i].setMinWidth(minWidth);
            //tf[i].setEditable(false);
            pane.getChildren().add(tf[i]);
            // Add a spacer after the label
            pane.getChildren().add(createSpacer());
        }
        pane.setAlignment(Pos.CENTER_RIGHT);

        return pane;
    }


    private Node createSpacer() {
        final Region spacer = new Region();
        // Make it always grow or shrink according to the available space
        VBox.setVgrow(spacer, Priority.ALWAYS);
        return spacer;
    }



    /**
     * This method rounds a double value on n digits.
     * @param value
     * @param numberOfDigitsAfterDecimalPoint
     * @return double rounded
     */
    public double round(double value, int numberOfDigitsAfterDecimalPoint) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint,
                BigDecimal.ROUND_HALF_EVEN);
        return bigDecimal.doubleValue();
    }

    /*
            GETTER

     */
    public VBox getRoot() {
        return root;
    }
}
