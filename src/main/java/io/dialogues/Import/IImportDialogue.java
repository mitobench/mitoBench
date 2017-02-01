package io.dialogues.Import;

import java.io.File;

/**
 * Created by neukamm on 30.01.17.
 */
public interface IImportDialogue {

    void start();

    boolean isFileSelected();

    File getSelectedFile();



}
