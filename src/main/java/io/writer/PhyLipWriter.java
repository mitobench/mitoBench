package io.writer;

import controller.ATableController;
import controller.TableControllerUserBench;
import io.IOutputData;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import model.MTStorage;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


/**
 *
 * Writing sequences to sequential/interleaved PHYLIP format only makes sense if they have the same length
 * and were aligned before.
 *
 *
 *
 *
 *
 * Sequential format, strict:
 * (taxon name max. 10 characters long)
 *
 4 60
 Species1  atgctagctagctcgatgctagctagctcg
 atgctagctagctcgatgctagctagctcg
 Species2  atgctagctag-tagatgctagctag-tag
 atgctagctag-tagatgctagctag-tag
 Species3  atgttagctag-tggatgttagctagatgg
 atgttagctag-tggatgttagctagatgg
 Species4  atgttagctagttagatgttagctag-tgg
 atgttagctagttagatgttagctag-tgg


 * Interleaved format, strict:
 *
 4 30
 Species1   atgctagctagctcg
 Species2   atgctagctag-tag
 Species3   atgttagctag-tgg
 Species4   atgttagctagttag

 atgctagctagctcg
 atgctagctag-tag
 atgttagctagatgg
 atgttagctag-tgg


 *
 * Sequential format, relaxed:
 * (taxon name max. 250 characters long, separated by single space from actual sequence)
 *
 4 60
 Species1_unknown atgctagctagctcgatgctagctagctcg
 atgctagctagctcgatgctagctagctcg
 Species2_unknown atgctagctag-tagatgctagctag-tag
 atgctagctag-tagatgctagctag-tag
 Species3_unknown atgttagctag-tggatgttagctagatgg
 atgctagctag-tagatgctagctag-tag
 Species4_unknown atgttagctagttagatgttagctag-tgg
 atgttagctag-tggatgttagctagatgg

 * Interleaved format, relaxed:
 *
 4 30
 Species1_unknown atgctagctagctcg
 Species2_unknown atgctagctag-tag
 Species3_unknown atgttagctag-tgg
 Species4_unknown atgttagctagttag

 atgctagctagctcg
 atgctagctag-tag
 atgttagctagatgg
 atgttagctag-tgg

 *
 * Created by neukamm on 30.11.17.
 */

public class PhyLipWriter implements IOutputData {

    private final ObservableList<ObservableList> data;
    private int length;
    private String format_type;
    private String interleaved_or_sequential;


    public PhyLipWriter(ObservableList<ObservableList> dataToExport, String format_type, String interleaved_or_sequential){
        this.data = dataToExport;
        this.format_type = format_type;
        this.interleaved_or_sequential = interleaved_or_sequential;
    }

    @Override
    public void writeData(String file, ATableController tableController) throws IOException {
        if(!file.endsWith(".phylip"))
            file = file + ".phylip";

        FileOutputStream fos = new FileOutputStream(file);
        FileChannel fileChannel = fos.getChannel();
        String text;

        try {

            MTStorage mtStorage = tableController.getDataTable().getMtStorage();
            if(sequencesHaveSameLength(mtStorage,
                    tableController.getTableColumnByName("ID"))){

                int ntax = data.size();
                int nchar = length;

                // write header
                String header = ntax + " " + nchar;
                fileChannel.write(ByteBuffer.wrap((header + "\n").getBytes()));

                // get IDs
                String[] ids = tableController.getSampleNames(data);
                int length_longest=0;
                for(String s : ids){
                    if(s.length() > length_longest)
                        length_longest = s.length();
                }

                int sequencePositionCounter = 0;
                int maxCharactersPerLine = 100;


                /*
                    Strict (= id max. 10 characters) and sequential format
                 */
                if (format_type.equals("Strict") && interleaved_or_sequential.equals("Sequential")) {

                    // write id + sequence
                    for(int i = 0; i < ids.length; i++){
                        sequencePositionCounter = 0;
                        String id_extended = String.format("%-" + 10 + "." + 10 + "s", ids[i]);

                        if(sequencePositionCounter+maxCharactersPerLine > length){
                            fileChannel.write(ByteBuffer.wrap((id_extended + "" + mtStorage.getData().get(ids[i]).substring(sequencePositionCounter, length) + "\n").getBytes()));

                        } else {

                            fileChannel.write(ByteBuffer.wrap((id_extended + "" + mtStorage.getData().get(ids[i]).substring(sequencePositionCounter, sequencePositionCounter+maxCharactersPerLine)+ "\n").getBytes()));
                            sequencePositionCounter += maxCharactersPerLine;
                        }

                        while(sequencePositionCounter+maxCharactersPerLine <= length){
                            if(sequencePositionCounter+maxCharactersPerLine > length){
                                fileChannel.write(ByteBuffer.wrap((mtStorage.getData().get(ids[i]).substring(sequencePositionCounter, length) + "\n").getBytes()));

                            } else {
                                fileChannel.write(ByteBuffer.wrap((mtStorage.getData().get(ids[i]).substring(sequencePositionCounter, sequencePositionCounter+maxCharactersPerLine)+ "\n").getBytes()));

                            }
                            sequencePositionCounter += maxCharactersPerLine;
                        }
                    }
                }

                 /*
                    Strict (= id max. 10 characters) and interleaved format
                 */

                 else  if (format_type.equals("Strict") && interleaved_or_sequential.equals("Interleaved")) {

                    // write id + sequence
                    for(int i = 0; i < ids.length; i++) {
                        sequencePositionCounter = 0;
                        String id_extended = String.format("%-" + 10 + "." + 10 + "s", ids[i]);

                        if (sequencePositionCounter + maxCharactersPerLine > length) {
                            fileChannel.write(ByteBuffer.wrap((id_extended + "" + mtStorage.getData().get(ids[i]).substring(sequencePositionCounter, length) + "\n").getBytes()));

                        } else {
                            fileChannel.write(ByteBuffer.wrap((id_extended + "" + mtStorage.getData().get(ids[i]).substring(sequencePositionCounter, sequencePositionCounter + maxCharactersPerLine) + "\n").getBytes()));

                            sequencePositionCounter += maxCharactersPerLine;
                        }
                    }
                    fileChannel.write(ByteBuffer.wrap("\n".getBytes()));

                    while(sequencePositionCounter <= length){
                        for(int i = 0; i < ids.length; i++) {
                            if(sequencePositionCounter+maxCharactersPerLine > length){
                                fileChannel.write(ByteBuffer.wrap((mtStorage.getData().get(ids[i]).substring(sequencePositionCounter, length) + "\n").getBytes()));

                            } else {
                                fileChannel.write(ByteBuffer.wrap((mtStorage.getData().get(ids[i]).substring(sequencePositionCounter, sequencePositionCounter+maxCharactersPerLine)+ "\n").getBytes()));
                            }
                        }
                        sequencePositionCounter += maxCharactersPerLine;
                        fileChannel.write(ByteBuffer.wrap("\n".getBytes()));
                    }
                }


                /*
                    Relaxed (= id can have more than 10 characters) and interleaved format
                */

                    else  if (format_type.equals("Relaxed") && interleaved_or_sequential.equals("Interleaved")){
                    // write IDs and sequences (100 characters of sequence per line)
                    // write id + sequence
                    for(int i = 0; i < ids.length; i++) {
                        sequencePositionCounter = 0;
                        //String id_extended = String.format("%-" + length_longest + "." + length_longest + "s", ids[i]);

                        if (sequencePositionCounter + maxCharactersPerLine > length) {
                            fileChannel.write(ByteBuffer.wrap((ids[i] + " " + mtStorage.getData().get(ids[i]).substring(sequencePositionCounter, length) + "\n").getBytes()));


                        } else {
                            fileChannel.write(ByteBuffer.wrap((ids[i] + " " + mtStorage.getData().get(ids[i]).substring(sequencePositionCounter, sequencePositionCounter + maxCharactersPerLine) + "\n").getBytes()));

                            sequencePositionCounter += maxCharactersPerLine;
                        }
                    }
                    fileChannel.write(ByteBuffer.wrap("\n".getBytes()));


                    while(sequencePositionCounter <= length){
                        for(int i = 0; i < ids.length; i++) {
                            if(sequencePositionCounter+maxCharactersPerLine > length){
                                fileChannel.write(ByteBuffer.wrap((mtStorage.getData().get(ids[i]).substring(sequencePositionCounter, length) + "\n").getBytes()));

                            } else {
                                fileChannel.write(ByteBuffer.wrap((mtStorage.getData().get(ids[i]).substring(sequencePositionCounter, sequencePositionCounter+maxCharactersPerLine)+ "\n").getBytes()));

                            }
                        }
                        sequencePositionCounter += maxCharactersPerLine;
                        fileChannel.write(ByteBuffer.wrap("\n".getBytes()));

                    }

                }


                /*
                    Relaxed (= id can have more than 10 characters) and sequential format
                */

                else if (format_type.equals("Relaxed") && interleaved_or_sequential.equals("Sequential")) {

                    // write id + sequence
                    for(int i = 0; i < ids.length; i++){
                        sequencePositionCounter = 0;
                        String id_extended = String.format("%-" + length_longest + "." + length_longest + "s", ids[i]);

                        if(sequencePositionCounter+maxCharactersPerLine > length){
                            fileChannel.write(ByteBuffer.wrap((id_extended + " " + mtStorage.getData().get(ids[i]).substring(sequencePositionCounter, length) + "\n").getBytes()));


                        } else {
                            fileChannel.write(ByteBuffer.wrap((id_extended + " " + mtStorage.getData().get(ids[i]).substring(sequencePositionCounter, sequencePositionCounter+maxCharactersPerLine)+ "\n").getBytes()));
                            sequencePositionCounter += maxCharactersPerLine;
                        }

                        while(sequencePositionCounter+maxCharactersPerLine <= length){
                            if(sequencePositionCounter+maxCharactersPerLine > length){
                                fileChannel.write(ByteBuffer.wrap((mtStorage.getData().get(ids[i]).substring(sequencePositionCounter, length) + "\n").getBytes()));

                            } else {
                                fileChannel.write(ByteBuffer.wrap((mtStorage.getData().get(ids[i]).substring(sequencePositionCounter, sequencePositionCounter+maxCharactersPerLine)+ "\n").getBytes()));

                            }
                            sequencePositionCounter += maxCharactersPerLine;
                        }
                    }
                }

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

    }
}
