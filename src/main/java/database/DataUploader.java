package database;

import controller.TableControllerUserBench;
import io.writer.GenericWriter;

import java.io.IOException;

public class DataUploader {
    private TableControllerUserBench tablecontroller;

    public DataUploader(TableControllerUserBench tablecontroller) {

        this.tablecontroller = tablecontroller;
        // write data
        GenericWriter genericWriter = new GenericWriter(tablecontroller.getSelectedRows(), ",", true);
        try {
            genericWriter.writeData("data_to_upload.csv", tablecontroller);

        } catch (IOException e) {
            System.err.println("'data_to_upload.csv' could not be created.\n" + e);
        }
    }

    public void upload() {



    }
}
