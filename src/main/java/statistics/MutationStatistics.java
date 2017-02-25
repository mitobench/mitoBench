package statistics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import view.table.controller.TableControllerMutations;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 24.02.17.
 */
public class MutationStatistics {


    private final TableControllerMutations tableController;

    public MutationStatistics() {
            tableController = new TableControllerMutations();
            tableController.init();
    }

    public void writeToTable(HashMap<String, List<String>> hgs_per_mutation, TabPane tabPane){
        Tab tab = new Tab();
        tab.setId("tab_stats_mutation_freq");
        tab.setText("Mutation frequency");

        List<String> keys = new ArrayList<>();
        keys.addAll(hgs_per_mutation.keySet());
        Collections.sort(keys);
        TableView<ObservableList> table = tableController.getTable();

        File f = new File("src/main/java/statistics/tableStyle.css");
        table.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

        // add columns
        tableController.addColumn("Mutation", 0);
        tableController.addColumn("Total Number", 1);
        tableController.addColumn("Frequency", 2);
        tableController.addColumn("Haplogroup", 3);

        TableColumn colHG = (TableColumn)tableController.getTable().getColumns().get(3);
        colHG.getStyleClass().add("align-header-left");

        // add data (table content)
        // write mutation information to table
        ObservableList<ObservableList> rows = FXCollections.observableArrayList();
        for(String mut : hgs_per_mutation.keySet()){
            ObservableList  row = FXCollections.observableArrayList();
            double freq = (double)hgs_per_mutation.get(mut).size() / (double) hgs_per_mutation.size();
            row.addAll(mut, hgs_per_mutation.get(mut).size(), freq, hgs_per_mutation.get(mut));
            rows.add(row);
        }

        // clear Items in table
        table.getItems().removeAll(table.getItems());
        //FINALLY ADDED TO TableView
        table.getItems().addAll(rows);

        tab.setContent(table);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

    }

}
