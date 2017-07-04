package controller;

import com.sun.javafx.charts.Legend;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import leaflet.Location;
import leaflet.MapView;
import leaflet.MarkerIcons;
import net.java.html.boot.fx.FXBrowsers;
import net.java.html.leaflet.*;
import view.MitoBenchWindow;
import view.table.controller.TableControllerUserBench;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * This class controls all a actions of the map.
 *
 * Created by neukamm on 01.07.17.
 */
public class LeafletController {

    private final TableColumn id_col;
    private final TableColumn location_col;
    private final ObservableList items;
    private final MapView map;
    private final BorderPane mapBasicPane;
    private final ListView listView;
    private final TableColumn grouping_col;
    private TableControllerUserBench tableControllerUserBench;


    public LeafletController(MitoBenchWindow mitoBenchWindow,
                             GroupController groupController)
            throws FileNotFoundException, URISyntaxException, MalformedURLException {

        tableControllerUserBench = mitoBenchWindow.getTableControllerUserBench();
        grouping_col = tableControllerUserBench.getTableColumnByName("Grouping");

        id_col = tableControllerUserBench.getTableColumnByName("ID");
        location_col = tableControllerUserBench.getTableColumnByName("Location");
        items = tableControllerUserBench.getTable().getItems();

        map = new MapView();
        mapBasicPane = new BorderPane();
        mapBasicPane.setCenter(map);

        // a regular JavaFX ListView
        listView = new ListView<>();
        initMarker(listView);


        // we listen for the selected item and update the map accordingly
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Location>() {
            @Override
            public void changed(ObservableValue<? extends Location> ov, Location old_val, final Location new_val) {
                FXBrowsers.runInBrowser(map.getWebView(), () -> {

                    LatLng pos = new LatLng(new_val.getLat(), new_val.getLng());
                    map.getMap().setView(pos, 5);
                    map.getMap().openPopup(new_val.toString(), pos);
                    map.getMap().latLngToLayerPoint(pos);

                });
            }
        });

        mapBasicPane.setLeft(listView);
        Button showAllData = new Button("Show all data");

        mapBasicPane.setBottom(showAllData);

        // show all markers by clicking on the "show all data" button
        showAllData.setOnAction(e ->
                FXBrowsers.runInBrowser(map.getWebView(), () -> {

                    MarkerIcons markerIcons = new MarkerIcons(groupController, tableControllerUserBench);
                    markerIcons.setItems(listView.getItems());

                    if(grouping_col != null){
                        // get groups
                        List<String> columnData = new ArrayList<>();
                        for( Object item : items) {
                            columnData.add(grouping_col.getCellObservableValue(item).getValue().toString());
                        }
                        markerIcons.setGroups(columnData);
                    }

                    markerIcons.addIconsToMap(map.getMap());

                    Legend legend = markerIcons.getLegend();
                    legend.setVertical(true);
                    VBox rightBox = new VBox();
                    rightBox.getChildren().add(legend);
                    mapBasicPane.setRight(rightBox);
                }
                )
        );

    }


    /**
     * This method initializes the markers. This means, a location is created for each sample (with location
     * information available).
     *
     * @param list_locations
     */
    private void initMarker(ListView<Location> list_locations) {
        List<Location> marker_all = new ArrayList<>();

        if(location_col!=null){

            for (Object item : items) {
                String id = id_col.getCellObservableValue(item).getValue().toString();
                String location  = location_col.getCellObservableValue(item).getValue().toString();
                if(!location.equals("Undefined")){
                    String[] loc = location.split(",");
                    if(loc.length==2){
                        double latitude = Double.parseDouble(loc[0]);
                        double longitude = Double.parseDouble(loc[1]);
                        marker_all.add(new Location(id, latitude, longitude));
                    }
                }
            }
        }
        list_locations.getItems().addAll(marker_all);
    }



    /*
                GETTER and SETTER
     */
    public BorderPane getMap() {
        return mapBasicPane;
    }

}
