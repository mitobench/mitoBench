package view.helpers;

import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Created by Eric on 01.05.2014.
 */
public class ColorHelper {

    /**
     * Brightens the given color by the given fraction.
     * @param color
     * @param fraction
     * @return
     */
    public static Color brighten(Color color, double fraction) {
        double red =  (Math.min(1d, color.getRed() + 1d * fraction));
        double green = (Math.min(1d, color.getGreen() + 1d * fraction));
        double blue =  (Math.min(1d, color.getBlue() + 1d * fraction));

        double alpha = color.getOpacity();

        return new Color(red, green, blue, alpha);
    }

    /**
     * Darkens the given color by the given fraction.
     * @param color
     * @param fraction
     * @return
     */
    public static Color darken(Color color, double fraction) {
        double red =  (Math.max(0d, color.getRed() - 1d * fraction));
        double green = (Math.max(0d, color.getGreen() - 1d * fraction));
        double blue =  (Math.max(0d, color.getBlue() - 1d * fraction));

        double alpha = color.getOpacity();

        return new Color(red, green, blue, alpha);
    }


    /**
     * Averages the RGB values of a random color with those of a constant color.
     * Returns the averaged random color.
     * Credits to David Crow
     * (http://stackoverflow.com/questions/43044/algorithm-to-randomly-generate-an-aesthetically-pleasing-color-palette)
     * @param mix The constant color by which the random colors should get averaged
     * @return
     */
    public static Color generateMixedRandomColor(Color mix) {
        Random random = new Random();

        double red = random.nextDouble();
        double green = random.nextDouble();
        double blue = random.nextDouble();

        // Mix the color
        if (mix != null) {
            red =   (red + mix.getRed()) / 2;
            green = (green + mix.getGreen()) / 2;
            blue = (blue + mix.getBlue()) / 2;
        }

        return Color.color(red, green, blue);
    }

    /**
     * Returns a purely, randomly generated color.
     * @return
     */
    public static Color generateRandomColor(){
        Random r = new Random();

        int rCol1 = r.nextInt(256);
        int rCol2 = r.nextInt(256);
        int rCol3 = r.nextInt(256);

        return Color.rgb(rCol1,rCol2,rCol3);
    }
}
