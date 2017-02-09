package view.dialogues.popup;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by neukamm on 09.02.17.
 */
public abstract class APopupDialogue {


    protected GridPane dialogGrid;
    protected Stage dialog;

    public APopupDialogue(String title){
        dialog = new Stage();
        dialog.setTitle(title);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(new Stage());

        dialogGrid = new GridPane();
        dialogGrid.setAlignment(Pos.CENTER);
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);

    }

    /**
     * This method displays dialogue.
     */
    protected void show(){
        Scene dialogScene = new Scene(dialogGrid, 500, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }
}
