package view.table;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;

import javafx.scene.control.TableView;
import javafx.scene.input.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by neukamm on 02.02.17.
 */
public class DrapAndDropEventMaganer {

    private TableControllerDB tableDBController;
    private TableControllerUserBench tableUserController;
    private DataFormat myformat = new DataFormat("tabledata");
    private ObservableList selected;

    public DrapAndDropEventMaganer(TableControllerDB tableDB, TableControllerUserBench tableUser){

        this.tableDBController = tableDB;
        this.tableUserController = tableUser;
    }

    public void createEvent(){

        // manage event handler for tableUserController
        tableUserController.getTable().setOnDragOver(event -> {
            System.out.println("Mouse dragged over user table");
            mouseDragOver(event);
        });


        tableUserController.getTable().setOnDragExited(event -> {
            System.out.println("Mouse dragged and exited user table");
            tableUserController.getTable().setStyle("-fx-border-color: #C6C6C6;");

        });

        tableUserController.getTable().setOnDragDropped(event -> {
            System.out.println("Mouse dragged dropped user table");
            mouseDragDropped(event);
        });



        // manage event handler for tableDBController


        tableDBController.getTable().setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // drag was detected, start drag-and-drop gesture
                selected = (ObservableList)tableDBController.getTable().getSelectionModel().getSelectedItem();
                // copy all elements to Array -> obs. list not serializable
                List data_copy = new ArrayList();
                data_copy.addAll(selected);

                if(selected !=null){

                    Dragboard db = tableDBController.getTable().startDragAndDrop(TransferMode.ANY);
                    ClipboardContent content = new ClipboardContent();
                    content.put(myformat,  data_copy);
                    db.setContent(content);
                    event.consume();
                }
            }
        });

        tableDBController.getTable().setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                // data is dragged over the target
                Dragboard db = event.getDragboard();
                if (event.getDragboard().hasString()){
                    event.acceptTransferModes(TransferMode.ANY);
                }
                event.consume();
            }
        });

    }


    private  void mouseDragOver(final DragEvent e) {
        final Dragboard db = e.getDragboard();

            tableUserController.getTable().setStyle("-fx-border-color: red;"
                    + "-fx-border-width: 5;"
                    + "-fx-background-color: #C6C6C6;"
                    + "-fx-border-style: solid;");
            e.acceptTransferModes(TransferMode.ANY);

    }


    private void mouseDragDropped(final DragEvent e) {
        final Dragboard db = e.getDragboard();
        boolean success = false;
        //if (db.hasFiles()) {
        //    success = true;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        addData(tableDBController.getTable(), tableUserController.getTable(), selected);
                    } catch (Exception ex) {
                        Logger.getLogger(DrapAndDropEventMaganer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        //}
        //e.setDropCompleted(success);
        //e.consume();
    }


    void addData(TableView tableFrom, TableView tableTo, List data){
        // parse data back to observable list
        ObservableList data_tmp = FXCollections.observableArrayList();
        data_tmp.addAll(data);
        if(tableFrom.getColumns().size() == tableTo.getColumns().size()){
            tableTo.getItems().add(data_tmp);
        }
    }


}
