package controller;


import Logging.LogClass;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;

import view.MitoBenchWindow;
import view.dialogues.settings.AddDataToColumnDialog;
import view.dialogues.settings.AddNewColumnDialogue;
import view.dialogues.settings.CopyColumnDialogue;
import view.dialogues.settings.DeleteColumnDialogue;


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


    public void createContextMenu(){

        final ContextMenu menu = new ContextMenu();

        final MenuItem addNewGropuItem = new MenuItem("Add new column");
        addNewGropuItem.setOnAction(event -> {
            AddNewColumnDialogue addnewColumnDialogue =
                    new AddNewColumnDialogue("Add new column", controller, logClass);
        });

        final MenuItem addAllSelectedItem
                = new MenuItem("Add/replace data");
        addAllSelectedItem.setOnAction(event -> {
            AddDataToColumnDialog AddToColumnDialog = new AddDataToColumnDialog("",
                            controller, logClass);
        });


        final MenuItem deleteColumn = new MenuItem("Delete column");
        deleteColumn.setOnAction(event -> {
            DeleteColumnDialogue deleteColumnDialogue = new DeleteColumnDialogue("Delete column", controller, logClass);

        });

        final MenuItem copyColumn = new MenuItem("Copy column");
        copyColumn.setOnAction(event -> {
            CopyColumnDialogue deleteColumnDialogue = new CopyColumnDialogue("Copy column", controller, logClass);

        });

        menu.getItems().addAll(addNewGropuItem, addAllSelectedItem, copyColumn,  deleteColumn);
        table.setContextMenu(menu);

    }

    public void addButtonFunctionality(Button btn, MitoBenchWindow mitoBenchWindow){
        btn.setOnAction(e -> {
            mitoBenchWindow.enableBDTable();
            btn.setVisible(false);
        });
    }


    public void addDragAndDropFiles(MitoBenchWindow mitoBenchWindow){
        DragAndDropManagerInput dragAndDropManagerInput = new DragAndDropManagerInput(this, mitoBenchWindow);
        dragAndDropManagerInput.createEvent();

    }

    /**
     * This method returns all samples names.
     * @return
     * @param data
     */
    public String[] getSampleNames(ObservableList<ObservableList> data) {

        String[] ids = new String[getSelectedRows().size()];

        ObservableList<ObservableList> selection = data;
        int index_id = getColIndex("ID");

        int i = 0;
        for(ObservableList row : selection){
            ids[i] = (String) row.get(index_id);
            i++;
        }

        return ids;
    }

}