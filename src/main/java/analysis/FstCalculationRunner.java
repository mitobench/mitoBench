package analysis;

import IO.Writer;
import io.dialogues.Export.DataChoiceDialogue;
import javafx.collections.ObservableList;
import view.MitoBenchWindow;
import view.table.controller.TableControllerUserBench;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 20.04.17.
 */
public class FstCalculationRunner {

    private HashMap<String, List<String>> data;
    private String selection;

    public FstCalculationRunner(MitoBenchWindow mito) throws IOException {
        askForGroups(mito);
        prepareData(selection, mito.getTableControllerUserBench());
        run();
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

    public void run() throws IOException {
        //CalculatorStoneking calculateStoneking = new CalculatorStoneking();
        //calculateStoneking.calculateFst(data);
        //double[][] fsts = calculator.getFsts();
        //String[] groupnames = calculator.getGroupnames();

        //Writer writer = new Writer();
        //writer.writeResultsToFile("resultsFstStatistics.tsv", fsts, groupnames);
    }



}
