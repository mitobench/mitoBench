package view.dialogues.information;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.CancelButton;

public class DataValidationDialogue {
    private final Stage dialog;
    private final GridPane dialogGrid;
    private final boolean uploadPossible;


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
        addListener();

        show(600,400);
    }


    private void addComponents(String result, String log) {

        int row=0;

        Label textArea_message = new Label(result);
        Button upload_now_btn = new Button("Upload data now");
        Button upload_later_btn = new Button("Upload data later");
        if (!uploadPossible){
            upload_now_btn.setDisable(true);
            upload_later_btn.setDisable(true);
        }

        dialogGrid.add(textArea_message, 0, row, 2,1);
        dialogGrid.add(new Separator(), 0,++row, 2,1);
        dialogGrid.add(upload_now_btn, 0,++row, 1,1);
        dialogGrid.add(upload_later_btn, 1,row, 1,1);
        dialogGrid.add(new Separator(), 0,++row, 2,1);
        dialogGrid.add(new Label("Error log:"), 0,++row, 2,1);
        dialogGrid.add(new Label(log), 0,++row, 2,1);
    }

    private void addListener() {


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
}
