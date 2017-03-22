package io.inputtypes;

import io.IInputType;

/**
 * Created by peltzer on 07/12/2016.
 */
public class CategoricInputType implements IInputType {
    private String type = "Categorical";

    public CategoricInputType(String data) {
        this.type = data;
    }


    @Override
    public String getTypeInformation() { return type; }
}
