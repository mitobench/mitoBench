package analysis;

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;


public class PCA2 {

    private double[][] pointsArray;

    public void calculate (){

        //create real matrix
        RealMatrix realMatrix = MatrixUtils.createRealMatrix(pointsArray);

        //create covariance matrix of points, then find eigen vectors
        //see https://stats.stackexchange.com/questions/2691/making-sense-of-principal-component-analysis-eigenvectors-eigenvalues

        Covariance covariance = new Covariance(realMatrix);
        RealMatrix covarianceMatrix = covariance.getCovarianceMatrix();
        EigenDecomposition ed = new EigenDecomposition(covarianceMatrix);

        print(ed);



    }



    public void setData(double[][] data){
        pointsArray = data;
    }

    private void print(EigenDecomposition ed){
        int size = ed.getRealEigenvalues().length;
        for(int i = 0; i < size; i++){
            System.out.println(ed.getEigenvector(i).toString());
        }

    }


}
