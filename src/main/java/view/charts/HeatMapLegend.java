package view.charts;

import javafx.geometry.Orientation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
/**
 * Created by neukamm on 13.07.17.
 */
public class HeatMapLegend {

    private final static double MIN = 0 ;
    private final static double MAX = 1 ;
    private final static double WHITE_HUE = Color.WHITE.getHue() ;
    private final static double BLUE_HUE = Color.BLUE.getHue() ;
    private final StackPane root;

    public HeatMapLegend(int height){
        Image colorScale = createColorScaleImage(50, height, Orientation.VERTICAL);
        ImageView imageView = new ImageView(colorScale);
        root = new StackPane(imageView);

    }

    private Color getColorForValue(double value) {
        if (value < MIN || value > MAX) {
            return Color.BLACK ;
        }
        double hue = WHITE_HUE + (BLUE_HUE - WHITE_HUE) * (value - MIN) / (MAX - MIN) ;
        return Color.hsb(hue, 1.0, 1.0);
    }

    private Image createColorScaleImage(int width, int height, Orientation orientation) {
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

    public StackPane getRoot() {
        return root;
    }
}
