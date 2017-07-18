package view.table.controller;

import javafx.scene.input.DragEvent;

/**
 * Created by neukamm on 7/18/17.
 */
public interface IDragAndDropManager {

    void createEvent();

    void mouseDragOver(DragEvent e);

    void mouseDragDropped(DragEvent event);

}
