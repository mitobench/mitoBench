package io.writer;

import io.IOutputData;
import javafx.collections.ObservableList;
import model.MTStorage;
import controller.TableControllerUserBench;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neukamm on 17.05.17.
 */
public class MultiFastaWriter implements IOutputData  {
    private final MTStorage mtStorage;
    private final ObservableList<ObservableList> data;

    public MultiFastaWriter(MTStorage mtStorage, ObservableList<ObservableList> dataToExport){
        this.mtStorage = mtStorage;
        this.data = dataToExport;
    }

    @Override
    public void writeData(String file, TableControllerUserBench tableController) throws IOException {

        Writer writer = null;
        try {
            if(!(file.endsWith(".fa") ||file.endsWith(".fasta") || file.endsWith(".fna")))
                file = file + ".fasta";

            writer = new BufferedWriter(new FileWriter(new File(file)));

            // write view.data
            tableController.getTableColumnByName("ID");
            String text = "";

            List<String> ids = new ArrayList<>();
            for(int i = 0; i < data.size(); i++){
                ids.add((String)data.get(i).get(0));
            }

            for(String id : mtStorage.getData().keySet()){
                if(ids.contains(id)){
                    text += ">" + id + "\n";
                    text += mtStorage.getData().get(id) + "\n";
                }
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
