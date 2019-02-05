package io.writer;

import io.IOutputData;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import model.MTStorage;
import controller.TableControllerUserBench;

import java.io.*;

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
    public void writeData(String file, TableControllerUserBench tableController) throws IOException {
        Writer writer = null;
        try {

            MTStorage mtStorage = tableController.getDataTable().getMtStorage();
            if(sequencesHaveSameLength(mtStorage,
                    tableController.getTableColumnByName("ID"))){

                if(!file.endsWith(".nex"))
                    file = file + ".nex";

                writer = new BufferedWriter(new FileWriter(new File(file)));

                int ntax = data.size();
                int nchar = length;
                String missing_data_symbol = "N";
                // write header
                String header = "#NEXUS\nBegin data;\nDimensions ntax=" + ntax + " nchar=" + nchar + ";\n" +
                        "Format datatype=dna Interleave=yes missing=" + missing_data_symbol + " gap=-;\nMatrix\n";

                writer.write(header);

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

                while (sequencePositionCounter < length){
                    for(String id : ids){
                        String id_extended = String.format("%-" + length_longest + "." + length_longest + "s", id);
                        //writer.write(id_extended + "" + mtStorage.getData().get(id) + "\n");
//
                        if(sequencePositionCounter+maxCharactersPerLine > length){
                            writer.write(id_extended + "" + mtStorage.getData().get(id).substring(sequencePositionCounter,
                                    length) + "\n");
                        } else {
                            writer.write(id_extended + "" + mtStorage.getData().get(id).substring(sequencePositionCounter,
                                    sequencePositionCounter+maxCharactersPerLine)+ "\n");
                        }
                    }
                    sequencePositionCounter += maxCharactersPerLine;
                    writer.write("\n");
                }

                writer.write(";\nEnd;");


            } else {
                throw new Exception("Sequences do not have the same length!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            writer.flush();
            writer.close();
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
