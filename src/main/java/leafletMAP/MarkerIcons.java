package leafletMAP;

import com.sun.javafx.charts.Legend;
import controller.GroupController;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.Group;
import net.java.html.leaflet.*;
import controller.TableControllerUserBench;

import java.net.URL;
import java.util.ArrayList;
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
    // TODO: using awesome-markers ? (https://github.com/lvoogdt/Leaflet.awesome-markers)
    // TODO: or create own markers (just dots in different colors)
    private String iconColGreen = getClass().getResource("/leaflet-0.7.2/images/marker-icon-green.png").toExternalForm();
    private String iconColBlue = getClass().getResource("/leaflet-0.7.2/images/marker-icon-blue.png").toExternalForm();
    private String iconColRed = getClass().getResource("/leaflet-0.7.2/images/marker-icon-red.png").toExternalForm();;
    private String iconColOrange = getClass().getResource("/leaflet-0.7.2/images/marker-icon-orange.png").toExternalForm();
    private String iconColYellow = getClass().getResource("/leaflet-0.7.2/images/marker-icon-yellow.png").toExternalForm();
    private String iconColViolet = getClass().getResource("/leaflet-0.7.2/images/marker-icon-violet.png").toExternalForm();
    private String iconColGrey = getClass().getResource("/leaflet-0.7.2/images/marker-icon-grey.png").toExternalForm();
    private String iconColBlack = getClass().getResource("/leaflet-0.7.2/images/marker-icon-black.png").toExternalForm();
    private Color[] colorsString = new Color[]{Color.GREEN, Color.BLUE, Color.RED, Color.ORANGE, Color.YELLOW, Color.VIOLET,
    Color.GREY, Color.BLACK};
    private double radius = 2.0;


    public MarkerIcons(GroupController gC, TableControllerUserBench tb){

        groupController = gC;
        tableController = tb;

    }

    /**
     * This method links all icons to the map.
     * @param map
     */
    public void addIconsToMap(Map map) {

        //if(groups==null){
            addMarkerOneColor(
                    items,
                    map
            );
//        } else {
//            addMarkerMultiColor(map);
//        }

    }

    /**
     * This method generates marker/icon (single color = blue) if no grouping is defined.
     *
     * @param items
     * @param map
     */
    private void addMarkerOneColor(ObservableList items, Map map) {

        for(Object location : items){
            Location loc = (Location) location;
            LatLng pos = new LatLng(loc.getLat(), loc.getLng());

            // ancient marker
            MarkerOptions markerOptions_ancient = new MarkerOptions().setTitle("Ancient");
            URL pathToIcon_ancient = this.getClass().getResource("/icons/skull_filled.png");
            IconOptions iconOptions_ancient = new IconOptions(pathToIcon_ancient.toExternalForm());
            iconOptions_ancient.setIconSize(new Point(30,30));
            Icon icon_ancient = new Icon(iconOptions_ancient);


            // modern marker
            PathOptions pathOpt_modern = new PathOptions().setColor("BLUE");
            CircleMarker m_circle_modern = new CircleMarker(pos,  pathOpt_modern);

            if (((Location) location).getProperty().equals("ancient")){
                Popup popup = new Popup();
                popup.setContent(location.toString());

                Marker marker_ancient = new Marker(pos, markerOptions_ancient);
                marker_ancient.setIcon(icon_ancient);
                marker_ancient.bindPopup(popup);
                marker_ancient.addTo(map);

            } else{
                Popup popup = new Popup();
                popup.setContent(location.toString());
                m_circle_modern.bindPopup(popup);
                m_circle_modern.addTo(map);

            }
        }
    }


    /**
     * This method generates marker/icon in different colors for the elements in the different groups.
     * @param map
     */
    private void addMarkerMultiColor(Map map) {


        String[] colors_2 = new String[]{"GREEN", "BLUE", "RED", "ORANGE", "YELLOW", "VIOLET", "GREY", "BLACK"};
        TableColumn id_col = tableController.getTableColumnByName("ID");
        List<String> columnData = new ArrayList<>();
        for (Object item : tableController.getTable().getItems()) {
            ObservableList item_list = (ObservableList) item;
            columnData.add((String) id_col.getCellObservableValue(item_list).getValue());
        }


        int groupCount = 0;

        HashMap<String, Group> all_groups = groupController.getGroupsWithout();
        // iterate over groups and generates marker/icon for each element in the group.
        for(String gName : all_groups.keySet()){
            Group g = all_groups.get(gName);

            for(Object gMember : g.getEntries()){

                ObservableList entry = (ObservableList) gMember;

                String location = (String) entry.get(tableController.getColIndex("Location"));
                String id = (String) entry.get(tableController.getColIndex("ID"));
                if(!location.equals("") && columnData.contains(id)){
                    String[] loc = location.split(";");
                    LatLng pos = new LatLng(Double.parseDouble(loc[0]), Double.parseDouble(loc[1]));

                    PathOptions pathOpt = new PathOptions().setColor(colors_2[groupCount]);
                    CircleMarker m = new CircleMarker(pos,  pathOpt);
                    Popup popup = new Popup();
                    popup.setContent(entry.get(0).toString());
                    m.bindPopup(popup);
                    m.addTo(map);

                }
            }
            groupCount++;

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
        legend.setVertical(false);

        int colorCount = 0;
        for (String groupName : groupController.getGroupnames()) {
            if(!groupName.equals("")){
                Rectangle rect = new Rectangle(10,10,5, 5);
                rect.setFill(colorsString[colorCount]);
                rect.setStroke(colorsString[colorCount]);

                Circle circ = new Circle(10,10,5, colorsString[colorCount]);
                circ.setFill(colorsString[colorCount]);
                circ.setStroke(colorsString[colorCount]);

                Legend.LegendItem legendItem = new Legend.LegendItem(groupName, circ);
                legend.getItems().add(legendItem);
                colorCount++;
            }
        }

        return legend;

    }





}
