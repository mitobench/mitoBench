package analysis;

import Logging.LogClass;
import controller.ChartController;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.correlation.Covariance;
import view.visualizations.ScatterPlot;

import java.util.*;


public class PCA {

    private final ChartController chartController;
    private ScatterPlot pca_plot;
    private int numberOfDimensions = 2;
    private String[] groups;

    public PCA(ChartController cc){
        chartController = cc;

    }

    public double[][] calculate(double[][] values, int numPC){

        numberOfDimensions = numPC;

        double[][] values_meanCorrected = subtractMean(values);

        //create real matrix
        RealMatrix realMatrix = MatrixUtils.createRealMatrix(values_meanCorrected);

        //create covariance matrix of points, then find eigenvectors
        Covariance covariance = new Covariance(realMatrix);
        RealMatrix covarianceMatrix = covariance.getCovarianceMatrix();
        EigenDecomposition ed = new EigenDecomposition(covarianceMatrix);

        double[] eigenvalues = ed.getRealEigenvalues();

        // generate vector only with the chosen dimensions
        double[] featureVector = new double[this.numberOfDimensions];
        for(int i =0; i < numberOfDimensions; i++){
            featureVector[i] = eigenvalues[i];
        }

        RealMatrix eigenvectorsTransposed = MatrixUtils.createRealMatrix(getEigenvectors(ed, this.numberOfDimensions));
        RealMatrix valuesTransposed = MatrixUtils.createRealMatrix(transposeMatrix(values));

        RealMatrix resultPCA_tmp = eigenvectorsTransposed.multiply(valuesTransposed);

        return resultPCA_tmp.getData();




    }

    /**
     * Subtract mean (of row) from all values in this row.
     *
     * @param values
     * @return
     */
    private double[][] subtractMean(double[][] values) {

        for(int i = 0; i < values.length; i++){
            double mean = Arrays.stream(values[i]).average().getAsDouble();
            for(int j = 0; j < values[i].length; j++){
                values[i][j] = values[i][j] + mean;
            }
        }

        return values;
    }


    /**
     * Plot adjusted values.
     *
     *  @param result_pca
     * @param group_color
     * @param stage
     * @param logClass
     * @param tabpane_statistics
     * @param group_members
     */
    public void plot(double[][] result_pca, HashMap<String, Color> group_color, Stage stage, LogClass logClass,
                     TabPane tabpane_statistics, HashMap<String, ObservableList<String>> group_members) {

        pca_plot = new ScatterPlot(stage, logClass, tabpane_statistics);
        //groups = group_color.keySet().toArray(new String[group_color.keySet().size()]);
        groups = group_members.keySet().toArray(new String[group_members.keySet().size()]);
        double[] pc1 = result_pca[0];
        double[] pc2 = result_pca[1];
        String[] groupOrder = chartController.getGroupOrder();


        OptionalDouble lowerbound_x = Arrays.stream(pc1).min();
        OptionalDouble lowerbound_y = Arrays.stream(pc2).min();
        OptionalDouble upperbound_x = Arrays.stream(pc1).max();
        OptionalDouble upperbound_y = Arrays.stream(pc2).max();

        pca_plot.create(
                lowerbound_x.getAsDouble()-0.5,
                lowerbound_y.getAsDouble()-0.5,
                upperbound_x.getAsDouble()+0.5,
                upperbound_y.getAsDouble()+0.5
        );


        // filter out groups and assign specific colors

        if(groups.length!=0){
            for(int i = 0; i < groups.length; i++){
                List<Integer> indexes = new ArrayList<>();
                ObservableList<String> members = group_members.get(groups[i]);
                for(String member : members){
                    for(int j = 0; j < groupOrder.length; j++){
                        if (groupOrder[j].equals(member)) {
                            indexes.add(j);
                        }
                    }
                }
                double[] pc1_group = getSubArray(pc1, Collections.min(indexes), Collections.max(indexes));
                double[] pc2_group = getSubArray(pc2, Collections.min(indexes), Collections.max(indexes));

                String[] members_array = members.toArray(new String[members.size()]);
                pca_plot.addSeries(
                        groups[i],
                        pc1_group,
                        pc2_group,
                        members_array//getSubArray(members_array,Collections.min(indexes), Collections.max(indexes))
                        //getSubArray(groups,Collections.min(indexes), Collections.max(indexes))
                        );
            }

        } else {
            for(int i = 0; i < groupOrder.length; i++){

                double[] pc1_group = getSubArray(pc1, i, i);
                double[] pc2_group = getSubArray(pc2, i, i);

                pca_plot.addSeries(
                        groupOrder[i],
                        //group_color.get(groupOrder[i]),
                        pc1_group,
                        pc2_group,
                        groupOrder
                        //getSubArray(groupOrder, i, i+1)
                        );
            }
        }

    }

    /**
     * Get sub array.
     * @param pc
     * @param start
     * @param end
     * @return
     */
    private double[] getSubArray(double[] pc, int start, int end) {
        if(start==end){
            return new double[]{pc[start]};
        }
        return Arrays.copyOfRange(pc, start, end+1);
    }


    /**
     * Get sub array.
     * @param pc
     * @param start
     * @param end
     * @return
     */
    private String[] getSubArray(String[] pc, int start, int end) {
        if(start==end){
            return new String[]{pc[start]};
        }
        return Arrays.copyOfRange(pc, start, end+1);
    }


    /**
     * Transpose matrix.
     *
     * @param m
     * @return
     */
    private static double[][] transposeMatrix(double [][] m){
        double[][] temp = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;

    }


    /**
     * GETTER AND SETTER
     *
     */
    public void setGroups(String[] groups) {
        this.groups = groups;
    }

    private double[][] getEigenvectors(EigenDecomposition ed, int numberOfEigenvectors) {
        double[][] result = new double[numberOfEigenvectors][];

        for(int i = 0; i < numberOfEigenvectors; i++){
            RealVector eigenvector = ed.getEigenvector(i);
            result[i] = eigenvector.toArray();
        }

        return result;

    }

    public ScatterPlot getPca_plot() {
        return pca_plot;
    }


}
