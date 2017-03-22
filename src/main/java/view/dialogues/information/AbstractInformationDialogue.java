package view.dialogues.information;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * Created by peltzer on 02/12/2016.
 */
public class AbstractInformationDialogue {


    protected final Alert alert;

    public AbstractInformationDialogue(String title, String message, String header, String id) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId(id + "button");
        alert.getDialogPane().setId(id);
        alert.setContentText(message);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add( new Image("file:logo/mitoBenchLogo.jpg"));


    }

    protected void showAndWait(){
        alert.showAndWait();
    }
}


