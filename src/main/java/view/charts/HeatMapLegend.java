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

/**
 * Created by neukamm on 13.07.17.
 */
public class HeatMapLegend {

    private final static double MIN = 0 ;
    private final static double MAX = 1 ;
    private final VBox root;
    private final HeatChart heatmap;
    private int height;
    private int width;

    public HeatMapLegend(double width, HeatChart heat){
        heatmap = heat;
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

        java.awt.Color awtColor = heatmap.getCellColour(value, 0,1);
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

        double[] steps = new double[]{0.0,0.2,0.4,0.6,0.8,1.0};
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

//        HBox valuebox = new HBox();
//        valuebox.setMaxWidth(width);
//        //valuebox.setSpacing(30);
//        //valuebox.setAlignment(Pos.CENTER);
//
//
//
//        double[] steps = new double[]{0.0,0.2,0.4,0.6,0.8,1.0};
//        for(int i = 0; i < steps.length; i++){
//            Label val_label = new Label(""+steps[i]);
//            val_label.setPrefHeight(height/(double)10);
//            val_label.setFont(new Font("Sans-Serif", 16));
//            valuebox.getChildren().add(val_label);
//        }

        return pane;
    }


    private Node createSpacer() {
        final Region spacer = new Region();
        // Make it always grow or shrink according to the available space
        VBox.setVgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    /*
            GETTER

     */
    public VBox getRoot() {
        return root;
    }
}
