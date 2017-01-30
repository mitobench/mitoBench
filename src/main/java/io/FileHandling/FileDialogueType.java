/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.FileHandling;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.function.BiFunction;

public enum FileDialogueType {
    NEW_SESSION("Select File To Analyse", FileFilters.INPUT_DOCUMENTS, FileChooser::showOpenDialog);
    private final String title;
    private final List<ExtensionFilter> filters;
    private final BiFunction<FileChooser, Window, File> openFunction;


    FileDialogueType(final String title, final List<ExtensionFilter> filters, final BiFunction<FileChooser, Window, File> openFunction) {
        this.title = title;
        this.filters = filters;
        this.openFunction = openFunction;

    }

    public Path showChooser(final Window window) {
        FileChooser chooser = new FileChooser();

        chooser.setTitle(title);
        chooser.getExtensionFilters().addAll(filters);

        File result = openFunction.apply(chooser, window);

        if (result == null) {
            return null;
        } else {
            Path file = result.toPath();
            return file;
        }
   }
}
