package io.dialogues.Import;

import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by neukamm on 30.01.17.
 */
@Singleton
public class ImportDialogueFactoryImpl implements IImportDialogueFactory {

    @Inject
    public ImportDialogueFactoryImpl() {

    }

    @Override
    public ImportDialogueImpl create(Stage stage, boolean isTestMode) {
        return new ImportDialogueImpl(stage, isTestMode);
    }

}
