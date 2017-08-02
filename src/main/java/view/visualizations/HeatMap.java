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
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 13.07.17.
 */




public class HeatMap extends AChart{

    BorderPane heatMap = new BorderPane();

    public HeatMap(String lable_xaxis, String label_yaxis, LogClass logClass) {
        super(lable_xaxis, label_yaxis, logClass);
    }


//    public void createHeatMap(double[][] values, String[] labels, String title,
//                              HashMap<String, List<String>> haplotype_sharing){
public void createHeatMap(double[][] values, String[] labels, String title){



        double[] count = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            count[i] = Arrays.stream( values[i]).max().getAsDouble();
        }
        double MAX = Arrays.stream( count).max().getAsDouble();

        double[] count_min = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            count_min[i] = Arrays.stream( values[i]).min().getAsDouble();
        }
        double MIN = Arrays.stream( count_min).min().getAsDouble();

        HeatChart heat = new HeatChart(transposeMatrix(values), MIN, MAX);
        heat.setTitle(title);
        heat.setXValues(labels);
        heat.setYValues(labels);

        Image i = SwingFXUtils.toFXImage((BufferedImage) heat.getChartImage(false), null);
        ImageView v = new ImageView(i);
        heatMap.setCenter(v);
        //heatMap.setCenter(heat.getGrid(values, labels, haplotype_sharing));

        HeatMapLegend legend = new HeatMapLegend(v.getImage().getWidth(), heat);
        VBox heatmap = legend.getRoot();
        heatMap.setAlignment(heatmap, Pos.TOP_CENTER);

        heatMap.setBottom(heatmap);

    }

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

    public BorderPane getHeatMap() {
        return heatMap;
    }


    public void setContextMenu(TabPane tab){
        setContextMenu(heatMap, tab);
    }
}