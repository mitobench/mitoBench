package io.datastructure.location;

import io.IData;

/**
 * Created by neukamm on 23.03.17.
 */
public class LocationData implements IData {
    private double longitude;
    private double latitude;

    //Type Handlers
    public static final int PARSE_LOCATION_INFORMATION = 1;


    public LocationData(String toParse, int config) {
        setParseLocationInformation(toParse);

    }

    private void setParseLocationInformation(String toParseThis){
        if(!toParseThis.equals("")){
            String[] location = toParseThis.split(";");
            latitude = Double.parseDouble(location[0]);
            longitude = Double.parseDouble(location[1]);
        }
    }


    @Override
    public String getTableInformation() {
        if (latitude == 0.0 && longitude==0.0)
            return "";
        else
            return (latitude + ";" + longitude);
    }
}
