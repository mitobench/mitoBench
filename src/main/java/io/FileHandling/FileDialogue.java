/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.FileHandling;

import java.nio.file.Path;

public interface FileDialogue {
    void showChooser();

    boolean isFileSelected();

    Path getSelectedFile();
}
