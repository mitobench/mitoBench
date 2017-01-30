package io.FileHandling;

import javafx.stage.FileChooser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by neukamm on 30.01.17.
 */
public final class FileFilters {

    private static final List<String> EXTENSIONS_ANY_TEXT = listOf("*.txt", "*.rtf");

    private static final List<String> EXTENSIONS_FASTA = listOf("*.fa", "*.fasta", "*.fas");

    private static final List<String> EXTENSIONS_ARP = listOf("*.arp");

    private static final List<String> EXTENSIONS_HSD = listOf( "*.hsd");

    private static final List<String> EXTENSIONS_TSV = listOf( "*.tsv");

    private static final List<String> EXTENSIONS_MITOPROJ = listOf( "*.mitoproj");


    private static final FileChooser.ExtensionFilter FILTER_ALL = new FileChooser.ExtensionFilter("All Files", "*.*");

    private static final FileChooser.ExtensionFilter FILTER_ANY_TEXT = new FileChooser.ExtensionFilter("Text Files", EXTENSIONS_ANY_TEXT);

    private static final FileChooser.ExtensionFilter FILTER_FASTA = new FileChooser.ExtensionFilter("Fasta Files", EXTENSIONS_FASTA);

    private static final FileChooser.ExtensionFilter FILTER_ARP = new FileChooser.ExtensionFilter("ARP Files", EXTENSIONS_ARP);

    private static final FileChooser.ExtensionFilter FILTER_HSD = new FileChooser.ExtensionFilter("HSD Files", EXTENSIONS_HSD);

    private static final FileChooser.ExtensionFilter FILTER_TSV = new FileChooser.ExtensionFilter("TSV Files", EXTENSIONS_TSV);

    private static final FileChooser.ExtensionFilter FILTER_MITOPROJ = new FileChooser.ExtensionFilter("Mitoproj Files", EXTENSIONS_MITOPROJ);

    public static final List<FileChooser.ExtensionFilter> INPUT_DOCUMENTS = listOf(FILTER_ANY_TEXT, FILTER_FASTA, FILTER_ARP, FILTER_HSD, FILTER_TSV, FILTER_MITOPROJ);




    private FileFilters() {
        // Prevent instantiation - all members are static
    }


    public static <T> List<T> listOf(final T... a) {
        return Collections.unmodifiableList(Arrays.asList(a));
    }


}
