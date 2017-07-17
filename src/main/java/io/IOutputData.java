package io;

import view.table.controller.TableControllerUserBench;

import java.io.IOException;

/**
 * Created by peltzer on 22/11/2016.
 */
public interface IOutputData {
    void writeData(String file, TableControllerUserBench tableController) throws IOException;
    void setGroups(String groupID);

}
