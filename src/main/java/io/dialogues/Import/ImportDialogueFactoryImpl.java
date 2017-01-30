package io.dialogues.Import;

/**
 * Created by neukamm on 30.01.17.
 */
public class ImportDialogueFactoryImpl implements ImportDialogueFactory {
    @Override
    public ImportDialogue create() {
        return new ImportDialogue();
    }
}
