package view.dialogues.error;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;


/**
 * Created by neukamm on 12.07.17.
 */
public class DatabaseErrorDialogue {


    public DatabaseErrorDialogue() {

        Alert alert = createContextAlert();

        //Label label = new Label("The exception stacktrace was:");


        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        //expContent.add(label, 0, 0);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }

    private Alert createContextAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setContentText("Your username or password is not correct or the server cannot be reached.\nPlease try again.");
        return alert;
    }
}
