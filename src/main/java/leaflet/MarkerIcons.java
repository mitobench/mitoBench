package leaflet;

import com.sun.org.apache.xpath.internal.SourceTree;
import controller.GroupController;
import controller.LeafletController;
import javafx.collections.ObservableList;
import model.Group;
import net.java.html.leaflet.*;
import view.table.controller.TableControllerUserBench;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by neukamm on 03.07.17.
 */
public class MarkerIcons {
    private final TableControllerUserBench tableController;
    private ObservableList items;
    private List<String> groups = null;
    private GroupController groupController;
    private String iconColGreen = "https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-green.png";
    private String iconColBlue = "https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-blue.png";
    private String iconColRed = "https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-red.png";
    private String iconColOrange = "https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-orange.png";
    private String iconColYellow = "https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-yellow.png";
    private String iconColViolet = "https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-violet.png";
    private String iconColGrey = "https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-grey.png";
    private String iconColBlack = "https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-black.png";


    public MarkerIcons(GroupController gC, TableControllerUserBench tb){

        groupController = gC;
        tableController = tb;

    }

    public void setItems(ObservableList items) {
        this.items = items;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

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

    private void addMarkerOneColor(String color, ObservableList items, Map map) {
        for(Object add : items){
            LeafletController.Address loc = (LeafletController.Address) add;
            LatLng pos = new LatLng(loc.getLat(), loc.getLng());

            Icon icon = new Icon(new IconOptions(color)
                    .setShadowUrl("https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png")
            );
            Marker m = new Marker(pos, new MarkerOptions().setIcon(icon));
            m.addTo(map);
        }
    }

    private void addMarkerMultiColor(Map map) {


        String[] colors = new String[]{iconColGreen, iconColBlue, iconColRed, iconColOrange, iconColYellow,
                iconColViolet, iconColGrey, iconColBlack};

        // get unique group names
        int groupCount = 0;
        HashMap<String, Group> all_groups = groupController.getAllGroups();
        for(String gName : all_groups.keySet()){
            Group g = all_groups.get(gName);

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
                    m.addTo(map);
                    System.out.println("Add marker with color: " + colors[groupCount]);
                }
            }
            groupCount++;


        }

    }

}
