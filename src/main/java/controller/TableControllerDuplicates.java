package controller;

import Logging.LogClass;
import io.datastructure.Entry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TableControllerDuplicates{
    private final TableView table;
    private List<String> colnames=new ArrayList<>();
    private ArrayList<Object> col_names_sorted;

    public TableControllerDuplicates(LogClass logClass) {
        table = new TableView();
        table.setEditable(true);
    }

    /**
     * Override update method
     *
     * @param duplicates_database list with all duplicates found in database
     */


    public void updateTable(HashMap<Integer, List<Entry>> duplicates_database) {

        table.getItems().clear();

        // find all column names and add columns
        for (int i : duplicates_database.keySet()){
            List<Entry> entries = duplicates_database.get(i);
            for (Entry e : entries){
                addColumn(e.getIdentifier());
            }
        }

        defineColOrder();


        for (int i : duplicates_database.keySet()) {
            addData(duplicates_database.get(i));
        }

    }

    private void defineColOrder() {


        col_names_sorted = new ArrayList<>();

            // set default column order (ID -> LabSampleID -> author -> Haplogroup -> Population -> others)
            if (colnames.contains("ID")) {
                col_names_sorted.add("ID");
                colnames.remove("ID");
            }

            if (colnames.contains("Labsample ID")) {
                col_names_sorted.add("Labsample ID");
                colnames.remove("Labsample ID");
            }

        if (colnames.contains("Author")) {
            col_names_sorted.add("Author");
            colnames.remove("Author");
        }

            if (colnames.contains("Haplogroup")) {
                col_names_sorted.add("Haplogroup");
                colnames.remove("Haplogroup");
            }

            if (colnames.contains("Population")) {
                col_names_sorted.add("Population");
                colnames.remove("Population");
            }


            boolean containsSeq = false;
            if (colnames.contains("MTSequence")) {
                colnames.remove("MTSequence");
                containsSeq = true;
            }

            Collections.sort(colnames);
            col_names_sorted.addAll(colnames);
            if (containsSeq)
                col_names_sorted.add("MTSequence");


    }

    private void addData(List<Entry> entries) {
        ObservableList parsedData = FXCollections.observableArrayList();

    }


    /**
     * Add new column to table
     *
     * @param colname
     */
    public void addColumn(String colname) {
        if(!colnames.contains(colname)){
            TableColumn col = new TableColumn(colname);
            table.getColumns().addAll(col);
        }
    }


}

