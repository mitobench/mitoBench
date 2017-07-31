package view.dialogues.settings;

import Logging.LogClass;
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
import controller.ATableController;


/**
 * Created by neukamm on 26.11.2016.
 */
public class AddDataToColumnDialog extends APopupDialogue{


    private final TextField entry_field;
    private ComboBox comboBox;
    private ATableController tableController;


    public AddDataToColumnDialog(String title, ATableController tableController, LogClass logClass) {
        super(title, logClass);
        this.tableController = tableController;
        // Elements
        ObservableList<String> comboEntries = FXCollections.observableArrayList();

        for(String s : tableController.getCurrentColumnNames()){
            comboEntries.add(s);
        }

        comboBox = new ComboBox();
        comboBox.getItems().addAll(comboEntries);
        comboBox.getSelectionModel().selectFirst();
        entry_field = new TextField();


        Button okButton = new Button("OK");
        addButtonListener(okButton);

        dialogGrid.add(new Label("Select column"), 0,0);
        dialogGrid.add(comboBox, 1,0);
        dialogGrid.add(new Label("Entry"), 0,1);
        dialogGrid.add(entry_field, 1,1);
        dialogGrid.add(okButton, 1,2);
        show();
    }

    private void addButtonListener(Button okButton){



        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                // add elements to group

                //groupController.addElements(groupItems, comboBox.getValue().toString());
                tableController.updateTable(tableController.createNewEntryList(entry_field.getText(), comboBox.getValue().toString()));
                close();



            }
        });


        DropShadow shadow = new DropShadow();
        //Adding the shadow when the mouse cursor is on
        okButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        okButton.setEffect(shadow);
                    }
                });
        //Removing the shadow when the mouse cursor is off
        okButton.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        okButton.setEffect(null);
                    }
                });

    }


}