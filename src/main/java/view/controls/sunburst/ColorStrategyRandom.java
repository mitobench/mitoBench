package view.controls.sunburst;

import view.helpers.ColorHelper;
import javafx.scene.paint.Color;

/**
 * A simple strategy which assigns a random color to each donut-unit.
 */
public class ColorStrategyRandom implements IColorStrategy{

    /**
     * Returns a Color for the given item
     * @param item The donut-unit model
     * @param sector The sector number
     * @param level The relative deepness level
     * @return
     */
    @Override
    public Color colorFor(WeightedTreeItem<?> item, int sector, int level) {
        return ColorHelper.generateMixedRandomColor(Color.WHITE);
    }
}
