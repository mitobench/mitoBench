package io.writer;

import controller.TableControllerUserBench;
import io.IOutputData;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import view.table.MTStorage;

import java.io.*;


/**
 *
 * Writing sequences to sequential/interleaved PHYLIP format only makes sense if they have the same length
 * and were aligned before.
 *
 *
 *
 *
 *
 * Sequential format:
 *
 4 15
 Species1   atgctagctagctcgatgctagctagctcg
 Species2   atgctagctag-tagatgctagctag-tag
 Species3   atgttagctag-tggatgttagctagatgg
 Species4   atgttagctagttagatgttagctag-tgg


 * Interleaved format:
 *
 4 15
 Species1   atgctagctagctcg
 Species2   atgctagctag-tag
 Species3   atgttagctag-tgg
 Species4   atgttagctagttag

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
    private boolean interleaved;


    public PhyLipWriter(ObservableList<ObservableList> dataToExport, boolean interleaved){
        this.data = dataToExport;
        this.interleaved = interleaved;
    }

    @Override
    public void writeData(String file, TableControllerUserBench tableController) throws IOException {
        Writer writer = null;
        try {

            MTStorage mtStorage = tableController.getDataTable().getMtStorage();
            if(sequencesHaveSameLength(mtStorage,
                    tableController.getTableColumnByName("ID"))){

                if(!file.endsWith(".phylip") || !file.endsWith(".PHYLIP"))
                    file = file + ".phylip";

                writer = new BufferedWriter(new FileWriter(new File(file)));

                int ntax = data.size();
                int nchar = length;

                // write header
                String header = ntax + " " + nchar;
                writer.write(header);

                // get IDs
                String[] ids = tableController.getSampleNames(data);
                int length_longest=0;
                for(String s : ids){
                    if(s.length() > length_longest)
                        length_longest = s.length();
                }
                length_longest +=3;


                if (interleaved){

                } else {

                }

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

    }
}
