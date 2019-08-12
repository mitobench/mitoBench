package analysis.fstcalculation;

import java.io.IOException;
import java.util.*;


public class DistanceMatrix {

    private final DistanceMethods distance;
    private final double gamma_a;
    private int distance_type;
    private double[][] distancematrix_d;

    public DistanceMatrix(int numberOfUsableLoci, double gamma, int type, List<Integer> usableLoci){
        gamma_a  = gamma;
        distance_type = type;
        distance = new DistanceMethods(numberOfUsableLoci, usableLoci);
    }


    /**
     * This method calculates the distance between two DNA sequences within populations.
     * The distance method has to be set by the user.
     *
     * @param sequences
     * @throws IOException
     */
    public void calculateWP(List<String> sequences) throws IOException {

        distancematrix_d = new double[sequences.size()][sequences.size()];

        List<Double> list_distances = new ArrayList<>();

        for (int i = 0; i < sequences.size(); i++) {
            String sequence1 = sequences.get(i);

            for (int j = 0; j < sequences.size(); j++) {
                String sequence2 = sequences.get(j);
                if (i == j) {
                    distancematrix_d[i][j] = 0.0;
                } else {
                    //System.out.println("Calculating distance for pair " + i + "|" + j);
                    double d = getDistance(sequence1, sequence2, distance_type, sequences);
                    distancematrix_d[j][i] = d;
                    list_distances.add(d);
                }
            }
        }
    }


    /**
     * This method calculates the distance between two DNA sequences among populations.
     * The distance method has to be set by the user.
     *
     * @param pop1
     * @param pop2
     * @throws IOException
     */
    public void calculateAP(List<String> pop1, List<String> pop2) throws IOException {

        List<String> all_individuals = new ArrayList<String>(pop1);
        all_individuals.addAll(pop2);

        distancematrix_d = new double[pop1.size()][pop2.size()];

        List<Double> list_distances = new ArrayList<>();
        for (int i = 0; i < pop1.size(); i++){
            String sequence1 = pop1.get(i);

            for (int j = 0; j < pop2.size(); j++){
                String sequence2 = pop2.get(j);
                double d = getDistance(sequence1, sequence2, distance_type, all_individuals);
                distancematrix_d[i][j] = d;
                list_distances.add(d);
            }

        }
    }


    /**
     *
     * This method calculates the distance between two DNA sequences.
     *
     * @param sequence1
     * @param sequence2
     * @param distance_type
     * @param all_individuals
     * @return
     * @throws IOException
     */
    private double getDistance(String sequence1, String sequence2, int distance_type, List<String> all_individuals) throws IOException {
        double d=0.0;

        switch (distance_type) {
            case 0: // Pairwise difference
                //System.out.println("Start calculation");
                d = distance.getPairwiseDifference(sequence1, sequence2);
                break;

            case 1: // Percentage difference
                d = distance.getPercentageDifference(sequence1, sequence2);
                break;

            case 2: // Jukes and Cantor
                d = distance.getJukesAndCantorDifference(sequence1, sequence2, gamma_a);
                break;

            case 3: // Kimura 2-parameters
                d = distance.getKimura2PDifference(sequence1, sequence2, gamma_a);
                break;

            case 4: // Tamura --> not yet supported
                d = distance.getTamuraDifference(sequence1, sequence2, all_individuals);
                break;
        }

        return d;
    }




    /**
     * Returns the distance matric
     * @return
     */
    public double[][] getDistancematrix_d() {
        return distancematrix_d;
    }
}
