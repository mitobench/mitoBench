package view.controls.sunburst;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Represents an enumeration unit of the legend. It consists of an enumeration sign,
 * represented by a rectangle and a label.
 * Created by Eric on 09.05.2014.
 */
public class LegendItem extends HBox{

    /***************************************************************************
     *                                                                         *
     * Private fields                                                          *
     *                                                                         *
     **************************************************************************/

    private Rectangle rect;
    private Label label;


    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a new LegendItem with the given color and text
     * @param color
     * @param text
     */
    public LegendItem(Color color, String text) {
        this.label = new Label(text);

        this.rect = new Rectangle();
        this.rect.setHeight(15);
        this.rect.setWidth(10);
        this.rect.setFill(color);

        getChildren().add(rect);
        getChildren().add(label);

        this.getStyleClass().add("legend-item");
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/


    /**
     * Sets the color of the enumeration sign.
     * @param color
     */
    public void setRectColor(Color color) {
        this.rect.setFill(color);
    }

    /**
     * Sets the text of the enumeration unit.
     * @param text
     */
    public void setLabelText(String text) {
        this.label.setText(text);
    }

}
