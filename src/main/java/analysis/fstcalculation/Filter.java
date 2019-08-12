package analysis.fstcalculation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Filter {


    private double allowedMissingData;
    private double numberOfLoci;
    private double[] loci_count_missing_data;
    private double[] loci_count_gaps;
    private char gap = '-';
    private int numberOfSequences;

    public List<Integer> getUsableLoci(HashMap<String, List<String>> testData, String missingData, double allowedMissingData) {

        numberOfLoci = 0.0;
        numberOfSequences = 0;
        this.allowedMissingData = allowedMissingData;

        // it can be assumed that all sequences have the same length
        for (String key : testData.keySet()) {
            numberOfLoci = testData.get(key).get(0).length();
            this.loci_count_missing_data = new double[testData.get(key).get(0).length()];
            this.loci_count_gaps = new double[testData.get(key).get(0).length()];
            Arrays.fill(this.loci_count_missing_data, 0.0);
            break;
        }

        for (String key : testData.keySet()) {
            List<String> pop = testData.get(key);
            for (int ind = 0; ind < pop.size(); ind++) {
                String seq = pop.get(ind);
                numberOfSequences++;
                for (int loci = 0; loci < seq.length(); loci++) {
                    char base = seq.charAt(loci);

                    if(String.valueOf(base).equals(missingData.toLowerCase()) ||
                            String.valueOf(base).equals(missingData.toUpperCase())){

                        loci_count_missing_data[loci]++;
                    } else if(base == gap){
                        loci_count_gaps[loci]++;
                    }
                }
            }
        }

        return calculateUsableLoci();
    }



    /**
     * 0 means: not usable
     * 1 means: usable
     * @return
     */

    private List<Integer> calculateUsableLoci() {
        List<Integer> usableLoci = new ArrayList();
        for(int i = 0; i < loci_count_missing_data.length; i++){
            double perc_missing = loci_count_missing_data[i] / (double)numberOfSequences;
            double perc_gaps = loci_count_gaps[i] / (double)numberOfSequences;
            double sum_missing_gaps = loci_count_gaps[i]+loci_count_missing_data[i];
            if(perc_missing >= allowedMissingData || perc_gaps == 1.0 || perc_missing==1.0 || sum_missing_gaps==numberOfSequences){
                usableLoci.add(0);
            } else {
                usableLoci.add(1);
            }
        }

        return usableLoci;
    }


    public double getNumberOfTotalLoci() {
        return this.numberOfLoci;
    }
}
