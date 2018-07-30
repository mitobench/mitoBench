package view.visualizations;

import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import leafletMAP.MapView;


public class GeographicalMapViz {

    private BorderPane mapBasicPane;

    public GeographicalMapViz(){
        mapBasicPane = new BorderPane();
    }


    /**
     *      GETTER and SETTER
     */

    public BorderPane getMapBasicPane() {
        return mapBasicPane;
    }

    public void setCenter(MapView center) {
        this.mapBasicPane.setCenter(center);
    }

    public void setLeft(ListView listView) {
        this.mapBasicPane.setLeft(listView);
    }
}
