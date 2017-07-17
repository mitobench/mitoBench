package view.table.controller;

import javafx.collections.ObservableList;
import javafx.scene.input.*;
import view.MitoBenchWindow;

import java.io.File;

/**
 * Created by neukamm on 17.07.17.
 */
public class DragAndDropManagerInput {


    private TableControllerUserBench tableUserController;
    private DataFormat myformat;
    private ObservableList<ObservableList> selected;
    private MitoBenchWindow mito;

    public DragAndDropManagerInput(TableControllerUserBench tableUser, MitoBenchWindow mitoBenchWindow){

        if(myformat == null){
            myformat = new DataFormat("tabledataInput");
        }
        this.tableUserController = tableUser;
        mito = mitoBenchWindow;
    }

    public void createEvent(){

        // manage event handler for tableUserController
        tableUserController.getTable().setOnDragOver(event -> {
            mouseDragOver(event);
        });


        tableUserController.getTable().setOnDragExited(event -> tableUserController.getTable().setStyle("-fx-border-color: #C6C6C6;"));

        tableUserController.getTable().setOnDragDropped(event -> mouseDragDropped());



    }


    private  void mouseDragOver(final DragEvent e) {
        tableUserController.getTable().setStyle("-fx-border-color: #ff525e;"
                + "-fx-border-width: 5;"
                + "-fx-background-color: #C6C6C6;"
                + "-fx-border-style: solid;");
        e.acceptTransferModes(TransferMode.ANY);
    }


    private void mouseDragDropped() {

        tableUserController.getTable().setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = true;
                for (File file : db.getFiles()) {
                    mito.getFileMenu().openFile(file);
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

    }


}
