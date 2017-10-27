package view.visualizations;

import Logging.LogClass;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * Created by neukamm on 13.07.17.
 */




public class HeatMap extends AChart{

    BorderPane heatMap = new BorderPane();

    public HeatMap(String lable_xaxis, String label_yaxis, LogClass logClass) {
        super(lable_xaxis, label_yaxis, logClass);

    }


    /**
     * Create image with heatmap and legend
     * @param fsts
     * @param groupnames
     * @param min
     * @param max
     */
    public void createHeatMap(double[][] fsts, String[] groupnames, double min, double max){

        double[] count = new double[fsts.length];
        for (int i = 0; i < fsts.length; i++) {
            count[i] = Arrays.stream( fsts[i]).max().getAsDouble();
        }
        double MAX = Arrays.stream( count).max().getAsDouble();
        //double MAX = max;

        double[] count_min = new double[fsts.length];
        for (int i = 0; i < fsts.length; i++) {
            count_min[i] = Arrays.stream( fsts[i]).min().getAsDouble();
        }
        double MIN = Arrays.stream( count_min).min().getAsDouble();
        //double MIN = min;

        HeatChart heat = new HeatChart(transposeMatrix(fsts), MIN, MAX);
        heat.setTitle("Fst values");
        heat.setXValues(groupnames);
        heat.setYValues(groupnames);

        Image i = SwingFXUtils.toFXImage((BufferedImage) heat.getChartImage(false), null);
        ImageView v = new ImageView(i);
        heatMap.setCenter(v);

        HeatMapLegend legend = new HeatMapLegend(v.getImage().getWidth(), heat);
        VBox heatmap = legend.getRoot();
        heatMap.setAlignment(heatmap, Pos.TOP_CENTER);
        heatMap.setBottom(heatmap);

    }

    /**
     * Transpose matrix.
     *
     * @param matrix
     * @return
     */
    public static double[][] transposeMatrix(double[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;

        double[][] transposedMatrix = new double[n][m];

        for(int x = 0; x < n; x++)
        {
            for(int y = 0; y < m; y++)
            {
                transposedMatrix[x][y] = matrix[y][x];
            }
        }

        return transposedMatrix;
    }


    /**
     *  SETTER AND GETTER
     *
     */
    public BorderPane getHeatMap() {
        return heatMap;
    }
    public void setContextMenu(TabPane tab){
        setContextMenu(heatMap, tab);
    }
}