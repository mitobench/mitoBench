package leaflet;

/**
 * Created by neukamm on 01.07.17.
 */

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import net.java.html.boot.fx.FXBrowsers;
import net.java.html.leaflet.LatLng;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        final MapView map = new MapView();

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(map);

        // a regular JavaFX ListView
        ListView<Address> listView = new ListView<>();
        listView.getItems().addAll(new Address("Toni", 48.1322840, 11.5361690),
                new Address("Jarda", 50.0284060, 14.4934400),
                new Address("JUG Münster", 51.94906770000001, 7.613701100000071));
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
        Scene scene = new Scene(borderPane);

        stage.setTitle("JavaFX and DukeScript");
        stage.setScene(scene);
        stage.show();
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

}

