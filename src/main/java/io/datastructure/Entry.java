package io.datastructure;

import io.IInputType;
import view.datatypes.IData;

/**
 * This class is intended to provide a generic view.data type resource of the form <ID + view.data>. Data can be whatever we want to have, we just need to define it once as String, integer, double.
 * Created by peltzer on 17/11/2016.
 */
public class Entry {
    private String identifier;
    private IInputType type;
    private IData data;


    public Entry(String identifier, IInputType type, IData data) {
        this.identifier = identifier;
        this.type = type;
        this.data = data;
    }

    public IInputType getType() {
        return type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public IData getData() {
        return data;
    }

}
