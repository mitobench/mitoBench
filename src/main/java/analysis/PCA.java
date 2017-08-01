package analysis;

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.jblas.ComplexDoubleMatrix;
import org.jblas.DoubleMatrix;
import org.jblas.Eigen;
import view.visualizations.ScatterPlot;

import java.util.*;


public class PCA {

    private double[][] pointsArray;
    private ScatterPlot pca_plot;
    private EigenDecomposition ed;

    public PCA(){}

    public void calculate (){


//        double[][] src = new double[][]{
//                {-1, -1, 0, 2, 0},
//                {-2, 0, 0, 1, 1}
//        };
//        DoubleMatrix mt = new DoubleMatrix(src);
//        DoubleMatrix covariance = mt.mmul(mt.transpose()).div(mt.columns);
//        ComplexDoubleMatrix eigVal = Eigen.eigenvalues(covariance);
//        ComplexDoubleMatrix[] eigVector = Eigen.eigenvectors(covariance);
//        System.out.println(eigVal);
//        System.out.println(Arrays.toString(eigVector));
//        ComplexDoubleMatrix cvec = eigVector[0];
//
//        for (int i = 0; i < eigVal.length - 1; i++)
//        {
//            for (int j = 0; j < eigVal.length - i - 1; j++)
//            {
//                double j1 = eigVal.get(j).real();
//                double j2 = eigVal.get(j + 1).real();
//                if (j2 > j1)
//                {
//                    cvec.swapColumns(j, j + 1);
//                    eigVal.swapRows(j, j + 1);
//                }
//            }
//        }
//        cvec = cvec.transpose();
//        System.out.println(cvec);
//        DoubleMatrix mt2 = cvec.getReal();
//        mt2 = mt2.getRange(0, 1, 0, mt2.columns);
//        System.out.println(mt2.mmul(mt));
//
//        System.out.println(PCA.dimensionReduction(mt, 1));

        //create real matrix
        RealMatrix realMatrix = MatrixUtils.createRealMatrix(pointsArray);

        //create covariance matrix of points, then find eigen vectors

        Covariance covariance = new Covariance(realMatrix);
        RealMatrix covarianceMatrix = covariance.getCovarianceMatrix();
        ed = new EigenDecomposition(covarianceMatrix);


        DoubleMatrix mt = new DoubleMatrix(pointsArray);
        DoubleMatrix mt2 = new DoubleMatrix(getResult());


        System.out.println(mt2.mmul(mt));
        //System.out.println(PCA.dimensionReduction(mt, 1));

        //print(ed);

    }



    public void setData(double[][] data){
        pointsArray = data;
    }

    private void print(EigenDecomposition ed){
        int size = ed.getRealEigenvalues().length;
        for(int i = 0; i < size; i++){
            System.out.println(ed.getEigenvector(i));
        }

    }


    public void plot() {
        pca_plot = new ScatterPlot();

        //double[] pc1 = getPC(0);
        //double[] pc2 = getPC(1);

//        OptionalDouble lowerbound_x = Arrays.stream(pc1).min();
//        OptionalDouble lowerbound_y = Arrays.stream(pc2).min();
//        OptionalDouble upperbound_x = Arrays.stream(pc1).max();
//        OptionalDouble upperbound_y = Arrays.stream(pc2).max();
//
//        pca_plot.create(
//                (int)lowerbound_x.getAsDouble(),
//                (int)lowerbound_y.getAsDouble(),
//                (int)upperbound_x.getAsDouble(),
//                (int)upperbound_y.getAsDouble());
//
//        pca_plot.addSeries("", pc1, pc2);


    }

    private double[][] getResult() {


        RealVector eigenvector0 = ed.getEigenvector(0);
        RealVector eigenvector1 = ed.getEigenvector(1);

        double[][] result = new double[2][];
        result[0] = eigenvector0.toArray();
        result[1] = eigenvector1.toArray();

        return result;
        // double[][] data_transposed = transposeMatrix(result);
        // return data_transposed;

    }



    public static double[][] transposeMatrix(double [][] m){
        double[][] temp = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }


    public ScatterPlot getPca_plot() {
        return pca_plot;
    }




    /**
     * Reduce matrix dimension
     *
     * @param source Source matrix
     * @param dimension Target dimension
     * @return Target matrix
     */
    public static DoubleMatrix dimensionReduction(DoubleMatrix source, int dimension)
    {
        //C=X*X^t / m
        DoubleMatrix covMatrix = source.mmul(source.transpose()).div(source.columns);
        ComplexDoubleMatrix eigVal = Eigen.eigenvalues(covMatrix);
        ComplexDoubleMatrix[] eigVectorsVal = Eigen.eigenvectors(covMatrix);
        ComplexDoubleMatrix eigVectors = eigVectorsVal[0];
        //Sort sigen vector from big to small by eigen values
        List<PCABean> beans = new ArrayList<PCABean>();
        for (int i = 0; i < eigVectors.columns; i++)
        {
            beans.add(new PCABean(eigVal.get(i).real(), eigVectors.getColumn(i)));
        }
        Collections.sort(beans);
        DoubleMatrix newVec = new DoubleMatrix(dimension, beans.get(0).vector.rows);
        for (int i = 0; i < dimension; i++)
        {
            ComplexDoubleMatrix dm = beans.get(i).vector;
            DoubleMatrix real = dm.getReal();
            newVec.putRow(i, real);
        }
        return newVec.mmul(source);
    }

    static class PCABean implements Comparable<PCABean>
    {
        double eigenValue;

        ComplexDoubleMatrix vector;

        public PCABean(double eigenValue, ComplexDoubleMatrix vector)
        {
            super();
            this.eigenValue = eigenValue;
            this.vector = vector;
        }



        @Override
        public int compareTo(PCABean o)
        {
            return Double.compare(o.eigenValue, eigenValue);
        }



        @Override
        public String toString()
        {
            return "PCABean [eigenValue=" + eigenValue + ", vector=" + vector + "]";
        }

    }
}
