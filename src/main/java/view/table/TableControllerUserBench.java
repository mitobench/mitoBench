package view.table;


import io.datastructure.Entry;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import view.groups.AddToGroupDialog;
import view.groups.CreateGroupDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by neukamm on 07.11.16.
 */
public class TableControllerUserBench extends ATableController{

    public TableControllerUserBench() {
        super();
    }

    public void addRowListener(Label infolabel){
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            // update text
            infolabel.setText(table.getSelectionModel().getSelectedItems().size() + " rows are selected");
        });

    }

    public void createContextMenu(){

        final ContextMenu menu = new ContextMenu();

        final MenuItem addNewGropuItem = new MenuItem("Create new group");
        addNewGropuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CreateGroupDialog createGroupDialog =
                        new CreateGroupDialog(groupController, table, controller);
            }
        });

        final MenuItem addAllSelectedItem
                = new MenuItem("Add to group");
        addAllSelectedItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AddToGroupDialog addToGroupDialog =
                        new AddToGroupDialog(groupController, table.getSelectionModel().getSelectedItems(), controller);
            }
        });

        menu.getItems().addAll(addNewGropuItem, addAllSelectedItem);
        table.setContextMenu(menu);
    }

    @Override
    public void updateTable(HashMap<String, List<Entry>> input) {
        if(input != null){
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
                if(!curr_colnames.contains(s))
                    curr_colnames.add(s);
            }

            // display updated table
            data = parseDataTableToObservableList(dataTable, curr_colnames);


            // add columns
            int i = 0;
            for(String colName : curr_colnames) {
                int j = i;
                TableColumn col = new TableColumn(colName);
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                col_names.add(colName);
                table.getColumns().addAll(col);
                i++;

            }

            // clear Items in table
            table.getItems().removeAll(table.getItems());
            //FINALLY ADDED TO TableView
            table.getItems().addAll(data);

            setColumns_to_index();
        }

        editMenu.upateGroupItem(getCurrentColumnNames(), groupController);
    }

}
