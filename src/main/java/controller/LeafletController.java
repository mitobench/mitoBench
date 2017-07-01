package controller;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.BorderPane;
import leaflet.MainApp;
import leaflet.MapView;
import net.java.html.boot.fx.FXBrowsers;
import net.java.html.leaflet.LatLng;
import netscape.javascript.JSObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neukamm on 01.07.17.
 */
public class LeafletController {

    private final TableColumn id_col;
    private final TableColumn location_col;
    private final ObservableList items;
    private final MapView map;
    private final BorderPane borderPane;


    public LeafletController(TableColumn id, TableColumn location, ObservableList items) throws FileNotFoundException, URISyntaxException, MalformedURLException {

        id_col = id;
        location_col = location;
        this.items = items;


        map = new MapView();
        borderPane = new BorderPane();
        borderPane.setCenter(map);


        // a regular JavaFX ListView
        ListView<Address> listView = new ListView<>();
        addDataToMap(listView);

        // we listen for the selected item and update the map accordingly
        // as a demo of how to interact between JavaFX and DukeScript
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Address>() {
            @Override
            public void changed(ObservableValue<? extends Address> ov, Address old_val, final Address new_val) {
                FXBrowsers.runInBrowser(map.getWebView(), new Runnable() {
                    @Override
                    public void run() {
                        LatLng pos = new LatLng(new_val.getLat(), new_val.getLng());
                        map.getMap().setView(pos, 20);
                        map.getMap().openPopup("Here is " + new_val, pos);
                    }
                });
            }
        });

        borderPane.setLeft(listView);

    }

    private void addDataToMap(ListView<Address> listView) {
        List<Address> marker_all = new ArrayList<>();

        if(location_col!=null){

            for (Object item : items) {
                String id = id_col.getCellObservableValue(item).getValue().toString();
                String[] loc = location_col.getCellObservableValue(item).getValue().toString().split(",");
                if(loc.length==2){
                    double latitude = Double.parseDouble(loc[0]);
                    double longitude = Double.parseDouble(loc[1]);

                    marker_all.add(new Address(id, latitude, longitude));
                }

            }

        }

        listView.getItems().addAll(marker_all);

    }

    private static class Address {

        private final String name;
        private final double lat;
        private final double lng;

        public Address(String name, double lat, double lng) {
            this.name = name;
            this.lat = lat;
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }

        @Override
        public String toString() {
            return name;
        }



    }

    public BorderPane getMap() {
        return borderPane;
    }
}
