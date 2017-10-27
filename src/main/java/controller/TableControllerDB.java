package controller;

import Logging.LogClass;
import io.datastructure.Entry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import org.controlsfx.control.table.TableFilter;
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

    public void addFilter(){
        TableFilter filter = new TableFilter(table);
    }

    public void addButtonFunctionality(Button addAllBtn, Button addSelectedBtn, Button disableBtn, MitoBenchWindow mitoBenchWindow) {

        addAllBtn.setOnAction(e -> {
            // parse data back to observable list
            ObservableList selected = getTable().getItems();
            ObservableList data_obs = FXCollections.observableArrayList();
            data_obs.addAll(selected);
            HashMap<String, List<Entry>> data_entries = createNewEntryListDragAndDrop(data_obs);
            mitoBenchWindow.getTableControllerUserBench().updateTable(data_entries);
        });

        addSelectedBtn.setOnAction(e -> {
            // parse data back to observable list
            ObservableList selected = table.getSelectionModel().getSelectedItems();
            if(selected.size()!=0){
                ObservableList data_obs = FXCollections.observableArrayList();
                data_obs.addAll(selected);
                HashMap<String, List<Entry>> data_entries = createNewEntryListDragAndDrop(data_obs);
                mitoBenchWindow.getTableControllerUserBench().updateTable(data_entries);
            }

        });

        disableBtn.setOnAction(e -> {
            //table.setVisible(false);
            mitoBenchWindow.disableBDTable();
            mitoBenchWindow.getEnableDBBtn().setVisible(true);
        });
    }
}
