package io;

import controller.TableControllerUserBench;

import java.io.IOException;

/**
 * Created by peltzer on 22/11/2016.
 */
public interface IOutputData {
    void writeData(String file, TableControllerUserBench tableController) throws Exception;
    void setGroups(String groupID);

}
