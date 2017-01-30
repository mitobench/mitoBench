package io.dialogues.Import;

import javafx.stage.Stage;

/**
 * Created by neukamm on 30.01.17.
 */
public interface IImportDialogueFactory {
    IImportDialogue create(final Stage stage, boolean isTestMode);

}
