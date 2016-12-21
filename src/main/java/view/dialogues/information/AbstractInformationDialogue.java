package view.dialogues.information;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


/**
 * Created by peltzer on 02/12/2016.
 */
public class AbstractInformationDialogue {

    public AbstractInformationDialogue(String title, String message, String id) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId(id + "button");
        alert.getDialogPane().setId(id);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


