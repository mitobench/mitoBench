package view.dialogues.information;

/**
 * Created by neukamm on 18.05.17.
 */
public class InformationDialogue extends AbstractWarningDialogue{
    public InformationDialogue(String title, String message, String header, String id) {
        super(title, message, header, id);
        showAndWait();
    }
}
