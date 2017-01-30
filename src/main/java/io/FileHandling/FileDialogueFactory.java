/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.FileHandling;

import javafx.stage.Stage;

public interface FileDialogueFactory {
    FileDialogue create(final FileDialogueType type, final Stage stage);
}
