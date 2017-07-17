package io.writer;

import io.IOutputData;
import view.table.MTStorage;
import view.table.controller.TableControllerUserBench;

import java.io.*;

/**
 * Created by neukamm on 17.05.17.
 */
public class MultiFastaWriter implements IOutputData  {
    private final MTStorage mtStorage;
    private TableControllerUserBench tableController;

    public MultiFastaWriter(TableControllerUserBench tableController, MTStorage mtStorage){
        this.tableController = tableController;
        this.mtStorage = mtStorage;
    }

    @Override
    public void writeData(String file, TableControllerUserBench tableController) throws IOException {

        Writer writer = null;
        try {
            if(!(file.endsWith(".fa") ||file.endsWith(".fasta") || file.endsWith(".fna")))
                file = file + ".fa";

            writer = new BufferedWriter(new FileWriter(new File(file)));

            // write view.data
            this.tableController.getTableColumnByName("ID");
            String text = "";

            for(String id : mtStorage.getData().keySet()){
                text += ">" + id + "\n";
                text += mtStorage.getData().get(id) + "\n";
            }

            writer.write(text);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {

            writer.flush();
            writer.close();
        }

    }

    @Override
    public void setGroups(String groupID) {

    }
}
