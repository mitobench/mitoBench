package io.inputtypes;

import io.IInputType;

/**
 * Created by peltzer on 07/12/2016.
 */
public class RadioCarbonInputType implements IInputType {
    private String type = "C14";

    public RadioCarbonInputType(String data) {
        this.type = data;
    }

    @Override
    public String getTypeInformation() {
        return this.type;
    }
}
