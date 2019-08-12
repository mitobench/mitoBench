package analysis.fstcalculation;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;



public class FstAMOVA {

    private final Logger log;
    private int numberOfPermutations;
    private final UsefulFunctions functions;
    private DistanceMatrix distanceCalculator;
    private HashMap<String, List<String>> data;
    private int numberOfPopulations;
    private double[][] fsts;

    public FstAMOVA(List<Integer> usableLoci, int numberOfPermutations, int method, double gamma, Logger log){
        this.functions = new UsefulFunctions();
        this.numberOfPermutations = numberOfPermutations;
        this.distanceCalculator = new DistanceMatrix(functions.count1(usableLoci), gamma, method, usableLoci);
        this.log = log;
    }

    /**
     *
     * This method creates all possible pairs of populations to calculate
     * pairwise Fst.
     *
     * @return Fsts between all pairs of populations
     * @throws IOException
     */

    public double[][] calculateFst() throws IOException {

        long startTime_total = System.currentTimeMillis();
        System.out.println("Calculating Fst");


        List<String> populations = new ArrayList<>();
        populations.addAll(data.keySet());
        numberOfPopulations = 2;

        fsts = new double[populations.size()][populations.size()];

        // iterate over list, calculateWP distance between each pair of individuals
        BigInteger totalNumberOfComparisons = choose(populations.size(), 2);
        int pairsDone = 0;
        for(int i = 0; i < populations.size(); i++){
            for(int j = i+1; j < populations.size(); j++){
                int num_comp = ((populations.get(j).length()*populations.get(i).length())/2);
                log.info("Comparing population " + populations.get(j) + " and " + populations.get(i) +  "\n" +
                        "# of comparisons: " + num_comp);
                List<String> pop1 = data.get(populations.get(j));
                List<String> pop2 = data.get(populations.get(i));
                double fst = calculatePairwiseDistance(
                        pop1.toArray(new String[pop1.size()]),
                        pop2.toArray(new String[pop2.size()])
                );
                fsts[i][j] = fst;
                pairsDone++;
                log.info("("+ pairsDone+"/"+totalNumberOfComparisons+") pairs compared");
            }
        }

        long currtime_post_execution = System.currentTimeMillis();
        long diff = currtime_post_execution - startTime_total;

        long runtime_s = diff / 1000;
        if(runtime_s > 60) {
            long minutes = runtime_s / 60;
            long seconds = runtime_s % 60;
            log.info("Runtime of Fst calculation: " + minutes + " minutes, and " + seconds + " seconds.");
        } else {
            log.info("Runtime of Fst calculation: " + runtime_s + " seconds.");
        }
        return fsts;


    }


    /**
     * This method calculates the Fst value between all pairs of populations
     * n times to calculates the statistical significance of the first calculates Fst value.
     *
     * @return
     * @throws IOException
     */

    public double[][] calculatePermutatedFst() throws IOException {

        System.out.println("Calculating pvalues");
        List<String> populations = new ArrayList<>();
        populations.addAll(data.keySet());
        double[][] pvalues = new double[populations.size()][populations.size()];
        Permutation permutation = new Permutation();

        // iterate over list, calculateWP distance between each pair of individuals
        for(int i = 0; i < populations.size(); i++){
            for(int j = i+1; j < populations.size(); j++){
                List<String> pop1 = data.get(populations.get(i));
                List<String> pop2 = data.get(populations.get(j));

                int permutations_counter = 0;
                double[] fsts_permuted = new double[numberOfPermutations];
                while (permutations_counter < numberOfPermutations){
                    List[] permutedSamples = permutation.permuteSamples(pop1, pop2);
                    List<String> sample1 = permutedSamples[0];
                    List<String> sample2 = permutedSamples[1];


                    fsts_permuted[permutations_counter] = calculatePairwiseDistance(
                            sample1.toArray(new String[sample1.size()]),
                            sample2.toArray(new String[sample2.size()])
                    );
                    permutations_counter++;
                }

                // calculateWP pValue
                pvalues[i][j] = calculatePvalue(fsts_permuted, fsts[i][j]);

                // clear all
                permutation.clear();
            }
        }

        return pvalues;
    }


    /**
     * Calculates Fst value according standard ANOVA
     *
     * (Example in:
     *      Bird, CHRISTOPHER E., et al. "Detecting and measuring genetic differentiation."
     *      Phylogeography and population genetics in Crustacea 19.3 (2011): l-55.
     *  )
     *
     *
     * @param population1
     * @param population2
     * @return
     * @throws IOException
     */
    private double calculatePairwiseDistance(String[] population1, String[] population2) throws IOException {
        List<String> pop1 = Arrays.asList(population1);
        List<String> pop2 = Arrays.asList(population2);

        List<String> pop_all = new ArrayList<>(pop1);
        pop_all.addAll(pop2);

        // calculateWP distance matrix with given distance measure

        distanceCalculator.calculateWP(pop1);
        double[][] distMatrixPop1 = distanceCalculator.getDistancematrix_d();

        distanceCalculator.calculateWP(pop2);
        double[][] distMatrixPop2 = distanceCalculator.getDistancematrix_d();

        distanceCalculator.calculateAP(pop1, pop2);
        double[][] distMatrixPopall = distanceCalculator.getDistancematrix_d();

        int n1 = pop1.size(); // Number of individuals in population 1
        int n2 = pop2.size(); // Number of individuals in population 2
        int P = numberOfPopulations; // Number of populations
        double N = n1+n2; // Number of individuals
        double df_AP = P-1; // degrees of freedom among populations
        double df_WP = (n1 - 1) + (n2 - 1); // degrees of freedom within populations

        double d_w1 = calculateSum(distMatrixPop1); // sum of differences within population 1
        double d_w2 = calculateSum(distMatrixPop2); // sum of differences within population 2
        double d_AP = calculateSum(distMatrixPopall); // sum of differences among populations

        double ss_AP = calculateSS_AP(d_AP, d_w1, d_w2, N, (double)n1, (double)n2); // among populations, within groups o_b
        double ss_WP = calculateSS_WP(d_w1, d_w2, (double)n1, (double)n2); // within populations o_c

        double MS_WP = ss_WP / df_WP;
        double MS_AP = ss_AP / df_AP;

        double nc = calculateNc(N, P, n1, n2);
        double omega_square_WP = MS_WP;
        double omega_square_AP =(MS_AP - MS_WP) / nc;

        double Fst = omega_square_AP / (omega_square_WP + omega_square_AP);

        return Fst;
    }


    /**
     * This method sums up all values in a matrix.
     *
     * @param distMatrix
     * @return
     */
    private double calculateSum(double[][] distMatrix) {
        double sum = 0.0;
        for (int i = 0; i < distMatrix.length; i++) {
            for (int j = 0; j < distMatrix[0].length; j++) {
                sum += distMatrix[i][j];
            }
        }
        return sum;
    }

    /**
     * This methods calculates the sum of squared differences among populations.
     *
     * @param d_AP
     * @param d_w1
     * @param d_w2
     * @param N
     * @param n1
     * @param n2
     * @return
     */

    private double calculateSS_AP(double d_AP, double d_w1, double d_w2, double N, double n1, double n2){

        double tmp1 = (d_AP + d_w1) / (2*N);
        double tmp2 = d_w1 / (2*n1);
        double tmp3 = (d_AP+d_w2) / (2*N);
        double tmp4 = d_w2 / (2*n2);

        return (tmp1-tmp2) + (tmp3-tmp4);
    }

    /**
     * This methods calculates the sum of squared differences within populations.
     *
     * @param d_w1
     * @param d_w2
     * @param n1
     * @param n2
     * @return
     */

    private double calculateSS_WP(double d_w1, double d_w2, double n1, double n2){

        double tmp1 = d_w1 / (2*n1);
        double tmp2 = d_w2 / (2*n2);

        return tmp1 + tmp2;
    }


    /**
     * This method calculates Nc
     * @param N
     * @param P
     * @param n1
     * @param n2
     * @return
     */
    private double calculateNc(double N, int P, int n1, int n2) {
        double tmp = (Math.pow(n1,2) + Math.pow(n2, 2)) / N;
        double tmp0 = Math.floor(tmp);
        double tmp1 = N - (tmp0);
        double tmp2 = P-1;

        return tmp1 / tmp2;
    }


    /**
     * The null distribution of pairwise FST values under the hypothesis of no difference between
     * the populations is obtained by permuting haplotypes between populations. The P-value of
     * the test is the proportion of permutations leading to a FST value larger or equal to the
     * observed one.
     *
     * @param fsts_permuted
     * @param fst_original
     * @return
     */
    private double calculatePvalue(double[] fsts_permuted, double fst_original) {

        int numberOfFSTsBiggerOrEqual = 0;
        for(double fst : fsts_permuted){
            if(fst >= fst_original){
                numberOfFSTsBiggerOrEqual++;
            }
        }
        return numberOfFSTsBiggerOrEqual / (double)fsts_permuted.length;
    }


    public static BigInteger choose(int x, int y) {
        if (y < 0 || y > x)
            return BigInteger.ZERO;
        if (y == 0 || y == x)
            return BigInteger.ONE;

        BigInteger answer = BigInteger.ONE;
        for (int i = x - y + 1; i <= x; i++) {
            answer = answer.multiply(BigInteger.valueOf(i));
        }
        for (int j = 1; j <= y; j++) {
            answer = answer.divide(BigInteger.valueOf(j));
        }
        return answer;
    }

    /*
            Setter and Getter
     */
    public void setData(HashMap<String, List<String>> data) {
        this.data = data;
    }
    public String[] getGroupnames() {
        return data.keySet().toArray(new String[0]);
    }
    public DistanceMatrix getDistanceCalculator() {
        return distanceCalculator;
    }

}
