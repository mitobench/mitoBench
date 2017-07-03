package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.BorderPane;
import leaflet.MapView;
import leaflet.MarkerIcons;
import net.java.html.boot.fx.FXBrowsers;
import net.java.html.leaflet.*;
import view.table.controller.TableControllerUserBench;

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
    private final TableColumn grouping_col;
    private final ObservableList items;
    private final MapView map;
    private final BorderPane borderPane;
    private final ListView listView;


    public LeafletController(TableColumn id, TableColumn location, TableColumn grouping, ObservableList<String> items,
                             GroupController groupController, TableControllerUserBench tableController)
            throws FileNotFoundException, URISyntaxException, MalformedURLException {

        id_col = id;
        location_col = location;
        grouping_col = grouping;
        this.items = items;


        map = new MapView();
        borderPane = new BorderPane();
        borderPane.setCenter(map);


        // a regular JavaFX ListView
        listView = new ListView<>();
        initMarker(listView);

        // we listen for the selected item and update the map accordingly
        // as a demo of how to interact between JavaFX and DukeScript
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Address>() {
            @Override
            public void changed(ObservableValue<? extends Address> ov, Address old_val, final Address new_val) {
                FXBrowsers.runInBrowser(map.getWebView(), () -> {

                    LatLng pos = new LatLng(new_val.getLat(), new_val.getLng());
                    map.getMap().setView(pos, 5);
                    map.getMap().openPopup(new_val.toString(), pos);
                    map.getMap().latLngToLayerPoint(pos);

                });
            }
        });

        borderPane.setLeft(listView);
        Button showAllData = new Button("Show all data");
        borderPane.setBottom(showAllData);

        showAllData.setOnAction(e ->

                FXBrowsers.runInBrowser(map.getWebView(), () -> {

                    MarkerIcons markerIcons = new MarkerIcons(groupController, tableController);
                    markerIcons.setItems(listView.getItems());

                    if(grouping!=null){
                        // get groups
                        List<String> columnData = new ArrayList<>();
                        for( Object item : items) {
                            columnData.add(grouping.getCellObservableValue(item).getValue().toString());
                        }
                        markerIcons.setGroups(columnData);
                    }

                    markerIcons.addIconsToMap(map.getMap());


                }));

    }


    private void initMarker(ListView<Address> listView) {
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

    public static class Address {

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


    public MapView getMapView() {return map;}
    public BorderPane getMap() {
        return borderPane;
    }

    public ListView getListView() {
        return listView;
    }
}
