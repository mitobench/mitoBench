package view.visualizations;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by neukamm on 15.12.16.
 */
public class ColorSchemeStackedBarChart {



    public ColorSchemeStackedBarChart(Stage stage) throws MalformedURLException {
        // set stylesheet so specify colors
        URL url = this.getClass().getResource("/css/ColorsStackedBarChart.css");
        stage.getScene().getStylesheets().add(url.toExternalForm());

    }


    public void setNewColors(StackedBar stackedBar) {


        /**
         * Set Series color
         */
        for (int i = 0; i < stackedBar.getSeriesList().size(); i++) {
            for (Node node : stackedBar.getSbc().lookupAll(".series" + i)) {
                node.getStyleClass().remove("default-color" + (i % 8));
                node.getStyleClass().add("default-color"+i);
            }
        }

        /**
         * Set Legend items color
         */
        int i = 0;
        for (Node node : stackedBar.getSbc().lookupAll(".chart-legend-item")) {
            if (node instanceof Label && ((Label) node).getGraphic() != null) {
                ((Label) node).getGraphic().getStyleClass().remove("default-color" + (i % 8));
                ((Label) node).getGraphic().getStyleClass().add("default-color" + i);
            }
            i++;
        }
//        /**
//         * Set Series color
//         */
//        for (int i = 0; i < stackedBar.getSeriesList().size(); i++) {
//            for (Node node : stackedBar.getSbc().lookupAll(".series" + i)) {
//                String hg = node.getAccessibleText().split(" ")[0].trim();
//                node.getStyleClass().remove("default-color" + (i % 8));
//                node.getStyleClass().add("default-color"+hg);
//            }
//        }
//
//        /**
//         * Set Legend items color
//         */
//        int i = 0;
//        for (Node node : stackedBar.getSbc().lookupAll(".chart-legend-item")) {
//            if (node instanceof Label && ((Label) node).getGraphic() != null) {
//                String hg = ((Label) node).getText();
//                ((Label) node).getGraphic().getStyleClass().remove("default-color" + (i % 8));
//                ((Label) node).getGraphic().getStyleClass().add("default-color" + hg);
//            }
//            i++;
//        }

    }



    public void setNewColorsLess20(StackedBar stackedBar) {
        /**
         * Set Series color
         */
        for (int i = 0; i < stackedBar.getSeriesList().size(); i++) {
            for (Node node : stackedBar.getSbc().lookupAll(".series" + i)) {
                node.getStyleClass().remove("default-color" + (i % 8));
                node.getStyleClass().add("default-color"+i);
            }
        }

        /**
         * Set Legend items color
         */
        int i = 0;
        for (Node node : stackedBar.getSbc().lookupAll(".chart-legend-item")) {
            if (node instanceof Label && ((Label) node).getGraphic() != null) {
                ((Label) node).getGraphic().getStyleClass().remove("default-color" + (i % 8));
                ((Label) node).getGraphic().getStyleClass().add("default-color" + i);
            }
            i++;
        }

    }
}
