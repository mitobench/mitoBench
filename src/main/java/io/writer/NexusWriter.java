package io.writer;

import controller.ATableController;
import io.IOutputData;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import model.MTStorage;
import controller.TableControllerUserBench;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

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


    private final ObservableList<ObservableList> data;
    private int length;

    public NexusWriter(ObservableList<ObservableList> dataToExport){
        this.data = dataToExport;
    }

    @Override
    public void writeData(String file, ATableController tableController) throws IOException {

        if(!file.endsWith(".nex"))
            file = file + ".nex";

        FileOutputStream fos = new FileOutputStream(file);
        FileChannel fileChannel = fos.getChannel();
        try {

            MTStorage mtStorage = tableController.getDataTable().getMtStorage();
            if(sequencesHaveSameLength(mtStorage,
                    tableController.getTableColumnByName("ID"))){

                int ntax = data.size();
                int nchar = length;
                String missing_data_symbol = "N";
                // write header
                String header = "#NEXUS\nBegin data;\nDimensions ntax=" + ntax + " nchar=" + nchar + ";\n" +
                        "Format datatype=dna Interleave=yes missing=" + missing_data_symbol + " gap=-;\nMatrix\n";

                fileChannel.write(ByteBuffer.wrap(header.getBytes()));

                // get IDs
                String[] ids = tableController.getSampleNames(data);
                int length_longest=0;
                for(String s : ids){
                    if(s.length() > length_longest)
                        length_longest = s.length();
                }
                length_longest +=3;


                // write IDs and sequences (50 characters of sequence per line)
                int sequencePositionCounter = 0;
                int maxCharactersPerLine = 70;
                String text;

                while (sequencePositionCounter < length){
                    for(String id : ids){
                        String id_extended = String.format("%-" + length_longest + "." + length_longest + "s", id);
                        //writer.write(id_extended + "" + mtStorage.getData().get(id) + "\n");
//
                        if(sequencePositionCounter+maxCharactersPerLine > length){
                            text = id_extended + "" + mtStorage.getData().get(id).substring(sequencePositionCounter,
                                    length) + "\n";
                            fileChannel.write(ByteBuffer.wrap(text.getBytes()));
                        } else {
                            text = id_extended + "" + mtStorage.getData().get(id).substring(sequencePositionCounter,
                                    sequencePositionCounter+maxCharactersPerLine)+ "\n";
                            fileChannel.write(ByteBuffer.wrap(text.getBytes()));
                        }
                    }
                    sequencePositionCounter += maxCharactersPerLine;
                    fileChannel.write(ByteBuffer.wrap("\n".getBytes()));
                }

                fileChannel.write(ByteBuffer.wrap(";\nEnd;".getBytes()));

            } else {
                throw new Exception("Sequences do not have the same length!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            fileChannel.close();
            fos.close();
        }
    }


    /**
     * This method iterates over all sequences and test for equal length.
     *
     * @param mtStorage
     * @param id
     * @return
     */
    private boolean sequencesHaveSameLength(MTStorage mtStorage, TableColumn id) {
        length = 0;

        for (Object row : data) {

            String id_val = (String) id.getCellObservableValue(row).getValue();

            if(length==0){
                length = mtStorage.getData().get(id_val).length();
            } else{
                if(length!=mtStorage.getData().get(id_val).length()){
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void setGroups(String groupID) {
        // do nothing here
    }
}
