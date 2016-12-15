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

    List<String> core_hgs = Arrays.asList("L0", "L1", "L2", "L3", "L4",
                                          "M1", "N", "I", "W", "X", "R",
                                          "R0", "U", "K", "J", "T", "T1",
                                          "T2", "H", "HV");

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



        File file = new File("/home/neukamm/IdeaProjects/MitoBench/src/main/java/view/charts/Colors.css");
        URL url = file.toURI().toURL();
        stage.getScene().getStylesheets().add(url.toExternalForm());

    }


    public void setNewColors(StackedBar stackedBar, String[] haplogroups) {



        /**
         * Set Series color
         */
        for (int i = 0; i < stackedBar.getSeriesList().size(); i++) {
            for (Node node : stackedBar.getSbc().lookupAll(".series" + i)) {
                node.getStyleClass().remove("default-color" + (i % 8));
                node.getStyleClass().add("default-color" + (i % 28));
            }
        }

        /**
         * Set Legend items color
         */
        int i = 0;
        for (Node node : stackedBar.getSbc().lookupAll(".chart-legend-item")) {
            if (node instanceof Label && ((Label) node).getGraphic() != null) {
                ((Label) node).getGraphic().getStyleClass().remove("default-color" + (i % 8));
                ((Label) node).getGraphic().getStyleClass().add("default-color" + (i % 28));
            }
            i++;
        }

//        int i = 0;
//        while(i < haplogroups.length){
//            for (Node n : stackedBar.getSbc().lookupAll(".default-color" + i + ".chart-bar")) {
//                String col = colors.get(haplogroups[i]);
//                if(col!=null && col.length()!=0){
//                    n.setStyle("-fx-bar-fill:black;");
//                } else {
//                    n.setStyle("-fx-bar-fill:white;");
//                }
//            }
//            i++;
//        }



//        // Array "haplogroups" are ordered as added to bar plot
//        for (int i = 0; i < haplogroups.length; i++) {
//            String hg = haplogroups[i];
//
//            for (Node n : stackedBar.getSbc().lookupAll(".default-color" + i + ".chart-bar")) {
//                String col = colors.get(hg);
//                if(col!=null && col.length()!=0){
//                    //n.setStyle("-fx-bar-fill:#161616;");
//                    n.setStyle(colors.get(hg));
//                }
//
//            }
//        }
    }




}
