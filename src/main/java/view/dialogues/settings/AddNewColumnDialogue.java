package view.dialogues.settings;

import Logging.LogClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import controller.ATableController;


/**
 * Created by neukamm on 26.11.2016.
 */
public class AddNewColumnDialogue extends APopupDialogue{


    private final TextField dataField;
    private TextField columnNameField;
    private ATableController controller;

    public AddNewColumnDialogue(String title, ATableController tableController, LogClass logClass){
        super(title, logClass);
        controller = tableController;

        // Elements
        this.columnNameField = new TextField();
        this.dataField = new TextField();

        Button okButton = new Button("OK");
        addButtonListener(okButton);

        dialogGrid.add(new Label("Name of column:"), 0,0);
        dialogGrid.add(columnNameField, 1,0);
        //dialogGrid.add(new Label("Entry:"), 0, 1);
        //dialogGrid.add(dataField, 1, 1);
        dialogGrid.add(okButton, 1,2);
        show();

    }

    private void addButtonListener(Button okButton){

        okButton.setOnAction(e -> {
            // create new Group
            String colName = columnNameField.getText();
            LOG.info("Add new column: " + colName);
            controller.updateTable(controller.createNewEntryList(dataField.getText(), colName, true));
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