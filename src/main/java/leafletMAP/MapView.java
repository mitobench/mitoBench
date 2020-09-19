package leafletMAP;

/**
 * Created by neukamm on 01.07.17.
 */

import controller.GroupController;
import controller.TableControllerUserBench;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
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
    private final BorderPane mapBasicPane;
    private final TableColumn latitude_col;
    private final TableColumn longitude_col;
    private final TableColumn labID_col;
    private Map map;
    private GroupController groupController;

    public MapView(GroupController groupController, TableControllerUserBench tc, TableColumn sampling_latitude_col,
                   TableColumn sampling_longitude_col, ObservableList items,
                   TableColumn id_col, TableColumn labID_col, TableColumn grouping_col, BorderPane mapBasicPane) {

        this.tableController = tc;
        this.groupController = groupController;
        this.latitude_col = sampling_latitude_col;
        this.longitude_col = sampling_longitude_col;
        this.id_col = id_col;
        this.labID_col = labID_col;
        this.items = items;
        this.grouping_col = grouping_col;
        this.mapBasicPane = mapBasicPane;
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

                if(!latitude.equals("") && !longitude.equals("") ){
                    if(labID==null){
                        marker_all.add(new Location(id, Double.parseDouble(latitude), Double.parseDouble(longitude), ancient_modern));
                    } else {
                        marker_all.add(new Location(id + "_" + labID, Double.parseDouble(latitude), Double.parseDouble(longitude), ancient_modern));
                    }
                }
            }
        }
        list_locations.getItems().addAll(marker_all);
    }



    private void addMarker(){

        if(listView.getItems().size() > 0){
            MarkerIcons markerIcons = new MarkerIcons(groupController, tableController);
            markerIcons.setItems(listView.getItems());

            if(grouping_col != null){
                // get groups
                List<String> columnData = new ArrayList<>();
                for( Object item : items) {
                    columnData.add(grouping_col.getCellObservableValue(item).getValue().toString());
                }
                markerIcons.setGroups(columnData);
            }

            markerIcons.addIconsToMap(map);

           // Legend legend = markerIcons.getLegend();
            //mapBasicPane.setBottom(legend);
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
