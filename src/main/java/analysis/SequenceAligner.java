package analysis;

import controller.TableControllerUserBench;
import io.datastructure.Entry;
import io.datastructure.generic.GenericInputData;
import io.inputtypes.CategoricInputType;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SequenceAligner {

    private final Logger log;
    private final TableControllerUserBench tablecontroller;
    private HashMap<String, String> acc_sequence;

    public SequenceAligner(Logger log, TableControllerUserBench tableControllerUserBench){
        this.log = log;
        this.tablecontroller = tableControllerUserBench;

    }

    /**
     * Align given set of unaligned sequences with MAFFT.
     *
     * @param infile
     */
    public void align(String infile) {
        URL url = this.getClass().getResource("/MAFFT/mafft");
        System.out.println(url.getPath());
        acc_sequence = null;

        try {
            String line;

            acc_sequence = new HashMap<>();
            String command = url.getPath() + " " + infile;// + " --auto " + infile + " > sequences_aligned.fasta";
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader bri = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));

            while ((line = bri.readLine()) != null) {

                collectAlignment(line, bri);

            }
            bri.close();
            p.waitFor();
            System.out.println("MAFFT done.");

            // parse to table
            HashMap<String, List<Entry>> output = new HashMap<>();

            for(String acc : acc_sequence.keySet()){
                Entry entry = new Entry("MTSequence", new CategoricInputType("String"), new GenericInputData(acc_sequence.get(acc)));
                List<Entry> entries = new ArrayList<>();
                entries.add(entry);
                output.put(acc, entries);
            }
            tablecontroller.updateTable(output);


        }
        catch (Exception err) {
            err.printStackTrace();
        }




    }

    /**
     * Iterate through mafft output and collect alignment.
     *
     * @param line
     * @param breader
     * @throws IOException
     */
    private void collectAlignment(String line, BufferedReader breader) throws IOException {
        String acc;
        if(line.startsWith(">")){
            acc = line.trim().substring(1, line.length());
            if (!acc_sequence.keySet().contains(acc)) {
                acc_sequence.put(acc, "");
            }
            while ((line = breader.readLine()) != null){
                if(line.startsWith(">")){
                    collectAlignment(line, breader);
                } else {
                    String seq_tmp = acc_sequence.get(acc);
                    seq_tmp += line;
                    acc_sequence.put(acc, seq_tmp);
                }
            }
        }
    }

}
