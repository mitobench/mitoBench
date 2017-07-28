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
            AddDataToColumnDialog AddToColumnDialog =
                    new AddDataToColumnDialog("",
                            controller, logClass);
        });

        menu.getItems().addAll(addNewGropuItem, addAllSelectedItem);
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
     */
    public String[] getSampleNames() {

        String[] ids = new String[getSelectedRows().size()];

        ObservableList<ObservableList> selection = getSelectedRows();
        int index_id = getColIndex("ID");

        int i = 0;
        for(ObservableList row : selection){
            ids[i] = (String) row.get(index_id);
            i++;
        }

        return ids;
    }
}
