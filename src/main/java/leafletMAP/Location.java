package leafletMAP;

/**
 *
 * This class defines a location by latitude and longitude and a name.
 * The name is often the id of the sample entry
 *
 * Created by neukamm on 7/4/17.
 */
public class Location {

    private final String name;
    private final double lat;
    private final double lng;
    private final String property;
    private final String group;

    public Location(String name, double lat, double lng, String property, String group) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.property = property;
        this.group = group;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getProperty() {
        return property;
    }

    public String getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }



}
