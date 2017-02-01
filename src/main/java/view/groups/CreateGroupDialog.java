package view.groups;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.table.ATableController;
import view.table.TableControllerUserBench;


/**
 * Created by neukamm on 26.11.2016.
 */
public class CreateGroupDialog {


    private TextField groupnameField;
    private Stage dialog;
    private ATableController controller;

    public CreateGroupDialog(GroupController groupController, TableView table, ATableController tableController){

        controller = tableController;

        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        // Boxes
        VBox dialogVbox = new VBox(20);
        HBox hBox = new HBox(20);

        // Elements
        this.groupnameField = new TextField();

        Button okButton = new Button("OK");
        addButtonListener(okButton, groupController, table.getSelectionModel().getSelectedItems());


        hBox.getChildren().addAll(new Label("Groupname:"), groupnameField);
        dialogVbox.getChildren().addAll(hBox, okButton);
        dialogVbox.setAlignment(Pos.CENTER);

        Scene dialogScene = new Scene(dialogVbox, 270, 80);
        dialog.setScene(dialogScene);
        dialog.show();

    }

    private void addButtonListener(Button okButton, GroupController groupController, ObservableList groupItems){


        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                // create new Group
                String gName = groupnameField.getText();
                groupController.createGroup(gName);
                groupController.addElements(groupItems, gName);
                controller.updateTable(controller.createNewEntryList(groupnameField.getText()));
                dialog.close();
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


    public TextField getGroupnameField() {
        return groupnameField;
    }
}
