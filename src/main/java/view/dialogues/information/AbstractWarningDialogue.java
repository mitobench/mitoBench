package view.dialogues.information;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Created by neukamm on 18.05.17.
 */
public class AbstractWarningDialogue {

    protected final Alert alert;
    public AbstractWarningDialogue(String title, String message, String header, String id) {
        alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId(id + "button");
        alert.getDialogPane().setId(id);
        alert.setContentText(message);
    }

    protected void showAndWait(){
        alert.showAndWait();
    }

}
