package analysis.fstcalculation;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class FstCalculator {

    private static double gamma = 0.5;
    private static double level_missing_data = 0.05;
    private static double significance = 0.05;
    private static int number_of_permutations = 0;
    private static String missing_data = "N";
    private final HashMap<String, List<String>> data;
    private final Logger log;
    private String distance_method;
    private Writer writer;
    private String[] groupnames;


    public FstCalculator(HashMap<String, List<String>> data,
                         String missing_data,
                         double level_missing_data,
                         String distance_method,
                         int number_of_permutations,
                         double gamma,
                         double significance,
                         Logger LOG) {

        this.data = data;
        this.missing_data = missing_data;
        this.distance_method = distance_method;
        this.level_missing_data = level_missing_data;
        this.number_of_permutations = number_of_permutations;
        this.gamma = gamma;
        this.significance = significance;
        this.log = LOG;
    }

    public double[][] runCaclulations() throws IOException {
        Linearization linearization = new Linearization();

        DistanceTypeParser distanceTypeParser = new DistanceTypeParser();
        Filter filter = new Filter();
        List<Integer> usableLoci = filter.getUsableLoci(data, missing_data, level_missing_data);

        FstAMOVA standardAMOVA = new FstAMOVA(
                usableLoci,
                number_of_permutations,
                distanceTypeParser.parse(distance_method),
                gamma,
                log);

        standardAMOVA.setData(data);

        double[][] fsts_amova = standardAMOVA.calculateFst();
        double[][] pvalues = null;
        if(number_of_permutations > 0){
            pvalues = standardAMOVA.calculatePermutatedFst();
        }


        writer = new Writer(number_of_permutations);

        writer.writeResultsFstToString(
                fsts_amova,
                pvalues,
                standardAMOVA.getGroupnames(),
                usableLoci,
                level_missing_data,
                significance
        );
        writer.addDistanceMatrixToResult(
                standardAMOVA.getDistanceCalculator().getDistancematrix_d()
        );

        writer.addLinerarizedFstMatrix(
                linearization.linearizeWithSlatkin(fsts_amova),
                "# Linearized Fst values (Slatkin)."
        );
        writer.addLinerarizedFstMatrix(
                linearization.linearizeWithReynolds(fsts_amova),
                "# Linearized Fst values (Reynold)."
        );

        writer.writeResultsToFile("resultsFstStatisticsAMOVA.tsv");

        groupnames = standardAMOVA.getGroupnames();

        return fsts_amova;
    }


    public String getResultString(){
        return writer.getResult_as_string();
    }

    public String[] getGroupnames() {
        return groupnames;
    }


}
