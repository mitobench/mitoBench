package io.reader;

import io.Exceptions.ARPException;
import io.IInputData;
import io.datastructure.Entry;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by peltzer on 24/11/2016.
 */
public class ARPReader implements IInputData {
    private HashMap<String, List<Entry>> map = new HashMap<>();

    public ARPReader(String file) throws IOException, ARPException {
        FileReader fr = new FileReader(file);
        BufferedReader bfr = new BufferedReader(fr);


        String currline = "";
        int count = 0;
        boolean init = true;
        String currGroup = "";
            while ((currline = bfr.readLine()) != null) {
            if(init){
                init = false;
                //check if format is indeed ARP format
                if(currline.startsWith("[Profile]")){
                    continue;
                } else {
                    throw new ARPException("This is not in ARP Format!");
                }

            } else {
                if(currline.contains("SampleName=")){
                    //then we are inside the data block
                    String[] split = currline.split("\"");
                    currGroup = split[1];
                    continue;
                }
                if(currline.contains("[[Structure]]")){
                    break; //Then we are done with actual data, grouping is parsed differently by us...
                }
                if(currline.isEmpty() | currline.startsWith("}") |  currline.startsWith("SampleSize=") |
                   currline.startsWith("SampleData") | currline.startsWith("Title") | currline.startsWith("NbSamples") |
                   currline.startsWith("DataType") | currline.startsWith("LocusSeparator") | currline.startsWith("MissingData") |
                   currline.startsWith("GenotypicData") | currline.startsWith("[Data]") | currline.startsWith("[[Samples]]")) {
                    continue;
                } else {
                    String[] dataSplit = currline.split("\t");
                    String id = dataSplit[0];
                    String mtseq = dataSplit[2];
                    List<Entry> entries = new ArrayList<>();
                    Entry e = new Entry("MTSequence", "String", mtseq);
                    Entry e_group = new Entry("Grouping", "String", currGroup);
                    entries.add(e);
                    entries.add(e_group);
                    map.put(id, entries);
                }
            }
        }
    }



    @Override
    public HashMap<String, List<Entry>> getCorrespondingData() {
        return this.map;
    }
}
