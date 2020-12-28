package analysis;


import Logging.LogClass;
import controller.ChartController;
import controller.TableControllerUserBench;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.ojalgo.OjAlgoUtils;
import org.ojalgo.data.DataProcessors;
import org.ojalgo.function.aggregator.Aggregator;
import org.ojalgo.matrix.decomposition.Eigenvalue;
import org.ojalgo.matrix.decomposition.SingularValue;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.PhysicalStore;
import org.ojalgo.matrix.store.PrimitiveDenseStore;
import org.ojalgo.netio.BasicLogger;
import org.ojalgo.type.context.NumberContext;
import statistics.HaploStatistics;
import view.visualizations.PCAPlot;

import java.util.*;



public class PCA_Analysis {
    private static PhysicalStore.Factory<Double, PrimitiveDenseStore> MATRIX_FACTORY = PrimitiveDenseStore.FACTORY;
    private static NumberContext PERCENTAGE = NumberContext.getPercent(Locale.getDefault());
    private PrimitiveDenseStore data;
    private MatrixStore<Double> result;
    private String variancePC1;
    private String variancePC2;
    private String[] groups;
    private PCAPlot pca_plot;


    public void calculate(){

        BasicLogger.debug();
        BasicLogger.debug(OjAlgoUtils.getTitle());
        BasicLogger.debug(OjAlgoUtils.getDate());
        BasicLogger.debug();


        //BasicLogger.debug("Data", data);
        long numberOfVariables = data.countColumns();
        long numberOfSamples = data.countRows();
        //BasicLogger.debug("There are {} variables and {} samples.", numberOfVariables, numberOfSamples);

        /*
         * First we'll calculate the covariance and correlations matrices from the variable samples, and then
         * (using the covariance matrix) we'll calculate the EvD. We do this to verify the results of the SVD
         * method, and to show how the EvD and the SVD are related.
         */

        // First we create the covariance matrix. Forget about EvD, SVD, PCA...
        // This is simply the covariances of the sampled variables.
        PrimitiveDenseStore covariances = DataProcessors.covariances(MATRIX_FACTORY, data);
        //BasicLogger.debug();
        //BasicLogger.debug("Covariances", covariances);

        // Then calculate the EvD of that covariance matrix.
        Eigenvalue<Double> evd = Eigenvalue.PRIMITIVE.make(covariances);
        evd.decompose(covariances);
//        BasicLogger.debug();
//        BasicLogger.debug("EvD");
//        BasicLogger.debug("Eigenvalues (on the diagonal)", evd.getD());
//        BasicLogger.debug("Eigenvectors (in the columns)", evd.getV());

//        double sumOfEigenvalues = evd.getD().aggregateDiagonal(Aggregator.SUM);
//        BasicLogger.debug();
//        BasicLogger.debug("Relative (Variance) Importance: {}", evd.getD().operateOnAll(DIVIDE.by(sumOfEigenvalues)).sliceDiagonal());

        /*
         * Now let's start with the actual SVD
         */

        // Center the meassurements for each of the variables
        data.modifyAny(DataProcessors.CENTER);
//        BasicLogger.debug();
//        BasicLogger.debug("Data (centered)", data);

        SingularValue<Double> svd = SingularValue.PRIMITIVE.make(data);
        svd.decompose(data);

//        BasicLogger.debug();
//        BasicLogger.debug("SVD");
//        BasicLogger.debug("Left-singular Vectors (in the columns)", svd.getQ1());
//        BasicLogger.debug("Singular values (on the diagonal)", svd.getD());
//        BasicLogger.debug("Right-singular Vectors (in the columns) - compare these to the eigenvectors above", svd.getQ2());

        // Singular Value corresponding to PC1
        double pc1SV = svd.getD().doubleValue(0, 0);
        // Singular Value corresponding to PC2
        double pc2SV = svd.getD().doubleValue(1, 1);

        double pc1Variance = (pc1SV * pc1SV) / (numberOfSamples - 1);
        double pc2Variance = (pc2SV * pc2SV) / (numberOfSamples - 1);

        double sumOfSquaredSingularValues = svd.getD().aggregateDiagonal(Aggregator.SUM2);
        double sumOfVariances = sumOfSquaredSingularValues / (numberOfSamples - 1);

        // The sum of the eigenvalues (of the covariance matrix) should equal
        // the sum of the squared singular values (of the centered data)
        // divided by the degrees of freedom .
        //BasicLogger.debug();
        //BasicLogger.debug("Sum of eigenvalues/variance: {} == {}", sumOfEigenvalues, sumOfVariances);

        //BasicLogger.debug();
        this.variancePC1 = PERCENTAGE.format(pc1Variance / sumOfVariances);
        this.variancePC2 = PERCENTAGE.format(pc2Variance / sumOfVariances);
        //BasicLogger.debug("PC1: Variance={} ({}%) Loadings={}", pc1Variance, PERCENTAGE.format(pc1Variance / sumOfVariances), svd.getQ2().sliceColumn(0));
        //BasicLogger.debug("PC2: Variance={} ({}%) Loadings={}", pc2Variance, PERCENTAGE.format(pc2Variance / sumOfVariances), svd.getQ2().sliceColumn(1));

        // The loadings describe how to create the principal components from the original
        // variables. Multiply the data matrix by each of the loadings vectors you want to use.
        // In this example we keep the 2 first, most important, principal components.
        // Then we get a matrix with 2 columns corresponding to PC1 and PC2.
        //BasicLogger.debug();
        result = data.multiply(svd.getQ2().logical().column(0, 1).get());
        //BasicLogger.debug("Data (transformed)", data.multiply(svd.getQ2().logical().column(0, 1).get()));

        // There is another way to get the same thing from the SVD.
        // The left-singular vectors scaled by their corresponding singular values.
        //BasicLogger.debug();
        //BasicLogger.debug("Transformed data (derived another way) - compare the 2 first columns with what we just calculated above",
        //        svd.getQ1().multiply(svd.getD()));

        // It's also possible to recreate the covariance matrix from the SVD
        //BasicLogger.debug();
        //BasicLogger.debug("Covariances (from SVD) – compare this what we originally calculated", DataProcessors.covariances(MATRIX_FACTORY, svd));

        // And when we construct the covariance matrix from the SVD,
        // we can optionally remove some noise
        //BasicLogger.debug("Covariances (from SVD using only 2 components)", DataProcessors.covariances(MATRIX_FACTORY, svd, 2));

    }


    /**
     * Plot adjusted values.
     * @param result_pca
     * @param stage
     * @param logClass
     * @param tabpane_statistics
     * @param group_members
     * @param chartController
     * @param variancePC1
     * @param variancePC2
     * @param haploStatistics
     * @param tableControllerUserBench
     */
    public void plot(double[][] result_pca, Stage stage, LogClass logClass, TabPane tabpane_statistics,
                     HashMap<String, ObservableList<String>> group_members, ChartController chartController,
                     String variancePC1, String variancePC2, HaploStatistics haploStatistics, TableControllerUserBench tableControllerUserBench) {

        pca_plot = new PCAPlot(stage, logClass, tabpane_statistics, haploStatistics, tableControllerUserBench);
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
                upperbound_y.getAsDouble()+0.5,
                "PC1 (" + variancePC1 + " variance)",
                "PC2 (" + variancePC2 + " variance)",
                ""
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
                double[] pc1_group =  Arrays.copyOfRange(pc1, Collections.min(indexes), Collections.max(indexes) + 1);
                double[] pc2_group = Arrays.copyOfRange(pc2, Collections.min(indexes), Collections.max(indexes) + 1);

                String[] members_array = members.toArray(new String[members.size()]);
                pca_plot.addSeries(
                        groups[i],
                        pc1_group,
                        pc2_group,
                        members_array
                );
            }

        } else {
            for(int i = 0; i < groupOrder.length; i++){

                double[] pc1_group = Arrays.copyOfRange(pc1, i, i + 1);
                double[] pc2_group = Arrays.copyOfRange(pc2, i, i + 1);

                pca_plot.addSeries(
                        groupOrder[i],
                        pc1_group,
                        pc2_group,
                        new String[]{groupOrder[i]}
                );
            }
        }
    }


    public void setData(double[][] frequencies) {
        this.data = MATRIX_FACTORY.columns(transposeMatrix(frequencies));
    }

    public double[][] getResult() {

        return  transposeMatrix(result.toRawCopy2D());
    }

    public String getVariancePC1() {
        return variancePC1;
    }

    public String getVariancePC2() {
        return variancePC2;
    }

    public void setGroups(String[] groups) {
        this.groups = groups;
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

    public PCAPlot getPca_plot() {
        return pca_plot;
    }
}
