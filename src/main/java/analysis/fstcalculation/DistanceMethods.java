package analysis.fstcalculation;


import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class DistanceMethods {

    private final double numberOfUsableLoci;
    private final List<Integer> usableLoci;

    public DistanceMethods(int numberOfUsableLoci, List<Integer> usableLoci){
        this.numberOfUsableLoci = numberOfUsableLoci;
        this.usableLoci = usableLoci;
    }

    /**
     * This method calculates the pairwise difference between two DNA sequences.
     * @param sequence1
     * @param sequence2
     * @return
     */
    public double getPairwiseDifference(String sequence1, String sequence2){


        if(sequence1.length() != sequence2.length()){ // should never happen
            System.out.println("sequences have different length: " + sequence1.length() + "|" + sequence2.length());
            return -1;
        }

        int i = 0, n_d = 0;
        while (i < sequence1.length())
        {
            if (sequence1.charAt(i) != sequence2.charAt(i))
                n_d++;
            i++;
        }

        return n_d;
    }


    /**
     * This method calculates the percentage difference between two DNA sequences.
     * @param sequence1
     * @param sequence2
     * @return
     */
    public double getPercentageDifference(String sequence1, String sequence2){
        double n_d = getN_d(sequence1, sequence2);
        double d = n_d / numberOfUsableLoci;

        return d;

    }


    /**
     * This method calculates the Jukes & Cantor difference between two DNA sequences.
     * @param sequence1
     * @param sequence2
     * @param gamma_a
     * @return
     */
    public double getJukesAndCantorDifference(String sequence1, String sequence2, double gamma_a) {
        double n_d = getN_d(sequence1, sequence2);
        double L = numberOfUsableLoci;
        double p = n_d/ L;
        double x  = 1.0 - (4/(double)3)*p;

        double d_gamma_corrected = (-0.75) * gamma_a * ( Math.pow(x, (-1)/gamma_a) - 1);
        double v_d_gamma_corrected = Math.abs(p*(1-p) * (Math.pow(x,(-2)*(1/(gamma_a+1)))) / L);

        double d_gamma_corrected_rounded = (double)Math.round(d_gamma_corrected * 100000d) / 100000d;

        return d_gamma_corrected_rounded;
    }


    /**
     * This method calculates the Kimura 2 parameter difference between two DNA sequences.
     * @param sequence1
     * @param sequence2
     * @param gamma_a
     * @return
     * @throws IOException
     */
    public double getKimura2PDifference(String sequence1, String sequence2, double gamma_a) throws IOException {

        double n_s = getN_s(sequence1, sequence2);
        double n_v = getN_v(sequence1, sequence2);


        double P;
        if(n_s != 0)
            P = n_s / numberOfUsableLoci;
        else
            P = 0.0;


        double Q;
        if(n_v != 0)
            Q = n_v / numberOfUsableLoci;
        else
            Q = 0.0;

        double x = (1-2*P-Q);
        double power = -(1/(gamma_a+1));

        double c_1 = 1 / x;
        double c_1_gamma_corrected = Math.pow(x,power);

        double c_2 = 1/(1-2*Q);
        double c_2_gamma_corrected = Math.pow((1-2*Q),power);

        double c_3 =(c_1 + c_2) / (double)2;
        double c_3_gamma_corrected =(c_1_gamma_corrected + c_2_gamma_corrected) / (double)2;

        double d_gamma_corrected = ((gamma_a*0.5) * (Math.pow(x,-1/gamma_a) + 0.5*Math.pow((1-2*Q),-1/gamma_a) - (3/(double)2)))*numberOfUsableLoci;

        return Math.abs(round(d_gamma_corrected,5));
    }


    /**
     * This method calculates the Tamura difference between two DNA sequences.
     * @param sequence1
     * @param sequence2
     * @param sequences
     * @return
     */
    public double getTamuraDifference(String sequence1, String sequence2, List<String> sequences) {

        double n_s = getN_s(sequence1, sequence2);
        double n_v = getN_v(sequence1, sequence2);
        double omega = getOmega(sequences);

        double P = n_s / numberOfUsableLoci;
        double Q = n_v / numberOfUsableLoci;

        double x = 2*omega*(1-omega);

        double c_1 = 1 / (1 - (P / x));
        double c_2 = 1 / ( 1 - 2*Q);
        double c_3 = x*(c_1-c_2)+c_2;

        double d = (-x*Math.log(1-(P/x) -Q) - 0.5*(1-x)*Math.log(1-2*Q))*numberOfUsableLoci;

        return round(Math.abs(d),5);
    }


    /**
     * This method calculates the number of transversions (C<->A, T<->G, C<->G and A<->T) between two DNA sequences.
     * @param sequence1
     * @param sequence2
     * @return
     */
    private double getN_v(String sequence1, String sequence2) {
        // n_v = number of transversions (C<->A, T<->G, C<->G and A<->T)
        double n_v = 0.0;

        for(int i = 0; i < sequence1.length(); i++) {
            if(usableLoci.get(i)!=0){
                if        (sequence1.charAt(i) == 'C' && sequence2.charAt(i) == 'A'
                        || sequence1.charAt(i) == 'A' && sequence2.charAt(i) == 'C'

                        || sequence1.charAt(i) == 'T' && sequence2.charAt(i) == 'G'
                        || sequence1.charAt(i) == 'G' && sequence2.charAt(i) == 'T'

                        || sequence1.charAt(i) == 'G' && sequence2.charAt(i) == 'C'
                        || sequence1.charAt(i) == 'C' && sequence2.charAt(i) == 'G'

                        || sequence1.charAt(i) == 'A' && sequence2.charAt(i) == 'T'
                        || sequence1.charAt(i) == 'T' && sequence2.charAt(i) == 'A') {
                    n_v++;
                }
            }
        }
        return n_v;
    }


    /**
     * This method calculates the number of transitions (C<->T, A<->G) between two DNA sequences.
     * @param sequence1
     * @param sequence2
     * @return
     */

    private double getN_s(String sequence1, String sequence2) {
        // n_s = number of transitions (C<->T, A<->G)
        double n_s = 0.0;

        for(int i = 0; i < sequence1.length(); i++){
            if(usableLoci.get(i)!=0){
                if(        sequence1.charAt(i) == 'C' && sequence2.charAt(i) == 'T'
                        || sequence1.charAt(i) == 'T' && sequence2.charAt(i) == 'C'
                        || sequence1.charAt(i) == 'A' && sequence2.charAt(i) == 'G'
                        || sequence1.charAt(i) == 'G' && sequence2.charAt(i) == 'A'){
                    n_s++;
                }
            }
        }

        return n_s;
    }


    /**
     * This method calculates the number of substitution between two DNA sequences.
     *
     * @param sequence1
     * @param sequence2
     * @return
     */
    private double getN_d(String sequence1, String sequence2) {
        double d = 0.0;
        for(int i = 0; i < sequence1.length(); i++){
            if(usableLoci.get(i)!=0){
                if(sequence1.charAt(i) != sequence2.charAt(i) && sequence1.charAt(i) != 'N' && sequence2.charAt(i) != 'N')
                    d++;
            }
        }

        return d;
    }


    /**
     * This method calculates omega (= G+C ratio, computed on
     * all the DNA sequences of a given sample)
     *
     * @param sequences
     * @return
     */
    private double getOmega(List<String> sequences){

        int count_C = 0;
        int count_T = 0;
        int count_G = 0;
        int count_A = 0;

        for(int i = 0; i < sequences.size(); i++){
            if(usableLoci.get(i)!=0){
                String sequence = sequences.get(i);
                count_C = StringUtils.countMatches(sequence, "C");
                count_T = StringUtils.countMatches(sequence, "T");
                count_G = StringUtils.countMatches(sequence, "G");
                count_A = StringUtils.countMatches(sequence, "A");
            }
        }

        return (count_C + count_G) / (double)(count_A+count_C+count_G+count_T);

    }


    /**
     * This method rounds a double value on n digits.
     * @param value
     * @param numberOfDigitsAfterDecimalPoint
     * @return double rounded
     */
    private static double round(double value, int numberOfDigitsAfterDecimalPoint) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint,
                BigDecimal.ROUND_HALF_EVEN);
        return bigDecimal.doubleValue();
    }


    /**
     * creates a zipped list containing lists of elements from
     * the same positions of the lists in the arguments
     * @param l1 first list
     * @param l2 second list
     * @return a List<List<T>> containing elements from both lists
     */
    private static <T> List<List<T>> zip(List<T> l1, List<T> l2){

        return IntStream.range(0, l1.size())
                .mapToObj(i -> Arrays.asList(l1.get(i), l2.get(i)))
                .collect(Collectors.toList());

    }

}
