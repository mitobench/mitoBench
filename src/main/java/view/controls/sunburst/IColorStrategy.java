package view.controls.sunburst;

import javafx.scene.paint.Color;

/**
 * Defines a strategy by which the color scheme of the sunburst view is set.
 */
public interface IColorStrategy {
    /**
     * Returns the color for the requested donut-unit.
     *
     * @param item The donut-unit model
     * @param sector The sector number
     * @param level The relative deepness level
     * @return
     */
    Color colorFor(WeightedTreeItem<?> item, int sector, int level);
}
