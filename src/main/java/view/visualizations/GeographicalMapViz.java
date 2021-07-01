package view.visualizations;

import javafx.geometry.Orientation;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import leafletMAP.MapView;


public class GeographicalMapViz {

    private BorderPane mapBasicPane;
    private SplitPane basicPane;

    public GeographicalMapViz(){
        basicPane = new SplitPane();
        basicPane.setOrientation(Orientation.VERTICAL);
        mapBasicPane = new BorderPane();
        basicPane.getItems().add(mapBasicPane);

    }


    /**
     *      GETTER and SETTER
     */

    public BorderPane getMapBasicPane() {
        return mapBasicPane;
    }

    public SplitPane getBasicPane() {
        return basicPane;
    }

    public void setCenter(MapView center) {
        this.mapBasicPane.setCenter(center);
    }

    public void setLeft(ListView listView) {
        this.mapBasicPane.setLeft(listView);
    }
}
