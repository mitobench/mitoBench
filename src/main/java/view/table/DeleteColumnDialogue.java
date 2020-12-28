package view.table;

import Logging.LogClass;
import controller.ATableController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import view.dialogues.settings.APopupDialogue;

public class DeleteColumnDialogue extends APopupDialogue {

    private ComboBox comboBox;
    private ATableController tableController;

    public DeleteColumnDialogue(String title, ATableController controller, LogClass logClass) {
        super(title, logClass);
        this.tableController = controller;

        ObservableList<String> comboEntries = FXCollections.observableArrayList();

        for(String s : tableController.getCurrentColumnNames()){
            comboEntries.add(s);
        }

        comboBox = new ComboBox();
        comboBox.getItems().addAll(comboEntries);
        comboBox.getSelectionModel().selectFirst();

        Button okButton = new Button("OK");
        addButtonListener(okButton);

        dialogGrid.add(new Label("Select column"), 0,0);
        dialogGrid.add(comboBox, 1,0);
        dialogGrid.add(okButton, 1,1);
        show();

    }


    private void addButtonListener(Button okButton){


        okButton.setOnAction(e -> {
            // add elements to group
            tableController.removeColumn(comboBox.getValue().toString());
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
