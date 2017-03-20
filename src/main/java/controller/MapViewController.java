package controller;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import netscape.javascript.JSObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by neukamm on 17.03.17.
 */
public class MapViewController implements MapComponentInitializedListener {

    private final TableColumn id_col;
    private final TableColumn location_col;
    private final ObservableList items;
    private GoogleMapView mapView;
    private GoogleMap map;
    private List<Marker> marker_all;

    public MapViewController(TableColumn id, TableColumn location, ObservableList items){

        id_col = id;
        location_col = location;
        this.items = items;

        mapView = new GoogleMapView();
        mapView.addMapInializedListener(this);

    }


    @Override
    public void mapInitialized() {

        //Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();

        mapOptions.overviewMapControl(false)
                .center(new LatLong(47.6097, -122.3331))
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(2);


        map = mapView.createMap(mapOptions);


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
                    map.addMarker(marker);


                    map.addUIEventHandler(marker, UIEventType.click, (JSObject obj) -> {
                        InfoWindowOptions infoOptions = new InfoWindowOptions();
                        infoOptions.content("<h2>Sample ID: " + id + "</h2>");
                        InfoWindow window = new InfoWindow(infoOptions);
                        window.open(mapView.getMap(), marker);
                    });

                }

            }

        }
    }


    public GoogleMapView getMapView() {
        return mapView;
    }

    public void setMapView(GoogleMapView mapView) {
        this.mapView = mapView;
    }

    public GoogleMap getMap() {
        return map;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }
}
