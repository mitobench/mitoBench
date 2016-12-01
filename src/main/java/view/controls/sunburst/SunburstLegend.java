package view.controls.sunburst;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Represents the legend visual for a sunburst view.
 *
 */
public class SunburstLegend extends VBox {

    /***************************************************************************
     *                                                                         *
     * Private fields                                                          *
     *                                                                         *
     **************************************************************************/

    private final SunburstView<?> sunburstView;

    private int legendItemMax;

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a new SunburstLegend for the given SunburstView
     * @param sunburstView
     */
    public SunburstLegend(SunburstView sunburstView) {
        this.sunburstView = sunburstView;
        this.setAlignment(Pos.CENTER);

        this.getStyleClass().add("legend");

        this.setMinWidth(200);

        sunburstView.setOnVisualChanged(x -> updateLegend());

        setLegendItemMax(20);
    }


    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * Gets the maximum number of legend items which will be displayed
     * @return
     */
    public int getLegendItemMax() {
        return legendItemMax;
    }

    /**
     * Set the maximum number of legend items which will be displayed
     * @param legendItemMax
     */
    public void setLegendItemMax(int legendItemMax) {
        this.legendItemMax = legendItemMax;
    }

    /**
     * Removes all legend items
     */
    public void clearLegend() {
        this.getChildren().clear();
    }


    /***************************************************************************
     *                                                                         *
     * Private implementation                                                  *
     *                                                                         *
     **************************************************************************/

    /**
     * Updates the legend by setting the color and text values of the inner most units.
     * There will be generated as many LegendItems as needed.
     * This method is called by the updateSelectedItem method.
     */
    private void updateLegend() {

        clearLegend();

        WeightedTreeItem<?> currentRoot = sunburstView.getSelectedItem();
        int count = 0;
        for (WeightedTreeItem innerChild : currentRoot.getChildrenWeighted()) {

            if (count > legendItemMax) break;

            String value = innerChild.getValue().toString(); // TODO better string / description handling
            Color color = sunburstView.getItemColor(innerChild);
            System.out.println("Color for " + innerChild + " is: " + color);

            LegendItem item = new LegendItem(color, value);;

            this.getChildren().add(item);
            count++;
        }
    }
}
