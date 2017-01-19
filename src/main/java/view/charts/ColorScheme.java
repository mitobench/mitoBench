package view.charts;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 15.12.16.
 */
public class ColorScheme {

    HashMap<String, Integer> colors = new HashMap<>();

    public ColorScheme(Stage stage) throws MalformedURLException {


        colors.put("L0", 0);
        colors.put("L1", 1);
        colors.put("L2", 2);
        colors.put("L3", 3);
        colors.put("L4", 4);
        colors.put("M1", 5);
        colors.put("N", 6);
        colors.put("I", 7);
        colors.put("W", 8);
        colors.put("X", 9);
        colors.put("R", 10);
        colors.put("R0",11);
        colors.put("U", 12);
        colors.put("K", 13);
        colors.put("J", 14);
        colors.put("T", 15);
        colors.put("T1", 16);
        colors.put("T2", 17);
        colors.put("H", 18);
        colors.put("HV", 19);



        File f = new File("src/main/java/view/charts/css/Colors.css");
        stage.getScene().getStylesheets().clear();
        stage.getScene().getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

    }


    public void setNewColors(StackedBar stackedBar) {
        /**
         * Set Series color
         */
        for (int i = 0; i < stackedBar.getSeriesList().size(); i++) {
            for (Node node : stackedBar.getSbc().lookupAll(".series" + i)) {
                String hg = node.getAccessibleText().split(" ")[0].trim();
                node.getStyleClass().remove("default-color" + (i % 8));
                //node.getStyleClass().add("default-color" + (i % 28));
                node.getStyleClass().add("default-color"+hg);
            }
        }

        /**
         * Set Legend items color
         */
        int i = 0;
        for (Node node : stackedBar.getSbc().lookupAll(".chart-legend-item")) {
            if (node instanceof Label && ((Label) node).getGraphic() != null) {
                String hg = ((Label) node).getText();
                ((Label) node).getGraphic().getStyleClass().remove("default-color" + (i % 8));
                ((Label) node).getGraphic().getStyleClass().add("default-color" + hg);
            }
            i++;
        }

    }

    public  void setColorsBarChart(ABarPlot barPlot){


    }




}
