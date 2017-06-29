package io.datastructure.location;

import view.datatypes.IData;

/**
 * Created by neukamm on 23.03.17.
 */
public class LocationData implements IData {
    private double longitude;
    private double latitude;

    //Type Handlers
    public static final int DEFAULT = 0;
    public static final int PARSE_LOCATION_INFORMATION = 1;

    public LocationData(String toParse) {
        String[] location = toParse.split(",");
        latitude = Double.parseDouble(location[0]);
        longitude = Double.parseDouble(location[1]);

    }

    public LocationData(String toParse, int config) {
        setParseLocationInformation(toParse);

    }

    private void setParseLocationInformation(String toParseThis){
        String[] location = toParseThis.split(",");
        latitude = Double.parseDouble(location[0]);
        longitude = Double.parseDouble(location[1]);
    }


    @Override
    public String getTableInformation() {
        return (latitude + "," + longitude);
    }
}