package analysis;

import IO.Writer;
import Logging.LogClass;
import io.dialogues.Export.DataChoiceDialogue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import methods.Calculator;
import methods.Filter;
import view.MitoBenchWindow;
import view.table.controller.TableControllerFstValues;
import view.table.controller.TableControllerUserBench;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 20.04.17.
 */
public class FstCalculationRunner {

    private final MitoBenchWindow mitobench;
    private HashMap<String, List<String>> data;
    private TableControllerFstValues tableControllerFstValues;
    private double[][] fsts;
    private String[] groupnames;

    public FstCalculationRunner(MitoBenchWindow mito) throws IOException {
        mitobench = mito;
        prepareData(mito.getTableControllerUserBench());
    }


    private void prepareData(TableControllerUserBench tableControllerUserBench){
        data = new HashMap<>();
        int colIndexSelection = tableControllerUserBench.getColIndex(mitobench.getGroupController().getColname_group());
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

    public void run(boolean runSlatkin,
                    boolean runReynolds) throws IOException {


        Filter filter = new Filter();
        List<Integer> usableLoci = filter.getUsableLoci(data, 'N', 0.05);

        Calculator calculater = new Calculator(usableLoci);
        calculater.calculateFst(data);
        fsts = calculater.getFsts();
        groupnames = calculater.getGroupnames();


        // todo: write result in an appropriate way
        if(runSlatkin){
            calculater.linearizeWithSlatkin(fsts);
        }
        if(runReynolds){
            calculater.linearizeWithReynold(fsts);
        }

        // init table controller
        tableControllerFstValues = new TableControllerFstValues(mitobench.getLogClass());
        tableControllerFstValues.init();


        // write to file
        Writer writer = new Writer();
        writer.writeResultsToFile("resultsFstStatistics.tsv", fsts, groupnames);
    }

    public TableView writeToTable() {

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


    public String[] getGroupnames() {
        return groupnames;
    }
}
