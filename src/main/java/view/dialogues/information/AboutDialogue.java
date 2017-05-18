package view.dialogues.information;

import javafx.scene.image.ImageView;

/**
 * Created by peltzer on 02/12/2016.
 */
public class AboutDialogue extends AbstractInformationDialogue {

    public AboutDialogue(String title, String message, String headertext, String id) {
        super(title, message, headertext, id);
        ImageView imageView = new ImageView("file:logo/mitoBenchLogo.jpg");
        imageView.setFitHeight(144);
        imageView.setFitWidth(200);
        alert.getDialogPane().setGraphic(imageView);

        showAndWait();
    }
}
