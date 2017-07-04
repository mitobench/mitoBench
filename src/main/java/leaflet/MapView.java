package leaflet;

/**
 * Created by neukamm on 01.07.17.
 */

import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import net.java.html.boot.fx.FXBrowsers;
import net.java.html.leaflet.*;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

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

        // FXBrowsers loads the associated page into the WebView and runs our code.

        FXBrowsers.load(webView, MapView.class.getResource("/index.html"), () -> {
            // Here we define that the map is rendered to a div with id="map"
            // in our index.html.
            // This can only be done after the page is loaded and the context is initialized.
            map = new Map("map");

            // from here we just use the Leaflet API to show some stuff on the map
            map.setView(new LatLng(47.628304, -5.198158), 3);
            map.addLayer(new TileLayer(
                    "https://{s}.tile.thunderforest.com/landscape/{z}/{x}/{y}.png",
                    new TileLayerOptions().setMaxZoom(18)
            ));
            

        });
    }

    public Map getMap() {
        return map;
    }

    public WebView getWebView() {
        return webView;
    }


}
