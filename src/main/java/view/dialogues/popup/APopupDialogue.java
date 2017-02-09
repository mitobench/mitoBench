package view.dialogues.popup;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by neukamm on 09.02.17.
 */
public abstract class APopupDialogue {


    protected GridPane dialogVbox;
    protected Stage dialog;

    public APopupDialogue(String title){
        dialog = new Stage();
        dialog.setTitle(title);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(new Stage());

        dialogVbox = new GridPane();
        dialogVbox.setHgap(10);
        dialogVbox.setVgap(10);

    }

    /**
     * This method displays dialogue.
     */
    protected void show(){
        Scene dialogScene = new Scene(dialogVbox, 500, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }
}
