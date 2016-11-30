package io;

import java.io.IOException;

/**
 * Created by peltzer on 22/11/2016.
 */
public interface IOutputData {
    public void writeData(String file) throws IOException;
    public void setGroups(String groupID);

}
