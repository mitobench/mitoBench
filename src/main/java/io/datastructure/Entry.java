package io.datastructure;

/**
 * This class is intended to provide a generic view.data type resource of the form <ID + view.data>. Data can be whatever we want to have, we just need to define it once as String, integer, double.
 * Created by peltzer on 17/11/2016.
 */
public class Entry<T> {
    private String identifier;
    private String type;
    private T data;


    public Entry(String identifier, String type, T data) {
        this.identifier = identifier;
        this.type = type;
        this.data = data;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
