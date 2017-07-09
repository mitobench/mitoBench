package io.reader;

import database.ColumnNameMapper;
import io.Exceptions.ARPException;
import io.IInputData;
import io.datastructure.Entry;
import io.datastructure.generic.GenericInputData;
import io.inputtypes.CategoricInputType;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by peltzer on 24/11/2016.
 */
public class ARPReader implements IInputData {
    private HashMap<String, List<Entry>> map = new HashMap<>();
    private Logger LOG;

    public ARPReader(String file, Logger logger) throws IOException, ARPException {
        LOG = logger;
        LOG.info("Read ARP file: " + file);
        FileReader fr = new FileReader(file);
        BufferedReader bfr = new BufferedReader(fr);
        ColumnNameMapper mapper = new ColumnNameMapper();


        String currline;
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
                    //then we are inside the view.data block
                    String[] split = currline.split("\"");
                    currGroup = split[1];
                    continue;
                }
                if(currline.contains("[[Structure]]")){
                    break; //Then we are done with actual view.data, grouping is parsed differently by us...
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
                    Entry e = new Entry(mapper.mapString("MTSequence"), new CategoricInputType("String"), new GenericInputData(mtseq));
                    Entry e_group = new Entry(mapper.mapString("ARP-Groups"), new CategoricInputType("String"), new GenericInputData(currGroup));
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
