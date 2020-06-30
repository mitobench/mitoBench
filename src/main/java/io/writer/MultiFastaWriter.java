package io.writer;
import controller.ATableController;
import io.IOutputData;
import javafx.collections.ObservableList;
import model.MTStorage;
import controller.TableControllerUserBench;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neukamm on 17.05.17.
 */
public class MultiFastaWriter implements IOutputData  {
    private final MTStorage mtStorage;
    private final ObservableList<ObservableList> data;
    private final boolean writeUnaligned;

    public MultiFastaWriter(MTStorage mtStorage, ObservableList<ObservableList> dataToExport, boolean writeUnaligned){
        this.mtStorage = mtStorage;
        this.data = dataToExport;
        this.writeUnaligned = writeUnaligned;
    }

    @Override
    public void writeData(String file, ATableController tableController) throws IOException {

        if(!(file.endsWith(".fa") ||file.endsWith(".fasta") || file.endsWith(".fna")))
            file = file + ".fasta";

        FileOutputStream fos = new FileOutputStream(file);
        FileChannel fileChannel = fos.getChannel();

        // write view.data
        tableController.getTableColumnByName("ID");
        String text;

        List<String> ids = new ArrayList<>();
        for(int i = 0; i < data.size(); i++){
            ids.add((String)data.get(i).get(0));
        }

        for(String id : mtStorage.getData().keySet()){
            if(ids.contains(id)){
                if(this.writeUnaligned){
                    text = ">" + id + "\n" + mtStorage.getData().get(id).replace("-","") + "\n";
                } else {
                    text = ">" + id + "\n" + mtStorage.getData().get(id) + "\n";
                }

                fileChannel.write(ByteBuffer.wrap(text.getBytes()));
            }
        }

        fileChannel.close();
        fos.close();
    }

    @Override
    public void setGroups(String groupID) {

    }
}
