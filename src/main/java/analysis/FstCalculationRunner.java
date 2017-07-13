package analysis;


import IO.reader.DistanceTypeParser;
import IO.writer.Writer;
import fst.Linearization;
import fst.StandardAMOVA;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import methods.Filter;
import org.apache.log4j.Logger;
import view.MitoBenchWindow;
import view.charts.HeatChart;
import view.charts.HeatMapLegend;
import view.table.MTStorage;
import view.table.controller.TableControllerFstValues;
import view.table.controller.TableControllerUserBench;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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

        writeLog(runSlatkin, runReynolds, field_level_missing_data);


        Filter filter = new Filter();

        usableLoci = filter.getUsableLoci(
                data,
                missing_data_character,
                Double.parseDouble(field_level_missing_data)
        );

        Linearization linearization = new Linearization();
        DistanceTypeParser distanceTypeParser = new DistanceTypeParser();

        StandardAMOVA standardAMOVA = new StandardAMOVA(usableLoci);
        standardAMOVA.setDistanceParameter(distanceTypeParser.parse(distance_type), gamma_a);
        standardAMOVA.setData(data);

        fsts = standardAMOVA.calculateModifiedFst();
        groupnames = standardAMOVA.getGroupnames();


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

        writer.addDistanceMatrixToResult(standardAMOVA.getDistanceCalculator().getDistancematrix_d());


    }

    private void writeLog(boolean runSlatkin, boolean runReynolds, String level_missing_data) {

        LOG.info("Calculate Fst values.\nRun Slatkin: " + runSlatkin + ".\nRun Reynolds: " + runReynolds +
        ".\nLevel of missing data: " + level_missing_data + ".\nMissing data character: " + missing_data_character +
        ".\nGamma a value: " + gamma_a);

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

//        TableView<ObservableList> table = tableControllerFstValues.getTable();
//        tableControllerFstValues.addColumn("", 0);
//        int i = 1;
//        for(String group : groupnames){
//            tableControllerFstValues.addColumn(group, i);
//            i++;
//        }
//
//        // add data (table content)
//        // write population HG count information
//        ObservableList<ObservableList> entries = FXCollections.observableArrayList();
//        for(int j = 0; j < fsts.length ; j++){
//            ObservableList  entry = FXCollections.observableArrayList();
//            entry.add(groupnames[j]);
//            for(int col=0; col<fsts[0].length; col++)
//            {
//                entry.add(round(fsts[col][j],5));
//            }
//            entries.add(entry);
//        }
//
//
//        // clear Items in table
//        table.getItems().removeAll(table.getItems());
//        //FINALLY ADDED TO TableView
//        table.getItems().addAll(entries);

//        Tab tab = new Tab();
//        tab.setId("tab_" + id);
//        tab.setText(tab_header);
//        tab.setContent(table);

        ScrollPane scrollpane_result = new ScrollPane();
        String text = writer.getResult_as_string();
        Text t = new Text();
        t.setText(text);
        t.wrappingWidthProperty().bind(mitobench.getScene().widthProperty());
        scrollpane_result.setContent(t);
        //Label textArea_result = new Label(writer.getResult_as_string());

        Tab tab = new Tab();
        tab.setId("tab_" + id);
        tab.setText(tab_header);
        tab.setContent(scrollpane_result);

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

    public void visualizeResult() {

        HeatChart heat = new HeatChart(trasposeMatrix(fsts), 0.0, 1.0);
        heat.setTitle("Fst values");
        heat.setXValues(groupnames);
        heat.setYValues(groupnames);

        BorderPane b = new BorderPane();
        Image i = SwingFXUtils.toFXImage((BufferedImage) heat.getChartImage(false), null);
        ImageView v = new ImageView(i);
        b.setCenter(v);

        HeatMapLegend legend = new HeatMapLegend(300);
        b.setRight(legend.getRoot());

        Tab tab = new Tab("Fst values");
        tab.setId("tab_heatmap");
        tab.setContent(b);

        mitobench.getTabpane_visualization().getTabs().add(tab);



    }


    public static double[][] trasposeMatrix(double[][] matrix)
    {
        int m = matrix.length;
        int n = matrix[0].length;

        double[][] trasposedMatrix = new double[n][m];

        for(int x = 0; x < n; x++)
        {
            for(int y = 0; y < m; y++)
            {
                trasposedMatrix[x][y] = matrix[y][x];
            }
        }

        return trasposedMatrix;
    }
}

