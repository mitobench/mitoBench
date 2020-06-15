package io.reader;

import io.Exceptions.FastAException;
import io.IInputData;
import io.datastructure.Entry;
import io.datastructure.fastA.FastaEntry;
import io.datastructure.generic.GenericInputData;
import io.inputtypes.CategoricInputType;
import org.apache.log4j.Logger;

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
public class MultiFastaParser implements IInputData {
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
     * @param LOG
     * @throws IOException
     */
    public MultiFastaParser(String fileToParse, Logger LOG) throws IOException, FastAException {
        LOG.info("Read FastA file: " + fileToParse);

        fastaEntrys = new ArrayList<>();
        File f = new File(fileToParse);
        fr = new FileReader(f);
        bfr = new BufferedReader(fr);

        String currentLine;
        String currHeader = "";
        String currSeq = "";

        int init = 0;
        boolean seq_length_should_be_equal = true;
        int line_index = 0;
        int seq_length = 0;

        while ((currentLine = bfr.readLine()) != null) {
            currentLine = currentLine.trim();
            if (!currHeader.equals("") && currentLine.startsWith(">")) {
                if(seq_length_should_be_equal){
                    seq_length = currSeq.length();
                    seq_length_should_be_equal = false;
                }
                //Check whether lengths are equal between individual FastA entries, this is crucial and should be the case!
//                if(currSeq.length() != seq_length){
//                    throw new FastAException("Your sequence lengths do not match each other. Please ensure that you performed a multiple sequence alignment of your FastA entries first, before using them here.");
//                }
                //we have finished our first entry then
                FastaEntry faentry = new FastaEntry(currSeq, currHeader);
                fastaEntrys.add(faentry);
                //And reset everything
                currSeq = "";
                currHeader = currentLine.replace(">","").split(" ")[0];
                if(currHeader.matches(".*[^\\d]\\d{1}$")){
                    currHeader = currHeader.split("\\.")[0];
                }
                line_index++;
                continue;
            }

            if (currentLine.startsWith(">") && (init == 0)) { //then we have a header (first header)

                String header = currentLine.replace(">", "").split(" ")[0];

                // remove version number
                if(header.matches(".*[^\\d]\\d{1}$")){// || header.endsWith(".1") || header.endsWith(".2") || header.endsWith(".3") || header.endsWith(".4") || header.endsWith(".5")){
                    header = header.split("\\.")[0].trim();
                }
                currHeader = header;
                init = -1;
                line_index++;
                continue;
            } else { // we have sequence
                //Checking string for consistency properly with a regular expression
                line_index++;
                Pattern p = Pattern.compile("[ACTGRYSWKMVDHVNactgryswkmndhv-]*\n*");
                Matcher m = p.matcher(currentLine);
                if(m.matches()){
                    currSeq += currentLine;
                } else {
                    throw new FastAException("Your FastA entry in line " + line_index + " is incorrect. Please check your input file for correctness.\n" + currentLine);
                }
            }
        }

//        if(currSeq.length() != seq_length && seq_length!=0){
//            throw new FastAException("Your sequence lengths do not match each other. Please ensure that you performed a multiple sequence alignment of your FastA entries first, before using them here.");
//        } else {
            FastaEntry faentry = new FastaEntry(currSeq, currHeader);
            fastaEntrys.add(faentry);
        //}
    }


    /**
     * This method translates our list of read FastA entries into our specific dataformat for our GUI class.
     *
     * @return
     */
    private HashMap<String,List<Entry>> getOutput(){
        HashMap<String, List<Entry>> output = new HashMap<>();

        for(FastaEntry fa : fastaEntrys){
            Entry entry = new Entry("MTSequence", new CategoricInputType("String"), new GenericInputData(fa.getSequence()));
            List<Entry> sequence = new ArrayList<>();
            sequence.add(entry);
            output.put(fa.getHeader(), sequence);
        }

        return output;
    }


}
