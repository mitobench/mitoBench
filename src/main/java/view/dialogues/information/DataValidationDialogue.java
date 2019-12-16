package view.dialogues.information;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.CancelButton;

public class DataValidationDialogue {
    private final Stage dialog;
    private final GridPane dialogGrid;
    private final boolean uploadPossible;
    private Button upload_later_btn;
    private Button upload_now_btn;


    public DataValidationDialogue(String title, String result, String log, boolean uploadPossible){
        dialog = new Stage();
        dialog.setTitle(title);

        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(new Stage());

        dialogGrid = new GridPane();
        dialogGrid.setAlignment(Pos.CENTER);
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);

        this.uploadPossible = uploadPossible;
        addComponents(result, log);

        show(600,400);
    }


    private void addComponents(String result, String log) {

        int row=0;
        ScrollPane scrollPane_log = new ScrollPane();
        scrollPane_log.setDisable(true);
        Label textArea_message = new Label(result);
        upload_now_btn = new Button("Upload data now");
        upload_later_btn = new Button("Upload data later");
        if (!uploadPossible){
            upload_now_btn.setDisable(true);
            upload_later_btn.setDisable(true);
            dialogGrid.setBackground(new Background(new BackgroundFill(Color.INDIANRED, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            dialogGrid.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        scrollPane_log.setContent(new Label(log));

        dialogGrid.add(textArea_message, 0, row, 2,1);
        dialogGrid.add(new Separator(), 0,++row, 2,1);
        dialogGrid.add(upload_now_btn, 0,++row, 1,1);
        dialogGrid.add(upload_later_btn, 1,row, 1,1);
        dialogGrid.add(new Separator(), 0,++row, 2,1);
        dialogGrid.add(new Label("Error log:"), 0,++row, 2,1);
        dialogGrid.add(scrollPane_log, 0,++row, 2,1);
    }


    /**
     * This method displays dialogue.
     */
    protected void show(){
        Scene dialogScene = new Scene(dialogGrid, 500, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * This method displays dialogue.
     */
    protected void show(int width, int height){
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

    public Button getUpload_later_btn() {
        return upload_later_btn;
    }

    public Button getUpload_now_btn() {
        return upload_now_btn;
    }
}
