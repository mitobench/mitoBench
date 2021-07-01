package leafletMAP;

/**
 * Created by neukamm on 01.07.17.
 */

import com.sun.javafx.charts.Legend;
import controller.GroupController;
import controller.TableControllerUserBench;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import net.java.html.boot.fx.FXBrowsers;
import net.java.html.leaflet.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple example View of how to embed DukeScript in a JavaFX Application. In
 * this example we use leaflet4j https://github.com/jtulach/leaflet4j.
 *
 * @author antonepple
 */
public class MapView extends StackPane {

    private final WebView webView;
    private final TableControllerUserBench tableController;
    private final ListView listView;
    private final ObservableList items;
    private final TableColumn id_col;
    private final TableColumn grouping_col;
    private final SplitPane basicpane;
    private final BorderPane mapBasicPane;
    private final TableColumn latitude_col;
    private final TableColumn longitude_col;
    private final TableColumn labID_col;
    private Map map;
    private GroupController groupController;

    public MapView(GroupController groupController, TableControllerUserBench tc, TableColumn sampling_latitude_col,
                   TableColumn sampling_longitude_col, ObservableList items,
                   TableColumn id_col, TableColumn labID_col, TableColumn grouping_col, BorderPane mapBasicPane,
                   SplitPane basicpane) {

        this.tableController = tc;
        this.groupController = groupController;
        this.latitude_col = sampling_latitude_col;
        this.longitude_col = sampling_longitude_col;
        this.id_col = id_col;
        this.labID_col = labID_col;
        this.items = items;
        this.grouping_col = grouping_col;
        this.mapBasicPane = mapBasicPane;
        this.basicpane = basicpane;
        // we define a regular JavaFX WebView that DukeScript can use for rendering
        webView = new WebView();
        webView.setContextMenuEnabled(false);
        getChildren().add(webView);



        // a regular JavaFX ListView
        listView = new ListView<>();
        initMarker(listView);

        // FXBrowsers loads the associated page into the WebView and runs our code.
        FXBrowsers.load(webView, MapView.class.getResource("/leaflet-0.7.2/index.html"), () -> {
            // Here we define that the map is rendered to a div with id="map"
            // in our index.html.
            // This can only be done after the page is loaded and the context is initialized.
            map = new Map("map");

            // from here we just use the Leaflet API to show some stuff on the map
            map.setView(new LatLng(47.628304, -5.198158), 3);
            map.addLayer(new TileLayer(
                    "https://server.arcgisonline.com/ArcGIS/rest/services/Canvas/World_Light_Gray_Base/MapServer/tile/{z}/{y}/{x}.png", //"http://{s}.tile.opentopomap.org/{z}/{x}/{y}.png
                    new TileLayerOptions().setMaxZoom(18)
            ));

            // https://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}
            // https://server.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/{z}/{y}/{x}
            // https://server.arcgisonline.com/ArcGIS/rest/services/Canvas/World_Light_Gray_Base/MapServer/tile/{z}/{y}/{x}
            addMarker();


        });
    }



    /**
     * This method initializes the markers. This means, a location is created for each sample (with location
     * information available).
     *
     * @param list_locations
     */
    private void initMarker(ListView<Location> list_locations) {
        List<Location> marker_all = new ArrayList<>();

        if(latitude_col != null && longitude_col != null){

            for (Object item : items) {
                String id = id_col.getCellObservableValue(item).getValue().toString();
                String labID = labID_col.getCellObservableValue(item).getValue().toString();
                String latitude  = latitude_col.getCellObservableValue(item).getValue().toString();
                String longitude  = longitude_col.getCellObservableValue(item).getValue().toString();
                String ancient_modern = tableController.getTableColumnByName("Modern/Ancient Data").getCellObservableValue(item).getValue().toString();
                String group = null;
                if(groupController.groupingExists()){
                    group = tableController.getTableColumnByName("Grouping").getCellObservableValue(item).getValue().toString();
                }

                if(!latitude.equals("") && !longitude.equals("") ){



                    // check if there's already a item at this location
//                    Location loc_double = locationExists(marker_all, latitude, longitude);
//                    if(loc_double != null){
//                        String name_double = loc_double.getName();
//                        String property_double = loc_double.getProperty();
//                        String group_double = loc_double.getGroup();
//
//                        marker_all.remove(loc_double);
//
//
//                    } else {
                        if(labID==null){
                            marker_all.add(new Location(id, Double.parseDouble(latitude), Double.parseDouble(longitude), ancient_modern, group));
                        } else {
                            if(group!=null){
                                marker_all.add(new Location(id + "_" + labID + "_" + group, Double.parseDouble(latitude), Double.parseDouble(longitude), ancient_modern, group));
                            } else {
                                marker_all.add(new Location(id + "_" + labID, Double.parseDouble(latitude), Double.parseDouble(longitude), ancient_modern, group));
                            }
                        }
                    //}
                }
            }
        }
        list_locations.getItems().addAll(marker_all);
    }

    /**
     * Method iterates over already added locations and tests if certains locations
     * have already been added.
     *
     * @return location that exists already, null else
     * @param marker_all list of all already added locations
     * @param latitude of new location
     * @param longitude of new location
     */


    private Location locationExists(List<Location> marker_all, String latitude, String longitude) {
        for (Location loc : marker_all){
            if ((loc.getLat() == Double.parseDouble(latitude))
                    && (loc.getLng() == Double.parseDouble(longitude))){

                return loc;
            }
        }
        return null;
    }


    private void addMarker(){

        if(listView.getItems().size() > 0){
            MarkerIcons markerIcons = new MarkerIcons(groupController, tableController);
            markerIcons.setItems(listView.getItems());

            if(grouping_col != null){
                // get groups
                markerIcons.setGroups(List.copyOf(groupController.getGroupnames()));
//                List<String> columnData = new ArrayList<>();
//                for( Object item : items) {
//                    columnData.add(grouping_col.getCellObservableValue(item).getValue().toString());
//                }
//                markerIcons.setGroups(columnData);
            }

            markerIcons.addIconsToMap(map);

            Legend legend = markerIcons.getLegend();
            //mapBasicPane.setBottom(new ScrollPane(legend));
            basicpane.getItems().add(new ScrollPane(legend));
        } else {
            System.out.println("No items to show on geogr. map");
        }

    }



    public Map getMap() {
        return map;
    }

    public WebView getWebView() {
        return webView;
    }

    public ListView getListView() {
        return listView;
    }
}
