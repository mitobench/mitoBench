package controller;


import Logging.LogClass;

import io.datastructure.Entry;
import io.datastructure.generic.GenericInputData;
import io.inputtypes.CategoricInputType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import view.MitoBenchWindow;
import view.table.AddDataToColumnDialog;
import view.table.AddNewColumnDialogue;
import view.table.CopyColumnDialogue;
import view.table.DeleteColumnDialogue;
import view.table.TableContextMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by neukamm on 07.11.16.
 */
public class TableControllerUserBench extends ATableController {

    public TableControllerUserBench(LogClass logClass) {

        super(logClass);

    }

    public void createContextMenu(){
        TableContextMenu tableContextMenu = new TableContextMenu();

        tableContextMenu.getAddNewGropuItem().setOnAction(event -> {
            AddNewColumnDialogue addnewColumnDialogue =
                    new AddNewColumnDialogue("Add new column", controller, logClass);
        });
        tableContextMenu.getAddAllSelectedItem().setOnAction(event -> {
            AddDataToColumnDialog AddToColumnDialog = new AddDataToColumnDialog("", controller, logClass);
        });

        tableContextMenu.getDeleteSelectedRows().setOnAction(event -> {
            ObservableList<ObservableList> rows_to_delete = table.getSelectionModel().getSelectedItems();
            int index_accID = getColIndex("ID");
            if(index_accID != -1){

                HashMap<String, List<Entry>> input_new = (HashMap<String, List<Entry>>) table_content.clone();
                for(ObservableList<String> row : rows_to_delete){
                    // get data backup, delete rows, add data again
                    input_new.remove(row.get(index_accID));
                }
                // clear table
                column_to_index.clear();
                col_names.clear();
                table_content.clear();
                if(col_names_sorted!=null)
                    col_names_sorted.clear();
                // clean data model
                data.removeAll(data);
                // clean table view
                table.setItems(FXCollections.emptyObservableList());
                dataTable.getMtStorage().getData().clear();
                dataTable.getDataTable().clear();
                table.getColumns().removeAll(table.getColumns());

                // add updated data again
                updateTable(input_new);
                LOG.info(rows_to_delete.size() + " rows deleted.");
            } else {
                System.err.println("Wrong column name (value: -1)");
            }

        });

        tableContextMenu.getDeleteColumn().setOnAction(event -> {
            DeleteColumnDialogue deleteColumnDialogue = new DeleteColumnDialogue("Delete column", controller, logClass);

        });

        tableContextMenu.getCopyColumn().setOnAction(event -> {
            CopyColumnDialogue deleteColumnDialogue = new CopyColumnDialogue("Copy column", controller, logClass);

        });

        table.setContextMenu(tableContextMenu.getMenu());

    }

    public void addDragAndDropFiles(MitoBenchWindow mitoBenchWindow){
        DragAndDropManagerInput dragAndDropManagerInput = new DragAndDropManagerInput(this, mitoBenchWindow);
        dragAndDropManagerInput.createEvent();

    }


    /**
     * Test if all sequences have equal length.
     *
     * @param ids_table_col
     * @return
     */
    public boolean sequencesHaveSameLength(TableColumn ids_table_col){
        HashMap<String, String> sequences = new HashMap<>();
        data.stream().forEach((o)
                -> sequences.put((String) ids_table_col.getCellData(o),
                this.dataTable.getMtStorage().getData().get(ids_table_col.getCellData(o)))
        );

        int length = -1;

        for(String acc : sequences.keySet()){
            String seq = sequences.get(acc);
            if(length == -1){
                length = seq.length();
            } else {
                if(length != seq.length()){
                    return false;
                }
            }
        }
        return true;
    }

}