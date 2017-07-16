package analysis;


import IO.reader.DistanceTypeParser;
import IO.writer.Writer;
import fst.FstHudson1992;
import fst.Linearization;
import fst.StandardAMOVA;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import methods.Filter;
import org.apache.log4j.Logger;
import view.MitoBenchWindow;
import view.charts.HeatChart;
import view.charts.HeatMap;
import view.charts.HeatMapLegend;
import view.table.MTStorage;
import view.table.controller.TableControllerFstValues;
import view.table.controller.TableControllerUserBench;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    private Logger LOG;


    public FstCalculationRunner(MitoBenchWindow mito, String type, double gamma, char missing_data_character)
            throws IOException {
        mitobench = mito;
        distance_type = type;
        gamma_a = gamma;
        this.missing_data_character = missing_data_character;
        LOG = mito.getLogClass().getLogger(this.getClass());

        prepareData(mito.getTableControllerUserBench(), mitobench.getTableControllerUserBench().getDataTable().getMtStorage());
    }


    private void prepareData(TableControllerUserBench tableControllerUserBench, MTStorage mtStorage){
        data = new HashMap<>();
        int colIndexSelection = tableControllerUserBench.getColIndex(mitobench.getGroupController().getColname_group());
        int colIndexSequence = tableControllerUserBench.getColIndex("ID");

        ObservableList<ObservableList> table = tableControllerUserBench.getTable().getItems();
        for(ObservableList row : table){
            String group = (String)row.get(colIndexSelection);
            String id = (String)row.get(colIndexSequence);
            String sequence = mtStorage.getData().get(id);
            if(!group.equals("Undefined")){
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



    }

    public void run(boolean runSlatkin, boolean runReynolds, String field_level_missing_data) throws IOException {

        Filter filter = new Filter();

        usableLoci = filter.getUsableLoci(
                data,
                missing_data_character,
                Double.parseDouble(field_level_missing_data)
        );

        Linearization linearization = new Linearization();
        DistanceTypeParser distanceTypeParser = new DistanceTypeParser();

//        StandardAMOVA standardAMOVA = new StandardAMOVA(usableLoci);
//        standardAMOVA.setDistanceParameter(distanceTypeParser.parse(distance_type), gamma_a);
//        standardAMOVA.setData(data);
//
//        fsts = standardAMOVA.calculateModifiedFst();
//        groupnames = standardAMOVA.getGroupnames();


        FstHudson1992 fstHudson1992 = new FstHudson1992(usableLoci);
        fstHudson1992.setDistanceParameter(distanceTypeParser.parse(distance_type), gamma_a);
        fstHudson1992.setData(data);

        fsts = fstHudson1992.calculateFst();
        groupnames = fstHudson1992.getGroupnames();


        // write to file
        writer = new Writer();
        writer.writeResultsFstToString(fsts,
                groupnames,
                usableLoci,
                Double.parseDouble(field_level_missing_data)
        );


        if(runSlatkin){
            fsts_slatkin = linearization.linearizeWithSlatkin(fsts);
            writer.addLinerarizedFstMatrix(fsts_slatkin, "Slatkin's linearized Fsts");
        }
        if(runReynolds){
            fsts_reynolds = linearization.linearizeWithReynolds(fsts);
            writer.addLinerarizedFstMatrix(fsts_reynolds, "Reynolds' distance");
        }

        // init table controller
        tableControllerFstValues = new TableControllerFstValues(mitobench.getLogClass());
        tableControllerFstValues.init();

       // writer.addDistanceMatrixToResult(fstHudson1992.getDistanceCalculator().getDistancematrix_d());

        writeLog(runSlatkin, runReynolds, field_level_missing_data);



    }

    private void writeLog(boolean runSlatkin, boolean runReynolds, String level_missing_data) {

        LOG.info("Calculate Fst values.\nRun Slatkin: " + runSlatkin + ".\nRun Reynolds: " + runReynolds +
        ".\nLevel of missing data: " + level_missing_data + ".\nMissing data character: " + missing_data_character +
        ".\nGamma a value: " + gamma_a);

    }


    public void writeToTable() {

        writeTabPane(
                fsts,
                "Fst values",
                "fst_values"
        );

//        if(fsts_slatkin != null)
//            writeTabPane(fsts_slatkin, "Fst values (Slatkin)", "fst_values_slatkin");
//        if(fsts_reynolds != null)
//            writeTabPane(fsts_reynolds, "Fst values (Reynolds)", "fst_values_reynolds");



    }

    private void writeTabPane(double[][] fsts, String tab_header, String id){

        ScrollPane scrollpane_result = new ScrollPane();
        String text = writer.getResult_as_string();
        Text t = new Text();
        t.setText(text);
        t.wrappingWidthProperty().bind(mitobench.getScene().widthProperty());
        scrollpane_result.setContent(t);

        Tab tab = new Tab();
        tab.setId("tab_" + id);
        tab.setText(tab_header);
        tab.setContent(scrollpane_result);

        mitobench.getTabpane_statistics().getTabs().add(tab);


    }


    public void visualizeResult() {

        HeatMap heatMap = new HeatMap("","", mitobench.getLogClass());
        heatMap.setContextMenu(mitobench.getTabpane_visualization());
        heatMap.createHeatMap(fsts, groupnames);

        Tab tab = new Tab("Fst values");
        tab.setId("tab_heatmap");
        tab.setContent(heatMap.getHeatMap());

        mitobench.getTabpane_visualization().getTabs().add(tab);

    }



    public void writeToFile(String path) throws IOException {
        writer.writeResultsToFile(path+ File.separator+"mitoBench_results_fst.txt");
    }


    /*
            GETTER
     */

    public String[] getGroupnames() {
        return groupnames;
    }


}

