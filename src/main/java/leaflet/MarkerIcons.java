package leaflet;

import com.sun.javafx.charts.Legend;
import controller.GroupController;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Group;
import net.java.html.leaflet.*;
import view.table.controller.TableControllerUserBench;

import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 03.07.17.
 */
public class MarkerIcons {
    private final TableControllerUserBench tableController;
    private ObservableList items;
    private List<String> groups = null;
    private GroupController groupController;

    // definition of all colours that can be used to color the marker icons
    // (https://github.com/pointhi/leaflet-color-markers)
    // todo: using awesome-markers ? (https://github.com/lvoogdt/Leaflet.awesome-markers)
    private String iconColGreen = "https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-green.png";
    private String iconColBlue = "https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-blue.png";
    private String iconColRed = "https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-red.png";
    private String iconColOrange = "https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-orange.png";
    private String iconColYellow = "https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-yellow.png";
    private String iconColViolet = "https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-violet.png";
    private String iconColGrey = "https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-grey.png";
    private String iconColBlack = "https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-black.png";
    private Color[] colorsString = new Color[]{Color.GREEN, Color.BLUE, Color.RED, Color.ORANGE, Color.YELLOW, Color.VIOLET,
    Color.GREY, Color.BLACK};


    public MarkerIcons(GroupController gC, TableControllerUserBench tb){

        groupController = gC;
        tableController = tb;

    }

    /**
     * This method links all icons to the map.
     * @param map
     */
    public void addIconsToMap(Map map) {

        if(groups==null){
            addMarkerOneColor(
                    "https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-blue.png",
                    items,
                    map
            );
        } else {
            addMarkerMultiColor(map);
        }

    }

    /**
     * This method generates marker/icon (single color = blue) if no grouping is defined.
     * @param color
     * @param items
     * @param map
     */
    private void addMarkerOneColor(String color, ObservableList items, Map map) {
        for(Object location : items){
            Location loc = (Location) location;
            LatLng pos = new LatLng(loc.getLat(), loc.getLng());


            Icon icon = new Icon(new IconOptions(color)
                    .setShadowUrl("https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png")
            );

            Marker m = new Marker(pos, new MarkerOptions().setIcon(icon));
            Popup popup = new Popup();
            popup.setContent(location.toString());
            m.bindPopup(popup);
            m.addTo(map);
        }
    }


    /**
     * This method generates marker/icon in different colors for the elements in the different groups.
     * @param map
     */
    private void addMarkerMultiColor(Map map) {

        String[] colors = new String[]{iconColGreen, iconColBlue, iconColRed, iconColOrange, iconColYellow,
                iconColViolet, iconColGrey, iconColBlack};

        int groupCount = 0;
        HashMap<String, Group> all_groups = groupController.getAllGroups();
        // iterate over groups and generates marker/icon for each element in the group.
        for(String gName : all_groups.keySet()){
            Group g = all_groups.get(gName);
            if(!g.equals("Undefined")){
                for(Object gMember : g.getEntries()){

                    ObservableList entry = (ObservableList) gMember;

                    String location = (String) entry.get(tableController.getColIndex("Location"));
                    if(!location.equals("Undefined")){
                        String[] loc = location.split(",");
                        LatLng pos = new LatLng(Double.parseDouble(loc[0]), Double.parseDouble(loc[1]));
                        Icon icon = new Icon(new IconOptions(colors[groupCount])
                                .setShadowUrl("https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png")
                        );
                        Marker m = new Marker(pos, new MarkerOptions().setIcon(icon));
                        Popup popup = new Popup();
                        popup.setContent(entry.get(0).toString());
                        m.bindPopup(popup);
                        m.addTo(map);
                    }
                }
                groupCount++;
            }


        }
    }


    /*
                    SETTER and GETTER
     */

    public void setItems(ObservableList items) {
        this.items = items;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }


    /**
     * This method generates a legend (to define which color is which group) based on the grouping.
     * @return Legend
     */

    public Legend getLegend() {

        Legend legend = new Legend();
        int colorCount = 0;
        for (String groupName : groupController.getGroupnames()) {
            if(!groupName.equals("Undefined")){
                Rectangle rect = new Rectangle(20,20,10, 10);
                rect.setFill(colorsString[colorCount]);
                rect.setStroke(colorsString[colorCount]);

                Legend.LegendItem legendItem = new Legend.LegendItem(groupName, rect);
                legend.getItems().add(legendItem);
                colorCount++;
            }
        }

        return legend;

    }





}
