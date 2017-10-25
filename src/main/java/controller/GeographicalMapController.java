package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import leafletMAP.Location;
import leafletMAP.MapView;
import net.java.html.boot.fx.FXBrowsers;
import net.java.html.leaflet.*;
import view.MitoBenchWindow;
import view.visualizations.GeographicalMapViz;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;

/**
 *
 *
 * This class controls all a actions of the map.
 *
 * Created by neukamm on 01.07.17.
 */
public class GeographicalMapController {

    private final TableColumn id_col;
    private final TableColumn location_col;
    private final ObservableList items;
    private final MapView map;
    private final TableColumn grouping_col;
    private TableControllerUserBench tableControllerUserBench;


    public GeographicalMapController(MitoBenchWindow mitoBenchWindow,
                                     GroupController groupController,
                                     GeographicalMapViz geographicalMapViz)
            throws FileNotFoundException, URISyntaxException, MalformedURLException {

        tableControllerUserBench = mitoBenchWindow.getTableControllerUserBench();

        grouping_col = tableControllerUserBench.getTableColumnByName("Grouping");
        id_col = tableControllerUserBench.getTableColumnByName("ID");
        location_col = tableControllerUserBench.getTableColumnByName("Location");
        items = tableControllerUserBench.getSelectedRows();



        map = new MapView(groupController, tableControllerUserBench, location_col, items, id_col, grouping_col, geographicalMapViz.getMapBasicPane());
        geographicalMapViz.setCenter(map);



        // we listen for the selected item and update the map accordingly
        map.getListView().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        map.getListView().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Location>() {
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

        geographicalMapViz.setLeft(map.getListView());

    }



}
