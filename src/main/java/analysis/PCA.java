package analysis;

import Logging.LogClass;
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

    private ScatterPlot pca_plot;
    private int numberOfDimensions = 2;
    private String[] groups;

    public PCA(){}

    public double[][] calculate(double[][] values, int numPC){

        numberOfDimensions = numPC;

        double[][] values_meanCorrected = subtractMean(values);

        //create real matrix
        RealMatrix realMatrix = MatrixUtils.createRealMatrix(values_meanCorrected);

        //create covariance matrix of points, then find eigen vectors

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

    private double[][] subtractMean(double[][] values) {

        for(int i = 0; i < values.length; i++){
            double mean = Arrays.stream(values[i]).average().getAsDouble();
            for(int j = 0; j < values[i].length; j++){
                values[i][j] = values[i][j] + mean;
            }
        }

        return values;
    }






    public void plot(double[][] result_pca, HashMap<String, Color> group_color, Stage stage, LogClass logClass, TabPane tabpane_statistics) {
        pca_plot = new ScatterPlot(stage, logClass, tabpane_statistics);
        groups = group_color.keySet().toArray(new String[group_color.keySet().size()]);

        double[] pc1 = result_pca[0];
        double[] pc2 = result_pca[1];

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
        for(int i = 0; i < groups.length; i++){
            pca_plot.addSeries(groups[i], group_color.get(groups[i]), pc1[i], pc2[i]);
        }



    }

    public void setGroups(String[] groups) {
        this.groups = groups;
    }

    public static double[][] transposeMatrix(double [][] m){
        double[][] temp = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;

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
