package controller;

import Logging.LogClass;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import leafletMAP.Location;
import leafletMAP.MapView;
import net.java.html.boot.fx.FXBrowsers;
import net.java.html.leaflet.*;
import view.MitoBenchWindow;
import view.dialogues.settings.GeoMapDisplay;
import view.visualizations.GeographicalMapViz;


/**
 *
 *
 * This class controls all a actions of the map.
 *
 * Created by neukamm on 01.07.17.
 */
public class GeographicalMapController {

    private final TableColumn id_col;
    private final TableColumn sampling_latitude_col;
    private final ObservableList items;
    private final GroupController groupController;
    private final GeographicalMapViz geographicalMapViz;
    private final MitoBenchWindow mito;
    private LogClass logClass;
    private MapView map=null;
    private final TableColumn grouping_col;
    private final TableColumn sampling_longitude_col;
    private TableControllerUserBench tableControllerUserBench;


    public GeographicalMapController(MitoBenchWindow mitoBenchWindow,
                                     GroupController groupController,
                                     GeographicalMapViz geographicalMapViz) {

        tableControllerUserBench = mitoBenchWindow.getTableControllerUserBench();
        logClass = mitoBenchWindow.getLogClass();
        this.mito = mitoBenchWindow;

        grouping_col = tableControllerUserBench.getTableColumnByName("Grouping");
        id_col = tableControllerUserBench.getTableColumnByName("ID");
        sampling_latitude_col = tableControllerUserBench.getTableColumnByName("Sample Latitude");
        sampling_longitude_col = tableControllerUserBench.getTableColumnByName("Sample Longitude");
        items = tableControllerUserBench.getSelectedRows();
        this.groupController = groupController;
        this.geographicalMapViz = geographicalMapViz;

        createMapView(sampling_latitude_col, sampling_longitude_col);
        setOptionsTab();


    }

    public void createMapView(TableColumn latitude_col, TableColumn longitude_col){

        map = new MapView(
                groupController,
                tableControllerUserBench,
                latitude_col,
                longitude_col,
                items,
                id_col,
                tableControllerUserBench.getTableColumnByName("Labsample ID"),
                grouping_col,
                geographicalMapViz.getMapBasicPane()
        );

        geographicalMapViz.setCenter(map);



        // we listen for the selected item and update the map accordingly
        map.getListView().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        map.getListView().getSelectionModel().selectedItemProperty().addListener((ChangeListener<Location>)
                (ov, old_val, new_val) ->
                        FXBrowsers.runInBrowser(map.getWebView(), () -> {

                            LatLng pos = new LatLng(new_val.getLat(), new_val.getLng());
                            map.getMap().setView(pos, 5);
                            map.getMap().openPopup(new_val.toString(), pos);
                            map.getMap().latLngToLayerPoint(pos);

                        }));

        geographicalMapViz.setLeft(map.getListView());

    }

    public void setOptionsTab(){
        GeoMapDisplay geoMapDisplay = new GeoMapDisplay("Map Options", map.getListView().getItems().size(),logClass);

        // add functionality to buttons
        geoMapDisplay.getRb_location_sampling().selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                createMapView(
                        tableControllerUserBench.getTableColumnByName("Sampling Latitude"),
                        tableControllerUserBench.getTableColumnByName("Sampling Longitude")
                );
                geoMapDisplay.getTextField_sample_info().setText("# of samples: " + map.getListView().getItems().size());
            }
        });

        geoMapDisplay.getRb_location_sample().selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                createMapView(
                        tableControllerUserBench.getTableColumnByName("Sample Latitude"),
                        tableControllerUserBench.getTableColumnByName("Sample Longitude")
                );
                geoMapDisplay.getTextField_sample_info().setText("# of samples: " +  map.getListView().getItems().size());

            }
        });

        geoMapDisplay.getRb_location_TMA().selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                createMapView(
                        tableControllerUserBench.getTableColumnByName("TMA inferred Latitude"),
                        tableControllerUserBench.getTableColumnByName("TMA inferred Longitude")
                );
                geoMapDisplay.getTextField_sample_info().setText("# of samples: " +  map.getListView().getItems().size());

            }
        });

        mito.getTabpane_statistics().getTabs().add(geoMapDisplay.getTab());
        mito.getTabpane_statistics().getSelectionModel().select(geoMapDisplay.getTab());

    }



}
