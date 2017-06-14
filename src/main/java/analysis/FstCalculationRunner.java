package analysis;


import IO.writer.Writer;
import fst.FstCalculator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

import methods.Filter;
import view.MitoBenchWindow;
import view.table.MTStorage;
import view.table.controller.TableControllerFstValues;
import view.table.controller.TableControllerUserBench;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 20.04.17.
 */
public class FstCalculationRunner {

    private final MitoBenchWindow mitobench;
    private final String distance_type;
    private double gamma_a;
    private char missing_data_character;
    private HashMap<String, List<String>> data;
    private TableControllerFstValues tableControllerFstValues;
    private double[][] fsts;
    private double[][] fsts_slatkin=null;
    private double[][] fsts_reynolds=null;
    private String[] groupnames;
    private List<Integer> usableLoci;
    private Writer writer;

    public FstCalculationRunner(MitoBenchWindow mito, String type, double gamma, char missing_data_character)
            throws IOException {
        mitobench = mito;
        distance_type = type;
        gamma_a = gamma;
        this.missing_data_character = missing_data_character;

        prepareData(mito.getTableControllerUserBench(), mitobench.getTableControllerUserBench().getDataTable().getMtStorage());
    }


    private void prepareData(TableControllerUserBench tableControllerUserBench, MTStorage mtStorage){
        data = new HashMap<>();
        int colIndexSelection = tableControllerUserBench.getColIndex(mitobench.getGroupController().getColname_group());
        //int colIndexSequence = tableControllerUserBench.getColIndex("MTSequence");
        int colIndexSequence = tableControllerUserBench.getColIndex("ID");

        ObservableList<ObservableList> table = tableControllerUserBench.getTable().getItems();
        for(ObservableList row : table){
            String group = (String)row.get(colIndexSelection);
            String id = (String)row.get(colIndexSequence);
            String sequence = mtStorage.getData().get(id);
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

    public void run(boolean runSlatkin, boolean runReynolds, String field_level_missing_data) throws IOException {


        Filter filter = new Filter();

        usableLoci = filter.getUsableLoci(
                data,
                missing_data_character,
                Double.parseDouble(field_level_missing_data)
        );

        FstCalculator calculater = new FstCalculator(
                usableLoci,
                filter.getNumberOfTotalLoci()
        );

        calculater.calculateFst(data);
        fsts = calculater.getFsts();
        groupnames = calculater.getGroupnames();


        // todo: write result in an appropriate way
        if(runSlatkin){
            fsts_slatkin = calculater.linearizeWithSlatkin(fsts);
        }
        if(runReynolds){
            fsts_reynolds = calculater.linearizeWithReynolds(fsts);
        }

        // init table controller
        tableControllerFstValues = new TableControllerFstValues(mitobench.getLogClass());
        tableControllerFstValues.init();


        // write to file
        writer = new Writer();
        writer.writeResultsFstToString(fsts,
                groupnames,
                usableLoci,
                Double.parseDouble(field_level_missing_data)
        );


    }


    public void writeToTable() {

        writeTable(
                fsts,
                "Fst values",
                "fst_values"
        );

//        if(fsts_slatkin != null)
//            writeTable(fsts_slatkin, "Fst values (Slatkin)", "fst_values_slatkin");
//        if(fsts_reynolds != null)
//            writeTable(fsts_reynolds, "Fst values (Reynolds)", "fst_values_reynolds");



    }

    private void writeTable(double[][] fsts, String tab_header, String id){

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
                entry.add(round(fsts[col][j],5));
            }
            entries.add(entry);
        }


        // clear Items in table
        table.getItems().removeAll(table.getItems());
        //FINALLY ADDED TO TableView
        table.getItems().addAll(entries);

//        Tab tab = new Tab();
//        tab.setId("tab_" + id);
//        tab.setText(tab_header);
//        tab.setContent(table);


        Label textArea_result = new Label(writer.getResult_as_string());

        Tab tab = new Tab();
        tab.setId("tab_" + id);
        tab.setText(tab_header);
        tab.setContent(textArea_result);

        mitobench.getTabpane_statistics().getTabs().add(tab);


    }


    public static BigDecimal round(double value, int numberOfDigitsAfterDecimalPoint) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint,
                BigDecimal.ROUND_HALF_EVEN);
        return bigDecimal;
    }


    public void writeToFile(String path) throws IOException {
        writer.writeResultsToFile(path+ File.separator+"mitoBench_results_fst.txt");
    }


    public String[] getGroupnames() {
        return groupnames;
    }

    public double[][] getFsts() {
        return fsts;
    }
}

