package view.charts;

import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;

/**
 * Created by neukamm on 16.02.17.
 */
public class ColorSchemeBarchart {

    HashMap<String, String> colors = new HashMap<>();


    public ColorSchemeBarchart(Stage stage) throws MalformedURLException {
        // set stylesheet so specify colors
        File f = new File("src/main/java/view/charts/css/Colors.css");
        stage.getScene().getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));



        colors.put("L0", "-fx-bar-fill: #ffce1b;");
        colors.put("L1", "-fx-bar-fill: #ff9030;");
        colors.put("L2", "-fx-bar-fill: #ff4b29;");
        colors.put("L3", "-fx-bar-fill: red;");
        colors.put("L4", "-fx-bar-fill: #960e0c;");
        colors.put("M1", "-fx-bar-fill: #1a2ae8;");
        colors.put("N", "-fx-bar-fill: #2ce99b;");
        colors.put("I", "-fx-bar-fill: #32ca29;");
        colors.put("W", "-fx-bar-fill: #009a32;");
        colors.put("X", "-fx-bar-fill: #185933;");
        colors.put("R", "-fx-bar-fill: #512603;");
        colors.put("R0","-fx-bar-fill: #814f11;");
        colors.put("U", "-fx-bar-fill: #dd82d1;");
        colors.put("K", "-fx-bar-fill: #740492;");
        colors.put("J", "-fx-bar-fill: #93e0df;");
        colors.put("T", "-fx-bar-fill: #178e92;");
        colors.put("T1", "-fx-bar-fill: #026e75;");
        colors.put("T2", "-fx-bar-fill: #065a83;");
        colors.put("H", "-fx-bar-fill: darkgrey;");
        colors.put("HV", "-fx-bar-fill: grey;");

    }


    public Node setColor(Node node, String hg){
        node.setStyle(colors.get(hg));

        return node;

    }
}
