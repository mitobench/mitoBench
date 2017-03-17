package view.map;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import netscape.javascript.JSObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neukamm on 17.03.17.
 */
public class MapViewController {

    private final ObservableList<ObservableList> items;
    private final TableColumn id_col;
    private final TableColumn location_col;

    private MapView mapView;
    private List<Marker> marker_all;


    public MapViewController(TableColumn id_column, TableColumn location_column, ObservableList<ObservableList> tableItems){
        id_col = id_column;
        location_col = location_column;
        items = tableItems;

    }

    public void createMarkers(){

        if(location_col!=null){
            marker_all = new ArrayList<>();

            for (Object item : items) {
                String id = id_col.getCellObservableValue(item).getValue().toString();
                String[] loc = location_col.getCellObservableValue(item).getValue().toString().split(";");
                if(loc.length==2){
                    double latitude = Double.parseDouble(loc[0]);
                    double longitude = Double.parseDouble(loc[1]);

                    MarkerOptions markerOptions = new MarkerOptions();

                    markerOptions.position( new LatLong(latitude, longitude) )
                            .visible(Boolean.TRUE)
                            .title(id);

                    Marker marker = new Marker( markerOptions );
                    mapView.getMap().addMarker(marker);

                    mapView.getMap().addUIEventHandler(marker, UIEventType.click, (JSObject obj) -> {
                        InfoWindowOptions infoOptions = new InfoWindowOptions();
                        infoOptions.content("<h2>Sample ID: " + id + "</h2>");
                        InfoWindow window = new InfoWindow(infoOptions);
                        window.open(mapView.getMap(), marker);
                    });
                }

            }

        }
    }


    public MapView getMapView() {
        return mapView;
    }

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }
}
