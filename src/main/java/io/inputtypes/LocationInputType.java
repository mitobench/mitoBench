package io.inputtypes;

import io.IInputType;

/**
 * Created by neukamm on 22.03.17.
 */
public class LocationInputType implements IInputType{
    private String type = "Location";

    public LocationInputType(String headerType) {
        type = headerType;
    }

    @Override
    public String getTypeInformation() {
        return this.type;
    }
}
