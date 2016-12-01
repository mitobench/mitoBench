package view.controls.sunburst;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

/**
 * A Tree Item which is weighted
 */
public class WeightedTreeItem<T> extends TreeItem<T> {

    /***************************************************************************
     *                                                                         *
     * Private Fields                                                          *
     *                                                                         *
     **************************************************************************/

    private final ObjectProperty<Double> weight = new SimpleObjectProperty<>(this, "weight", 0d);


    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/


    /**
     * Creates an empty WeightedTreeItem
     */
    public WeightedTreeItem() {
        this(0, null);
    }

    /**
     *
     * @param weightValue
     * @param value
     */
    public WeightedTreeItem(double weightValue, T value) {
        setWeight(weightValue);
        setValue(value);
    }


    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * The weight property
     * @return
     */
    public ObjectProperty<Double> weightProperty() {
        return weight;
    }

    /**
     * Get the weight of this item
     * @return
     */
    public final double getWeight() {
        return weightProperty().get();
    }

    /**
     * Set the weight of this item
     * @param weightValue
     */
    public void setWeight(double weightValue) {
        weightProperty().set(weightValue);
    }

    /**
     * Returns all children of this WeightedTreeItem
     * @return
     */
    public ObservableList<WeightedTreeItem<T>> getChildrenWeighted() {
        return (ObservableList<WeightedTreeItem<T>>) (ObservableList<?>) getChildren();
    }


    /**
     * Gets the relative weight of this node among its local level
     * The relative weight is in a range of [0.0 - 1.0]
     *
     * @return
     */
    public double getRelativeWeight() {
        double relativeWeight = 1;
        if (getParent() instanceof WeightedTreeItem<?>) {
            WeightedTreeItem<T> parent = (WeightedTreeItem<T>) getParent();
            relativeWeight = 1 / parent.getChildrenAbsoluteWeight() * getWeight();
        }
        return relativeWeight;
    }

    public String toString(){
        return "(" + getValue().toString() + " : " + getRelativeWeight()+ ")";
    }


    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/


    /**
     * Get the absolute total weight of the children.
     *
     * @return
     */
    private synchronized double getChildrenAbsoluteWeight() {
        double totalChildrenAbsoluteWeight = 0;
        for (WeightedTreeItem<T> child : getChildrenWeighted()) {
            totalChildrenAbsoluteWeight += child.getWeight();
        }
        return totalChildrenAbsoluteWeight;
    }
}
