package analysis;

import IO.Writer;
import Logging.LogClass;
import io.dialogues.Export.DataChoiceDialogue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import methods.CalculatorStoneking;
import view.MitoBenchWindow;
import view.table.controller.TableControllerFstValues;
import view.table.controller.TableControllerMutations;
import view.table.controller.TableControllerUserBench;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 20.04.17.
 */
public class FstCalculationRunner {

    private HashMap<String, List<String>> data;
    private String selection;
    private TableControllerFstValues tableControllerFstValues;

    public FstCalculationRunner(MitoBenchWindow mito) throws IOException {
        askForGroups(mito);
        prepareData(selection, mito.getTableControllerUserBench());
        run(mito.getScene(), mito.getTabpane_statistics(), mito.getLogClass());
    }



    public void askForGroups(MitoBenchWindow mito) throws IOException {

        List<String> columns = mito.getTableControllerUserBench().getCurrentColumnNames();

        DataChoiceDialogue dataChoiceDialogue = new DataChoiceDialogue(columns);
        selection = dataChoiceDialogue.getSelected();

    }

    public void prepareData(String selection, TableControllerUserBench tableControllerUserBench){
        data = new HashMap<>();
        int colIndexSelection = tableControllerUserBench.getColIndex(selection);
        int colIndexSequence = tableControllerUserBench.getColIndex("MTSequence");

        ObservableList<ObservableList> table = tableControllerUserBench.getTable().getItems();
        for(ObservableList row : table){
            String group = (String)row.get(colIndexSelection);
            String sequence = (String)row.get(colIndexSequence);
            if(!data.containsKey(group)){
                List<String> sequences = new ArrayList<>();
                sequences.add(sequence);
                data.put(group, sequences);
            } else {
                List<String> tmp = data.get(group);
                tmp.add(sequence);
                data.put(group, tmp);
            }
        }



    }

    public void run(Scene scene, TabPane statsTabPane, LogClass logClass) throws IOException {
        CalculatorStoneking calculateStoneking = new CalculatorStoneking();
        calculateStoneking.calculateFst(data);
        double[][] fsts = calculateStoneking.getFsts();
        String[] groupnames = calculateStoneking.getGroupnames();

        // init table controller
        tableControllerFstValues = new TableControllerFstValues(logClass);
        tableControllerFstValues.init();

        // write results to table view
        System.out.println("Write values to table");
        TableView table_fst_values = writeToTable(fsts, groupnames, scene);
        Tab tab = new Tab();
        tab.setId("tab_fst_values");
        tab.setText("Fst values");
        tab.setContent(table_fst_values);
        statsTabPane.getTabs().add(tab);
        statsTabPane.getSelectionModel().select(tab);

        logClass.getLogger(this.getClass()).info("Calculate pairwise Fst values between following groups:\n" + groupnames);

        // write to file

        Writer writer = new Writer();
        writer.writeResultsToFile("resultsFstStatistics.tsv", fsts, groupnames);
    }

    private TableView writeToTable(double[][] fsts, String[] groupnames, Scene scene) {

        TableView<ObservableList> table = tableControllerFstValues.getTable();
        tableControllerFstValues.addColumn("", 0);
        int i = 1;
        for(String group : groupnames){
            tableControllerFstValues.addColumn(group, i);
            i++;
        }

        // add data (table content)
        // write population HG count information
        ObservableList<ObservableList> entries = FXCollections.observableArrayList();
        for(int j = 0; j < fsts.length ; j++){
            ObservableList  entry = FXCollections.observableArrayList();
            entry.add(groupnames[j]);
            for(int col=0; col<fsts[0].length; col++)
            {
                entry.add(fsts[col][j]);
            }

            entries.add(entry);
        }


        // clear Items in table
        table.getItems().removeAll(table.getItems());
        //FINALLY ADDED TO TableView
        table.getItems().addAll(entries);

        return table;
    }


}
