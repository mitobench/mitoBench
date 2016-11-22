package io.reader;

import io.IInputData;
import io.datastructure.Entry;
import io.datastructure.inputTypes.FastaEntry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by peltzer on 17/11/2016.
 */
public class MultiFastAInput implements IInputData {
    private ArrayList<FastaEntry> fastaEntrys;
    private BufferedReader bfr;
    private FileReader fr;

    @Override
    public HashMap<String, List<Entry>> getCorrespondingData() {
        return getOutput();
    }


    /**
     * This method reads the actual file and generates an ArrayList of FastA entries properly.
     * @param fileToParse
     * @throws IOException
     */
    public MultiFastAInput(String fileToParse) throws IOException {
        fastaEntrys = new ArrayList<FastaEntry>();
        File f = new File(fileToParse);
        fr = new FileReader(f);
        bfr = new BufferedReader(fr);

        String currentLine = "";
        String currHeader = "";
        String currSeq = "";

        while ((currentLine = bfr.readLine()) != null) {
            if (!currHeader.equals("") && currentLine.startsWith(">")) {
                //we have our first entry then...
                FastaEntry faentry = new FastaEntry(currSeq, currHeader);
                fastaEntrys.add(faentry);
                //And reset everything
                currSeq = "";
                currHeader = currentLine.replace(">","");
                continue;
            }

            if (currentLine.startsWith(">")) { //then we have a header
                currHeader = currentLine.replace(">","");
                continue;
            } else { // we have sequence
                currSeq += currentLine;
            }
        }

        FastaEntry faentry = new FastaEntry(currSeq, currHeader);
        fastaEntrys.add(faentry);
    }


    /**
     * This method translates our list of read FastA entries into our specific dataformat for our GUI class.
     *
     * @return
     */
    private HashMap<String,List<Entry>> getOutput(){
        HashMap<String, List<Entry>> output = new HashMap<>();

        for(FastaEntry fa : fastaEntrys){
            Entry entry = new Entry("MTSequence", "String", fa.getSequence());
            List<Entry> sequence = new ArrayList<>();
            sequence.add(entry);
            output.put(fa.getHeader(), sequence);
        }

        return output;
    }


}
