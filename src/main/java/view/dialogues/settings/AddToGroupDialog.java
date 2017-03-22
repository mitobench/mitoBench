package view.dialogues.settings;

import Logging.LogClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import controller.GroupController;
import view.table.controller.ATableController;


/**
 * Created by neukamm on 26.11.2016.
 */
public class AddToGroupDialog extends APopupDialogue{


    private ComboBox comboBox;
    private Stage dialog;
    private ATableController tableController;


    public AddToGroupDialog(String title, GroupController groupController, ObservableList groupItems,
                            ATableController tableController, LogClass logClass) {
        super(title, logClass);
        this.tableController = tableController;
        // Elements
        ObservableList<String> comboEntries = FXCollections.observableArrayList();

        for(String s : groupController.getGroupnames()){
            comboEntries.add(s);
        }

        comboBox = new ComboBox<String>();
        comboBox.getItems().addAll(groupController.getGroupnames());
        comboBox.setEditable(true);


        Button okButton = new Button("OK");
        addButtonListener(okButton, groupController, groupItems);

        dialogGrid.add(new Label("Select group"), 0,0);
        dialogGrid.add(comboBox, 1,0);
        dialogGrid.add(okButton, 1,1);
        show();
    }

    private void addButtonListener(Button okButton, GroupController groupController, ObservableList groupItems){


        okButton.setOnAction(e -> {
            // add elements to group
            groupController.addElements(groupItems, comboBox.getValue().toString());
            tableController.updateTable(tableController.createNewEntryListForGrouping(comboBox.getValue().toString(), "Group (Grouping)"));
            close();

        });


        DropShadow shadow = new DropShadow();
        //Adding the shadow when the mouse cursor is on
        okButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> okButton.setEffect(shadow));
        //Removing the shadow when the mouse cursor is off
        okButton.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> okButton.setEffect(null));

    }


}
