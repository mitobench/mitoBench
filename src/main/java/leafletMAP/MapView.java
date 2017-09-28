package leafletMAP;

/**
 * Created by neukamm on 01.07.17.
 */

import com.sun.javafx.charts.Legend;
import controller.GroupController;
import controller.TableControllerUserBench;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import net.java.html.boot.fx.FXBrowsers;
import net.java.html.leaflet.*;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
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
    private final TableColumn location_col;
    private final ObservableList items;
    private final TableColumn id_col;
    private final TableColumn grouping_col;
    private final BorderPane mapBasicPane;
    private Map map;
    private GroupController groupController;

    public MapView(GroupController groupController, TableControllerUserBench tc, TableColumn location_col, ObservableList items,
                   TableColumn id_col, TableColumn grouping_col, BorderPane mapBasicPane)
            throws MalformedURLException, FileNotFoundException, URISyntaxException {

        this.tableController = tc;
        this.groupController = groupController;
        this.location_col = location_col;
        this.id_col = id_col;
        this.items = items;
        this.grouping_col = grouping_col;
        this.mapBasicPane = mapBasicPane;
        // we define a regular JavaFX WebView that DukeScript can use for rendering
        webView = new WebView();
        getChildren().add(webView);



        // a regular JavaFX ListView
        listView = new ListView<>();
        initMarker(listView);

        // FXBrowsers loads the associated page into the WebView and runs our code.
        FXBrowsers.load(webView, MapView.class.getResource("/index.html"), () -> {
            // Here we define that the map is rendered to a div with id="map"
            // in our index.html.
            // This can only be done after the page is loaded and the context is initialized.
            map = new Map("map");

            // from here we just use the Leaflet API to show some stuff on the map
            map.setView(new LatLng(47.628304, -5.198158), 3);
            map.addLayer(new TileLayer(
                    "http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", //"http://{s}.tile.opentopomap.org/{z}/{x}/{y}.png
                    new TileLayerOptions().setMaxZoom(18)
            ));

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



    private void addMarker(){

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

        Legend legend = markerIcons.getLegend();
        mapBasicPane.setBottom(legend);

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
