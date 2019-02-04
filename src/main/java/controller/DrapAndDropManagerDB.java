package controller;

import io.datastructure.Entry;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.input.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by neukamm on 02.02.17.
 */
public class DrapAndDropManagerDB implements IDragAndDropManager {

    private TableControllerDB tableDBController;
    private TableControllerUserBench tableUserController;
    private DataFormat myformat;
    private ObservableList<ObservableList> selected;

    public DrapAndDropManagerDB(TableControllerDB tableDB, TableControllerUserBench tableUser){

        if(myformat == null){
            myformat = new DataFormat("tabledata");
        }

        this.tableDBController = tableDB;
        this.tableUserController = tableUser;
    }

    public void createEvent(){

        // manage event handler for tableUserController
        tableUserController.getTable().setOnDragOver(event -> mouseDragOver(event));
        tableUserController.getTable().setOnDragExited(event -> tableUserController.getTable().setStyle("-fx-border-color: #C6C6C6;"));
        tableUserController.getTable().setOnDragDropped(event -> mouseDragDropped(event));

        // manage event handler for tableDBController

        tableDBController.getTable().setOnDragDetected(event -> dragDetected(event));
        tableDBController.getTable().setOnDragOver(event -> dragOver(event));

    }


    public void mouseDragOver(final DragEvent e) {
        tableUserController.getTable().setStyle("-fx-border-color: #ff525e;"
                + "-fx-border-width: 5;"
                + "-fx-background-color: #C6C6C6;"
                + "-fx-border-style: solid;");
        e.acceptTransferModes(TransferMode.ANY);
    }


    public void mouseDragDropped(DragEvent event) {
        Platform.runLater(() -> {
            try {
                addData(tableDBController, tableUserController, selected);
            } catch (Exception ex) {
                Logger.getLogger(DrapAndDropManagerDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void dragDetected(MouseEvent event){
        // drag was detected, start drag-and-drop gesture
        selected = tableDBController.getTable().getSelectionModel().getSelectedItems();
        // copy all elements to Array -> obs. list not serializable
        List<List> data_list = new ArrayList();
        for(int i = 0; i < selected.size(); i++){
            List data_copy = new ArrayList();
            data_copy.addAll(selected.get(i));
            data_list.add(data_copy);
        }

        if(selected !=null){
            Dragboard db = tableDBController.getTable().startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.put(myformat,  data_list);
            db.setContent(content);
            event.consume();
        }
    }

    private void dragOver(DragEvent event){
        // data is dragged over the target
        if (event.getDragboard().hasString()){
            event.acceptTransferModes(TransferMode.ANY);
        }
        event.consume();
    }

    private void addData(ATableController tableFrom_controller, ATableController tableTo_controller, List data){
        // parse data back to observable list
        ObservableList data_obs = FXCollections.observableArrayList();
        data_obs.addAll(data);
        HashMap<String, List<Entry>> data_entries = tableFrom_controller.createNewEntryListDragAndDrop(data_obs);
        tableTo_controller.updateTable(data_entries);
    }


}
