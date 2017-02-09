package view.table;

import io.datastructure.Entry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 01.02.17.
 */
public class TableControllerDB extends ATableController{



    public TableControllerDB(){
        super();
    }


    public void addButtonFunctionality(Button addAllBtn, Button addSelectedBtn,
                   TableControllerDB tableControllerDB, TableControllerUserBench tableControllerUserBench) {

        addAllBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                // parse data back to observable list
                ObservableList selected = tableControllerDB.getTable().getItems();
                ObservableList data_obs = FXCollections.observableArrayList();
                data_obs.addAll(selected);
                HashMap<String, List<Entry>> data_entries = tableControllerDB.createNewEntryListDragAndDrop(data_obs);
                tableControllerUserBench.updateTable(data_entries);
            }
        });

        addSelectedBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                // parse data back to observable list
                ObservableList selected = tableControllerDB.getTable().getSelectionModel().getSelectedItems();
                if(selected.size()!=0){
                    ObservableList data_obs = FXCollections.observableArrayList();
                    data_obs.addAll(selected);
                    HashMap<String, List<Entry>> data_entries = tableControllerDB.createNewEntryListDragAndDrop(data_obs);
                    tableControllerUserBench.updateTable(data_entries);
                }

            }
        });
    }
}
