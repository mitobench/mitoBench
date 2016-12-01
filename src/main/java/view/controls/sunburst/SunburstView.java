package view.controls.sunburst;

import com.sun.javafx.event.EventHandlerManager;
import view.controls.skin.SunburstViewSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;

/**
 * A Sunburst View visualizes weighted hierarchical view.data structures.
 * Refer to the view.data model for more information.
 *
 */
public class SunburstView<T> extends Control {

    /***************************************************************************
     *                                                                         *
     * Private fields                                                          *
     *                                                                         *
     **************************************************************************/

    private final EventHandlerManager eventHandlerManager = new EventHandlerManager(this);

    private final ObjectProperty<WeightedTreeItem<T>> rootItem = new SimpleObjectProperty<>(this, "rootItem", null);

    private final ObjectProperty<WeightedTreeItem<T>> selectedItem = new SimpleObjectProperty<>(this, "selectedItem", null);

    private final ObjectProperty<IColorStrategy> colorStrategy = new SimpleObjectProperty<>(this, "colorStrategy", new ColorStrategySectorShades());

    private final ObjectProperty<Integer> maxDeepness = new SimpleObjectProperty<>(this, "maxDeepness", 8);


    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a new SunburstView
     */
    public SunburstView(){
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }



    /***************************************************************************
     *                                                                         *
     * Events                                                                  *
     *                                                                         *
     **************************************************************************/


    // --- onVisualChanged

    /**
     * Represents an Event which is fired when the visual has changed.
     */
    @SuppressWarnings("serial")
    public static class VisualChangedEvent extends Event {


        @SuppressWarnings("rawtypes")
        public static final EventType<VisualChangedEvent> VISUAL_CHANGED =
                new EventType<>("VISUAL_CHANGED"); //$NON-NLS-1$
        /**
         * Creates a new event that can subsequently be fired.
         */
        public VisualChangedEvent() {
            super(VISUAL_CHANGED);
        }
    }

    /**
     * Callback property when the visual has changed
     */
    public final ObjectProperty<EventHandler<VisualChangedEvent>> onVisualChangedProperty() {
        return onVisualChanged;
    }
    public final void setOnVisualChanged(EventHandler<VisualChangedEvent> value) {
        onVisualChangedProperty().set(value);
    }
    public final EventHandler<VisualChangedEvent> getOnVisualChanged() {
        return onVisualChangedProperty().get();
    }
    private ObjectProperty<EventHandler<VisualChangedEvent>> onVisualChanged = new ObjectPropertyBase<EventHandler<VisualChangedEvent>>() {
        @SuppressWarnings("rawtypes")
        @Override protected void invalidated() {
            eventHandlerManager.setEventHandler(VisualChangedEvent.VISUAL_CHANGED, get());
        }

        @Override
        public Object getBean() {
            return SunburstView.this;
        }

        @Override
        public String getName() {
            return "onVisualChanged"; //$NON-NLS-1$
        }
    };

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    /** {@inheritDoc} */
    @Override public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        return tail.prepend(eventHandlerManager);
    }

    /**
     * Sets the root node of this view which makes up the whole view.data model.
     * It will set the selected item to this root item as well.
     * @param root
     */
    public void setRootItem(WeightedTreeItem<T> root){
        setSelectedItem(null);
        rootItem.set(root);
        setSelectedItem(root);
    }

    public WeightedTreeItem<T> getRootItem(){
        return rootItem.get();
    }

    /**
     * Sets the selected item which becomes the center item (parent) of the inner circle.
     * By default, the {link #getRootItem} is set as selected item.
     * @param root
     */
    public void setSelectedItem(WeightedTreeItem<T> root){
        selectedItem.set(root);
    }

    /**
     * Gets the current selected item, which is the item currently in the middle
     * of the Sunburst.
     * @return
     */
    public WeightedTreeItem<T> getSelectedItem(){
        return selectedItem.get();
    }

    /**
     * Gets the current strategy by which the sunburst donut units are colorized.
     * @return
     */
    public IColorStrategy getColorStrategy(){
        return colorStrategy.get();
    }

    /**
     * Sets the strategy by which the sunburst donut units are colorized.
     * @param colorStrategy
     */
    public void setColorStrategy(IColorStrategy colorStrategy) { this.colorStrategy.set(colorStrategy); }

    /**
     * Sets the maximum deepness to which the Sunburst shows the donut units.
     * @param maxDeepnessValue
     */
    public void setMaxDeepness(int maxDeepnessValue){
        maxDeepness.set(maxDeepnessValue);
    }

    /**
     * Returns the maximum deepness.
     * @return
     */
    public int getMaxDeepness(){
        return maxDeepness.get();
    }


    /**
     * Returns the color of the given item.
     * @param item The item for which the color should be returned
     * @return
     */
    public Color getItemColor(WeightedTreeItem<T> item) {

        Skin<?> skin = this.getSkin();
        //System.out.println(skin);
        Color color = null;
        if(skin instanceof SunburstViewSkin){
           //System.out.println("is SunburstViewSkin");
           color =((SunburstViewSkin) skin).getItemColor(item);
        }

        return color;

    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    public ObjectProperty<WeightedTreeItem<T>> rootItemProperty(){
        return rootItem;
    }

    public ObjectProperty<WeightedTreeItem<T>> selectedItemProperty() {
        return selectedItem;
    }

    public ObjectProperty<IColorStrategy> colorStrategy() {
        return colorStrategy;
    }

    public ObjectProperty<Integer> maxDeepness() {
        return maxDeepness;
    }

    /***************************************************************************
     *                                                                         *
     * Stylesheet Handling                                                     *
     *                                                                         *
     **************************************************************************/

    private static final String DEFAULT_STYLE_CLASS = "sunburst-view"; //$NON-NLS-1$

    /** {@inheritDoc} */
    @Override protected Skin<?> createDefaultSkin() {
        return new SunburstViewSkin<>(this);
    }

    /** {@inheritDoc} */
//    @Override public String getUserAgentStylesheet() {
//        String css = SunburstView.class.getResource("sunburstview.css").toExternalForm(); //$NON-NLS-1$
//        return css;
//    }

}