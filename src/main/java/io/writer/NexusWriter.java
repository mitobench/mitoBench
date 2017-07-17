package io.writer;

import io.IOutputData;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import view.table.MTStorage;
import view.table.controller.TableControllerUserBench;

import java.io.*;
import java.util.List;

import static java.io.File.separator;

/**
 *
 * Writing sequences to nexus file only makes sense ig they have the same length
 * and were aligned before.
 *
 *
 *
 *

 #NEXUS
 Begin data;
 Dimensions ntax=4 nchar=15;
 Format datatype=dna missing=? gap=-;
 Matrix
 Species1   atgctagctagctcg
 Species2   atgcta??tag-tag
 Species3   atgttagctag-tgg
 Species4   atgttagctag-tag
 ;
 End;



 *
 * Created by neukamm on 18.07.17.
 */
public class NexusWriter implements IOutputData {


    public NexusWriter(){
    }

    @Override
    public void writeData(String file, TableControllerUserBench tableController) throws IOException {
        Writer writer = null;
        try {
            if(!file.endsWith(".nex"))
                file = file + ".nex";

            writer = new BufferedWriter(new FileWriter(new File(file)));

            int ntax = tableController.getSelectedRows().size();
            int nchar = 0;
            String missing_data_symbol = "N";
            // write header
            String header = "#NEXUS\nBegin data;\nDimensions ntax=" + ntax + " nchar=" + nchar + ";\n" +
                    "Format datatype=dna missing=" + missing_data_symbol + " gap=-;\nMatrix\n";

            writer.write(header);

            if(sequencesHaveSameLength(tableController.getDataTable().getMtStorage(),
                    tableController.getTableColumnByName("ID"),
                    tableController))

            // write view.data
            for (ObservableList entry :  tableController.getViewDataCurrent()) {
                String text = "";
                for(int i = 0; i < entry.size(); i++){
                    if(i == entry.size()-1){
                        text += entry.get(i) + "\n";
                    } else {
                        text += entry.get(i) + separator;
                    }
                }

                writer.write(text);
                writer.write(";\nEnd;");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {

            writer.flush();
            writer.close();
        }
    }

    private boolean sequencesHaveSameLength(MTStorage mtStorage, TableColumn id, TableControllerUserBench tableController) {
        int length;

        for (Object row : tableController.getTable().getItems()) {

                String id_val = (String) id.getCellObservableValue(row).getValue();
                int length_seq = mtStorage.getData().get(id_val).length();


        }

        return false;
    }

    @Override
    public void setGroups(String groupID) {
        // do nothing here
    }
}
