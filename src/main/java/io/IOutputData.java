package io;

import java.io.IOException;

/**
 * Created by peltzer on 22/11/2016.
 */
public interface IOutputData {
    void writeData(String file) throws IOException;
    void setGroups(String groupID);

}
