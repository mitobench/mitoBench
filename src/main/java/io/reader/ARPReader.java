package io.reader;

import io.Exceptions.ARPException;
import io.IInputData;
import io.datastructure.Entry;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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

        while ((currline = bfr.readLine()) != null) {
            if(init){
                init = false;
                //check if format is indeed ARP format
                if(currline.contains("[Profile]")){
                    continue;
                } else {
                    throw new ARPException("This is not in ARP Format!");
                }

            } else { //ned to take samplename into account, too
                if(currline.contains("SampleData =")){
                    //then we are inside the data block
                }
            }

        }



//            //Parse header, two line header !!
//            if (currline.startsWith("##")) {
//                headergroup = currline.replace("##","").split("\t");
//                continue;
//            } else if (currline.startsWith("#")) {
//                headertype = currline.replace("#","").split("\t");
//                continue;
//            } else {
//                String[] splitLine = currline.split("\t");
//                //Assume ID is always first! -> requirement
//                List<Entry> entries = new ArrayList<>();
//
//                for (int i = 0; i < splitLine.length; i++) {
//                    Entry e = new Entry(headergroup[i], headertype[i], splitLine[i]);
//                    entries.add(e);
//                }
//                //Now add with ID to hashmap
//                map.put(splitLine[0], entries);
//
//
//            }
//        }


    }



    @Override
    public HashMap<String, List<Entry>> getCorrespondingData() {
        return null;
    }
}
