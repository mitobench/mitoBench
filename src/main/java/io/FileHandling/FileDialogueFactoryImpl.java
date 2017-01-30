/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.FileHandling;

import javafx.stage.Stage;
import javax.inject.Singleton;

@Singleton
public class FileDialogueFactoryImpl implements FileDialogueFactory {


    @Override
    public FileDialogue create(final FileDialogueType type, final Stage stage) {
        return new FileDialogueImpl(type, stage);
    }
}
