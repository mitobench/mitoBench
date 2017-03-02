package view.table.controller;


import Logging.LogClass;
import io.datastructure.Entry;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;

import view.MitoBenchWindow;
import view.dialogues.settings.AddToGroupDialog;
import view.dialogues.settings.CreateGroupDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Set;


/**
 * Created by neukamm on 07.11.16.
 */
public class TableControllerUserBench extends ATableController {

    public TableControllerUserBench(LogClass logClass) {
        super(logClass);
    }

    public void addRowListener(Label infolabel){
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            // update text
            infolabel.setText(table.getSelectionModel().getSelectedItems().size() + " rows are selected");
        });

    }

    @Override
    public void updateTable(HashMap<String, List<Entry>> input) {

        // update Entry structure
        updateEntryList(input);

        // add new values to existing one (DataTable)
        dataTable.update(input);

        // clean whole table
        data.clear();

        // get current col names
        List<String> curr_colnames = getCurrentColumnNames();

        table.getColumns().removeAll(table.getColumns());

        // define column order
        Set<String> cols = dataTable.getDataTable().keySet();
        for(String s : cols) {
            if(!curr_colnames.contains(s.trim()))
                curr_colnames.add(s);
        }

        // display updated table
        data = parseDataTableToObservableList(dataTable, curr_colnames);

        // add columns
        for(int i = 0; i < col_names_sorted.size(); i++) {
            addColumn(col_names_sorted.get(i), i);
        }

        // clear Items in table
        table.getItems().removeAll(table.getItems());
        //FINALLY ADDED TO TableView
        table.getItems().addAll(data);

        setColumns_to_index();

        groupMenu.upateGroupItem(col_names_sorted, groupController);

        if(!groupController.isGroupingExists())
            groupController.createInitialGrouping();

    }




    public void createContextMenu(){

        final ContextMenu menu = new ContextMenu();

        final MenuItem addNewGropuItem = new MenuItem("Create new group");
        addNewGropuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CreateGroupDialog createGroupDialog =
                        new CreateGroupDialog("", groupController, controller, logClass);
            }
        });

        final MenuItem addAllSelectedItem
                = new MenuItem("Add to group");
        addAllSelectedItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AddToGroupDialog addToGroupDialog =
                        new AddToGroupDialog("", groupController, table.getSelectionModel().getSelectedItems(),
                                controller, logClass);
            }
        });

        menu.getItems().addAll(addNewGropuItem, addAllSelectedItem);
        table.setContextMenu(menu);


    }

    public void addButtonFunctionality(Button btn, MitoBenchWindow mitoBenchWindow){
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                mitoBenchWindow.enableBDTable();
                btn.setVisible(false);
            }
        });
    }

}
