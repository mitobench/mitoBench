package view.map;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;

/**
 * Created by neukamm on 17.03.17.
 */
public class MapView implements MapComponentInitializedListener {

    private GoogleMapView mapView;
    private GoogleMap map;

    @Override
    public void mapInitialized() {

        mapView = new GoogleMapView();
        mapView.addMapInializedListener(this);

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
