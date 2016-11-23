package io.reader;

import io.Exceptions.FastAException;
import io.IInputData;
import io.datastructure.Entry;
import io.datastructure.inputTypes.FastaEntry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public MultiFastAInput(String fileToParse) throws IOException, FastAException {
        fastaEntrys = new ArrayList<FastaEntry>();
        File f = new File(fileToParse);
        fr = new FileReader(f);
        bfr = new BufferedReader(fr);

        String currentLine = "";
        String currHeader = "";
        String currSeq = "";

        int init = 0;
        int line_index = 0;

        while ((currentLine = bfr.readLine()) != null) {
            if (!currHeader.equals("") && currentLine.startsWith(">")) {
                //we have finished our first entry then
                FastaEntry faentry = new FastaEntry(currSeq, currHeader);
                fastaEntrys.add(faentry);
                //And reset everything
                currSeq = "";
                currHeader = currentLine.replace(">","");
                line_index++;
                continue;
            }

            if (currentLine.startsWith(">") && (init == 0)) { //then we have a header (first header)
                currHeader = currentLine.replace(">","");
                init = -1;
                line_index++;
                continue;
            } else { // we have sequence
                //Checking string for consistency properly with a regular expression

                line_index++;
                Pattern p = Pattern.compile("[ACTGNactgn-]*\n*");
                Matcher m = p.matcher(currentLine);
                if(m.matches()){
                    currSeq += currentLine;
                } else {
                    throw new FastAException("Your FastA entry in line " + line_index + " is incorrect. Please check your input file for correctness.");
                }
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
