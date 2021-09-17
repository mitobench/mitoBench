package view.dialogues.information;

import controller.DialogueController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UnalignedSequencesDialogue {

    private final Stage dialog;
    private final GridPane dialogGrid;
    private final DialogueController dialogueController;
    private Label textArea_message;
    private Button cancel_btn;

    public UnalignedSequencesDialogue(String title, String message, DialogueController dialogueController){
        this.dialogueController = dialogueController;
        dialog = new Stage();
        dialog.setTitle(title);

        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(new Stage());

        dialogGrid = new GridPane();
        dialogGrid.setAlignment(Pos.CENTER);
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);
        dialogGrid.setPadding(new Insets(10,10,10,10));

        addComponents(message);
        show(600,400);

    }

    private void addComponents(String message) {


        int row=0;
        textArea_message = new Label(message);
        cancel_btn = new Button("Cancel");


        dialogueController.unalignedDialogue_setCancel_btn(cancel_btn, dialog);

        textArea_message.setPadding(new Insets(10,10,10,10));
        cancel_btn.setPadding(new Insets(10,10,10,10));

        dialogGrid.add(textArea_message, 0, row, 2,1);
        dialogGrid.add(cancel_btn, 1,++row, 1,1);

    }


    /**
     * This method displays dialogue.
     */
    public void show(int width, int height){
        Scene dialogScene = new Scene(dialogGrid, width, height);
        dialog.setScene(dialogScene);
        dialog.show();
    }


    public Stage getDialog() {
        return dialog;
    }

    public GridPane getDialogGrid() {
        return dialogGrid;
    }

    public Label getTextArea_message() {
        return textArea_message;
    }

    public void setTextArea_message(Label textArea_message) {
        this.textArea_message = textArea_message;
    }

    public Button getCancel_btn() {
        return cancel_btn;
    }

    public void setCancel_btn(Button cancel_btn) {
        this.cancel_btn = cancel_btn;
    }

    public DialogueController getDialogueController() {
        return dialogueController;
    }
}

