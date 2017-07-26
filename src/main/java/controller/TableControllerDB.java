package controller;

import Logging.LogClass;
import io.datastructure.Entry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import view.MitoBenchWindow;

import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 01.02.17.
 */
public class TableControllerDB extends ATableController {



    public TableControllerDB(LogClass logClass){
        super(logClass);
    }


    public void addButtonFunctionality(Button addAllBtn, Button addSelectedBtn, Button disableBtn, MitoBenchWindow mitoBenchWindow) {

        addAllBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                // parse data back to observable list
                ObservableList selected = getTable().getItems();
                ObservableList data_obs = FXCollections.observableArrayList();
                data_obs.addAll(selected);
                HashMap<String, List<Entry>> data_entries = createNewEntryListDragAndDrop(data_obs);
                mitoBenchWindow.getTableControllerUserBench().updateTable(data_entries);
//                if(!mitoBenchWindow.getTableControllerUserBench().getGroupController().isGroupingExists())
//                    mitoBenchWindow.getTableControllerUserBench().getGroupController().createInitialGrouping();

            }
        });

        addSelectedBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                // parse data back to observable list
                ObservableList selected = table.getSelectionModel().getSelectedItems();
                if(selected.size()!=0){
                    ObservableList data_obs = FXCollections.observableArrayList();
                    data_obs.addAll(selected);
                    HashMap<String, List<Entry>> data_entries = createNewEntryListDragAndDrop(data_obs);
                    mitoBenchWindow.getTableControllerUserBench().updateTable(data_entries);
               //     if(!mitoBenchWindow.getTableControllerUserBench().getGroupController().isGroupingExists())
               //         mitoBenchWindow.getTableControllerUserBench().getGroupController().createInitialGrouping();
                }

            }
        });

        disableBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                //table.setVisible(false);
                mitoBenchWindow.disableBDTable();
                mitoBenchWindow.getEnableDBBtn().setVisible(true);

            }
        });
    }
}
