package leafletMAP;

import com.sun.javafx.charts.Legend;
import controller.GroupController;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.Group;
import net.java.html.leaflet.*;
import controller.TableControllerUserBench;
import net.java.html.leaflet.Map;

import java.net.URL;
import java.util.*;

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
//    private String iconColGreen = getClass().getResource("/leaflet-0.7.2/images/marker-icon-green.png").toExternalForm();
//    private String iconColBlue = getClass().getResource("/leaflet-0.7.2/images/marker-icon-blue.png").toExternalForm();
//    private String iconColRed = getClass().getResource("/leaflet-0.7.2/images/marker-icon-red.png").toExternalForm();;
//    private String iconColOrange = getClass().getResource("/leaflet-0.7.2/images/marker-icon-orange.png").toExternalForm();
//    private String iconColYellow = getClass().getResource("/leaflet-0.7.2/images/marker-icon-yellow.png").toExternalForm();
//    private String iconColViolet = getClass().getResource("/leaflet-0.7.2/images/marker-icon-violet.png").toExternalForm();
//    private String iconColGrey = getClass().getResource("/leaflet-0.7.2/images/marker-icon-grey.png").toExternalForm();
//    private String iconColBlack = getClass().getResource("/leaflet-0.7.2/images/marker-icon-black.png").toExternalForm();
//    private Color[] colorsString = new Color[]{Color.GREEN, Color.BLUE, Color.RED, Color.ORANGE, Color.YELLOW, Color.VIOLET,
//    Color.GREY, Color.BLACK};
    private double radius = 2.0;
    private ArrayList<Color> random_colours;


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
                    items,
                    map
            );
        } else {
            addMarkerMultiColor(items, map);
        }

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
//            MarkerOptions markerOptions_ancient = new MarkerOptions().setTitle("Ancient");
//            URL pathToIcon_ancient = this.getClass().getResource("/icons/raute.png");
//            IconOptions iconOptions_ancient = new IconOptions(pathToIcon_ancient.toExternalForm());
//            iconOptions_ancient.setIconSize(new Point(15,15));
//            Icon icon_ancient = new Icon(iconOptions_ancient);


            // modern marker
            PathOptions pathOpt_modern = new PathOptions().setColor("BLUE");
            CircleMarker m_circle_modern = new CircleMarker(pos,  pathOpt_modern);

//            if (((Location) location).getProperty().equals("ancient")){
//                Popup popup = new Popup();
//                popup.setContent(location.toString());
//                Marker marker_ancient = new Marker(pos, markerOptions_ancient);
//                marker_ancient.setIcon(icon_ancient);
//                marker_ancient.bindPopup(popup);
//                marker_ancient.addTo(map);
//
//            } else{
                Popup popup = new Popup();
                popup.setContent(location.toString());
                m_circle_modern.bindPopup(popup);
                m_circle_modern.addTo(map);

          //  }
        }
    }


    /**
     * This method generates marker/icon in different colors for the elements in the different groups.
     * @param items
     * @param map
     */
    private void addMarkerMultiColor(ObservableList items, Map map) {
        //List<String> group_names = List.copyOf(groupController.getGroupnames());
        // define random colors for each group
        random_colours = new ArrayList<>();
        for (String group : groups){
            if(group.equals(""))
                random_colours.add(Color.BLACK);
            else
                random_colours.add(getRandomColor());
        }

        for(Object location : items){
            Location loc = (Location) location;

            LatLng pos = new LatLng(loc.getLat(), loc.getLng());
            String group = loc.getGroup();

//            // ancient marker
//            MarkerOptions markerOptions_ancient = new MarkerOptions().setTitle("Ancient");
//            URL pathToIcon_ancient = this.getClass().getResource("/icons/raute.png");
//            IconOptions iconOptions_ancient = new IconOptions(pathToIcon_ancient.toExternalForm());
//            iconOptions_ancient.setIconSize(new Point(15,15));
//            Icon icon_ancient = new Icon(iconOptions_ancient);


            // modern marker

            String r_color = "#" + random_colours.get(groups.indexOf(group)).toString().substring(2).toUpperCase();
            PathOptions pathOpt_modern = new PathOptions().setColor(r_color);
            CircleMarker m_circle_modern = new CircleMarker(pos,  pathOpt_modern);

//            if (((Location) location).getProperty().equals("ancient")){
//                Popup popup = new Popup();
//                popup.setContent(location.toString());
//                Marker marker_ancient = new Marker(pos, markerOptions_ancient);
//                marker_ancient.setIcon(icon_ancient);
//                marker_ancient.bindPopup(popup);
//                marker_ancient.addTo(map);
//
//            } else{
                Popup popup = new Popup();
                popup.setContent(location.toString());
                m_circle_modern.bindPopup(popup);
                m_circle_modern.addTo(map);

           // }
        }

//        String[] colors_2 = new String[]{"GREEN", "BLUE", "RED", "ORANGE", "YELLOW", "VIOLET", "GREY", "BLACK"};
//        TableColumn id_col = tableController.getTableColumnByName("ID");
//        List<String> columnData = new ArrayList<>();
//        for (Object item : tableController.getTable().getItems()) {
//            ObservableList item_list = (ObservableList) item;
//            columnData.add((String) id_col.getCellObservableValue(item_list).getValue());
//        }
//
//        int groupCount = 0;
//
//        HashMap<String, Group> all_groups = groupController.getGroupsWithout();
//
//        // iterate over groups and generates marker/icon for each element in the group.
//        for(String gName : all_groups.keySet()){
//            Group g = all_groups.get(gName);
//
//            for(Object gMember : g.getEntries()){
//
//                ObservableList entry = (ObservableList) gMember;
//
//                String location = (String) entry.get(tableController.getColIndex("Location"));
//                String id = (String) entry.get(tableController.getColIndex("ID"));
//                if(!location.equals("") && columnData.contains(id)){
//                    String[] loc = location.split(";");
//                    LatLng pos = new LatLng(Double.parseDouble(loc[0]), Double.parseDouble(loc[1]));
//
//                    // generate random color
//                    Color random_color = getRandomColor();
//                    //PathOptions pathOpt = new PathOptions().setColor(colors_2[groupCount]);
//                    PathOptions pathOpt = new PathOptions().setColor(random_color.toString());
//                    CircleMarker m = new CircleMarker(pos,  pathOpt);
//                    Popup popup = new Popup();
//                    popup.setContent(entry.get(0).toString());
//                    m.bindPopup(popup);
//                    m.addTo(map);
//
//                }
//            }
//            groupCount++;
//
//        }
    }

    private Color getRandomColor() {
        return convertColor(Color.color(Math.random(), Math.random(), Math.random()));
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
        List<String> groupnames = new ArrayList<>(groupController.getGroupnames());
        java.util.Collections.sort(groupnames);

        for (String groupName : groupnames) {
            if(!groupName.equals("")){
//                Rectangle rect = new Rectangle(10,10,5, 5);
//                rect.setFill(random_colours.get(groups.indexOf(groupName)));
//                rect.setStroke(random_colours.get(groups.indexOf(groupName)));

                Circle circ = new Circle(10,10,5, random_colours.get(groups.indexOf(groupName)));
                circ.setFill(random_colours.get(groups.indexOf(groupName)));
                circ.setStroke(random_colours.get(groups.indexOf(groupName)));

                Legend.LegendItem legendItem = new Legend.LegendItem(groupName, circ);
                legend.getItems().add(legendItem);
            } else {
                Circle circ = new Circle(10,10,5, Color.BLACK);
                circ.setFill(Color.BLACK);
                circ.setStroke(Color.BLACK);

                Legend.LegendItem legendItem = new Legend.LegendItem("No group", circ);
                legend.getItems().add(legendItem);
            }
        }

        return legend;

    }

    private Color convertColor(Object o) {
        javafx.scene.paint.Color fx = (Color) o;

        return new Color((float) fx.getRed(),
                (float) fx.getGreen(),
                (float) fx.getBlue(),
                (float) fx.getOpacity());
    }


}
