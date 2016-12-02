package view.dialogues.information;

import javafx.scene.control.Alert;


/**
 * Created by peltzer on 02/12/2016.
 */
public class AbstractInformationDialogue {

    public AbstractInformationDialogue(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}


