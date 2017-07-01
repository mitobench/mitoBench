package leaflet;

/**
 * Created by neukamm on 01.07.17.
 */

import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import net.java.html.boot.fx.FXBrowsers;
import net.java.html.leaflet.Circle;
import net.java.html.leaflet.LatLng;
import net.java.html.leaflet.Map;
import net.java.html.leaflet.PathOptions;
import net.java.html.leaflet.Polygon;
import net.java.html.leaflet.TileLayer;
import net.java.html.leaflet.TileLayerOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * A simple example View of how to embed DukeScript in a JavaFX Application. In
 * this example we use leaflet4j https://github.com/jtulach/leaflet4j.
 *
 * @author antonepple
 */
public class MapView extends StackPane {

    private final WebView webView;
    private Map map;

    public MapView() throws MalformedURLException, FileNotFoundException, URISyntaxException {
        // we define a regular JavaFX WebView that DukeScript can use for rendering
        webView = new WebView();
        getChildren().add(webView);

        // FXBrowsers loads the associated page into the WebView and runs our
        // code.

        FXBrowsers.load(webView, MapView.class.getResource("/index.html"), new Runnable() {

            @Override
            public void run() {
                // Here we define that the map is rendered to a div with id="map"
                // in our index.html.
                // This can only be done after the page is loaded and the context is initialized.
                map = new Map("map");

                // from here we just use the Leaflet API to show some stuff on the map
                map.setView(new LatLng(51.505, -0.09), 13);
                map.addLayer(new TileLayer("http://{s}.tile.thunderforest.com/cycle/{z}/{x}/{y}.png",
                        new TileLayerOptions()
                                .setAttribution(
                                        "Map data &copy; <a href='http://www.thunderforest.com/opencyclemap/'>OpenCycleMap</a> contributors, "
                                                + "<a href='http://creativecommons.org/licenses/by-sa/2.0/'>CC-BY-SA</a>, "
                                                + "Imagery © <a href='http://www.thunderforest.com/'>Thunderforest</a>")
                                .setMaxZoom(18)
                                .setId("eppleton.ia9c2p12")
                ));

                // sample code showing how to use the Java API
                map.addLayer(new Circle(new LatLng(51.508, -0.11), 500,
                        new PathOptions().setColor("red").setFillColor("#f03").setOpacity(0.5)
                ).bindPopup("I am a Circle"));
                map.addLayer(new Polygon(new LatLng[] {
                        new LatLng(51.509, -0.08),
                        new LatLng(51.503, -0.06),
                        new LatLng(51.51, -0.047)
                }).bindPopup("I am a Polygon"));

            }
        });
    }

    public Map getMap() {
        return map;
    }

    public WebView getWebView() {
        return webView;
    }
}
