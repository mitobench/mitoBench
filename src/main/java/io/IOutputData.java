package io;

import controller.ATableController;
import controller.TableControllerUserBench;

import java.io.IOException;

/**
 * Created by peltzer on 22/11/2016.
 */
public interface IOutputData {
    void writeData(String file, ATableController tableController) throws Exception;
    void setGroups(String groupID);

}
