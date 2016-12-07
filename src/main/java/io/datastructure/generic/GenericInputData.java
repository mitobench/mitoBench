package io.datastructure.generic;

import view.table.datatypes.IData;

/**
 * Created by neukamm on 07.12.16.
 */
public class GenericInputData implements IData{

    private String data;

    public GenericInputData(String input){
        data = input;

    }

    @Override
    public String getTableInformation() {
        return data;
    }
}
