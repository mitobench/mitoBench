package view.controls.sunburst;

import view.helpers.ColorHelper;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link IColorStrategy} which assigns each sector a distinct color tone
 * and colorizes the different levels of a sector with shades of this color tone.
 */
public class ColorStrategySectorShades implements IColorStrategy{

    /***************************************************************************
     *                                                                         *
     * Private fields                                                          *
     *                                                                         *
     **************************************************************************/

    private final List<Color> sectorColors = new ArrayList<>();
    private final double maxShades = 8;

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a new ColorStrategySectorShades {@link IColorStrategy}
     */
    public ColorStrategySectorShades(){

        // We define some nice looking colors for our first few sectors
        sectorColors.add(Color.CORNFLOWERBLUE);
        sectorColors.add(Color.LIGHTGREEN);
        sectorColors.add(Color.web("98689C")); // Purple
        sectorColors.add(Color.CHOCOLATE);
        sectorColors.add(Color.web("B21217")); // Dark Red
        sectorColors.add(Color.web("E98A2B")); // Orange
        sectorColors.add(Color.web("F6EC46")); // Yellow
        sectorColors.add(Color.web("00A1DB")); // Blue
        sectorColors.add(Color.web("DF2126")); // Red

    }

    /***************************************************************************
     *                                                                         *
     * Overridden Public API                                                   *
     *                                                                         *
     **************************************************************************/

    @Override
    public Color colorFor(WeightedTreeItem<?> item, int sector, int level) {
        Color color = getSectorColor(sector);
        if(level > 0) {
            color = ColorHelper.brighten(color, level / maxShades);
        }
        return color;
    }

    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/

    private Color getSectorColor(int sector){
        while(sectorColors.size() <= (sector)){
            sectorColors.add(ColorHelper.generateMixedRandomColor(Color.WHITE));
        }
        return sectorColors.get(sector);
    }
}
