/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.FileHandling;

import javafx.stage.Stage;

import java.nio.file.Path;

public class FileDialogueImpl implements FileDialogue {
    private final FileDialogueType type;

    private final Stage stage;

    private Path selectedFile;

    public FileDialogueImpl(final FileDialogueType type, final Stage stage) {
        this.stage = stage;
        this.type = type;


    }

    @Override
    public void showChooser() {
        selectedFile = type.showChooser(stage);
    }

    @Override
    public boolean isFileSelected() {
        return selectedFile != null;
    }

    @Override
    public Path getSelectedFile() {
        return selectedFile;
    }
}
