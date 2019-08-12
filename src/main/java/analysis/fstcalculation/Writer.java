package analysis.fstcalculation;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class Writer {

    private final UsefulFunctions functions;
    private final int numberOfPermutations;
    private String result_as_string;

    /**
     * Constructor
     * @param number_of_permutations
     */
    public Writer(int number_of_permutations){
        this.functions = new UsefulFunctions();
        this.numberOfPermutations = number_of_permutations;

    }


    /**
     * This method writes the results to a text file.
     * @param file
     * @throws IOException
     */

    public void writeResultsToFile(String file) throws IOException {

        FileWriter fileWriter = new FileWriter(new File(file));
        BufferedWriter bfWriter = new BufferedWriter(fileWriter);

        bfWriter.write(result_as_string);

        bfWriter.flush();
        bfWriter.close();

    }


    /**
     * This method writes the results to one string.
     *
     * @param fsts
     * @param groupnames
     * @param usableLoci
     * @param level_missing_data
     */

    public void writeResultsFstToString(
            double[][] fsts,
            double[][] pvalues,
            String[] groupnames,
            List<Integer> usableLoci,
            double level_missing_data,
            double significance) {


        result_as_string = "";

        result_as_string += "Result Fst Calculation\n\nGroups:\n";
        for(String group : groupnames){
            result_as_string += "\t- " + group + "\n";
        }

        result_as_string += "\nPairwise Fst values:\n\n";

        for(int row=0; row<fsts.length; row++){
            for(int col=0; col<fsts[0].length; col++)
            {
                if(col > row){
                    // fill upper diagonal matrix with dashes
                    result_as_string += "-\t";
                } else {
                    double val = fsts[col][row];
                    if(Double.isInfinite(val)) {
                        result_as_string +="inf" + "\t";
                    } else if (Double.isNaN(val)) {
                        result_as_string +="NaN"  + "\t";
                    } else {
                        result_as_string += functions.round(val, 5) + "\t";
                    }
                }
            }
            result_as_string += "\n";
        }


        if(numberOfPermutations > 0){
            result_as_string += "\nP-values:\n\n";


            for(int row=0; row<pvalues.length; row++){
                for(int col=0; col<pvalues[0].length; col++)
                {
                    if(col >= row){
                        // fill upper diagonal matrix with dashes
                        result_as_string += "-\t";
                    } else {
                        double val = pvalues[col][row];
                        if(Double.isInfinite(val)) {
                            result_as_string +="inf" + "\t";
                        } else if (Double.isNaN(val)) {
                            result_as_string +="NaN"  + "\t";
                        } else {
                            result_as_string += functions.round(val, 5) + "\t";
                        }
                    }
                }
                result_as_string += "\n";
            }


            result_as_string += "\nP-values (easy):\n\n";

            for(int row=0; row<pvalues.length; row++){
                for(int col=0; col<pvalues[0].length; col++) {
                    if(col >= row){
                        // fill upper diagonal matrix with dashes
                        result_as_string += "-\t";
                    } else {
                        double val = pvalues[col][row];
                        if(val >= significance){
                            result_as_string += "-\t";
                        } else if(val < significance){
                            result_as_string += "+\t";
                        }
                    }
                }
                result_as_string += "\n";
            }
        }



        int linecounter = 1;
        result_as_string += "\nList of usable loci (" + functions.count1(usableLoci) + "):\n--------------------\n";
        for(int i = 0; i < usableLoci.size(); i++){
            if(usableLoci.get(i)==1){
                addValToString(linecounter, i);
            }
        }

        linecounter = 1;
        result_as_string += "\n\nNot usable loci ("+ (usableLoci.size()-functions.count1(usableLoci)) +"):"+
                "\n(>" + (int)(level_missing_data*100) + "% missing data) \n----------------------------\n";
        for(int i = 0; i < usableLoci.size(); i++){
            if(usableLoci.get(i)==0){
                addValToString(linecounter, i);
            }
        }
        result_as_string += "\n";
    }

    /**
     * Method that helps to write only 15 values per line.
     *
     * @param linecounter
     * @param i
     */
    private void addValToString(int linecounter, int i){
        if(linecounter % 15 == 0){
            result_as_string += i+1 + "\n";
            linecounter++;
        } else {
            result_as_string += i+1 + "\t";
            linecounter++;
        }
    }


    /**
     * Add distance matrix to result string.
     *
     * @param distancematrix_d
     */
    public void addDistanceMatrixToResult(double[][] distancematrix_d){
        printmatrix(distancematrix_d, "DistanceMethods matrix" +
                "-----------------------------------------\n");
    }

    private void printmatrix(double[][] matrix, String title) {

        result_as_string += title+"\n";
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                if(i==j){
                    result_as_string += 0.0 +"\t";
                } else if(j>i){
                    result_as_string += "-\t";
                } else {
                    result_as_string += matrix[i][j] +"\t";
                }
            }
            result_as_string += "\n";
        }
        result_as_string += "\n";
    }


    /**
     * Add linearized matrix to result string.
     *
     * @param fsts_lin
     * @param title
     */
    public void addLinerarizedFstMatrix(double[][] fsts_lin, String title) {

        printmatrix(fsts_lin, title);
    }


    public String getResult_as_string() {
        return result_as_string;
    }
}
