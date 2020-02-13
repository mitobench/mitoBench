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

    public Location(String name, double lat, double lng, String property) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.property = property;
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

    @Override
    public String toString() {
        return name;
    }



}
