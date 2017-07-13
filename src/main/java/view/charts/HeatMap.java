package view.charts;

import Logging.LogClass;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.awt.image.BufferedImage;

/**
 * Created by neukamm on 13.07.17.
 */




public class HeatMap extends AChart{

    BorderPane heatMap = new BorderPane();

    public HeatMap(String lable_xaxis, String label_yaxis, LogClass logClass) {
        super(lable_xaxis, label_yaxis, logClass);

    }


    public void createHeatMap(double[][] fsts, String[] groupnames){

        HeatChart heat = new HeatChart(transposeMatrix(fsts), 0.0, 1.0);
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