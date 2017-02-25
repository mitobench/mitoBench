package view.dialogues.settings;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import view.groups.GroupController;
import view.table.controller.ATableController;


/**
 * Created by neukamm on 26.11.2016.
 */
public class CreateGroupDialog extends APopupDialogue{


    private TextField groupnameField;
    private ATableController controller;

    public CreateGroupDialog(String title, GroupController groupController, TableView table, ATableController tableController){
        super(title);
        controller = tableController;

        // Elements
        this.groupnameField = new TextField();

        Button okButton = new Button("OK");
        addButtonListener(okButton, groupController, table.getSelectionModel().getSelectedItems());

        dialogGrid.add(new Label("Groupname:"), 0,0);
        dialogGrid.add(groupnameField, 1,0);
        dialogGrid.add(okButton, 1,1);
        show();

    }

    private void addButtonListener(Button okButton, GroupController groupController, ObservableList groupItems){

        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                // create new Group
                String groupName = groupnameField.getText();
                //groupController.createGroup(gName);
                groupController.createGroupByColumn("Group", groupName);
                controller.updateTable(controller.createNewEntryListForGrouping(groupnameField.getText(), "Group (Grouping)"));
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
