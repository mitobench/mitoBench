package view.table;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

/**
 * Created by neukamm on 29.11.16.
 */
public class TableMover {

    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;
    private TableView table;

    public TableMover(TableView table){
        this.table = table;

    }

    public void setMoving(){
        table.setCursor(Cursor.HAND);
        table.setOnMousePressed(tableOnMousePressedEventHandler);
        table.setOnMouseDragged(tableOnMouseDraggedEventHandler);
    }


    public void setZooming(){
        table.setCursor(Cursor.HAND);
        table.setOnMousePressed(tableOnMousePressedEventHandler);
        table.setOnMouseDragged(tableOnMouseDraggedEventHandler);
    }



    EventHandler<MouseEvent> tableOnMousePressedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();
                    orgTranslateX = ((TableView)(t.getSource())).getTranslateX();
                    orgTranslateY = ((TableView)(t.getSource())).getTranslateY();
                }
            };

    EventHandler<MouseEvent> tableOnMouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;

                    ((TableView)(t.getSource())).setTranslateX(newTranslateX);
                    ((TableView)(t.getSource())).setTranslateY(newTranslateY);
                }
            };


}
