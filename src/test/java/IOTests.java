import io.datastructure.Entry;
import io.reader.HSDInput;
import io.reader.MultiFastAInput;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by peltzer on 18/11/2016.
 */
public class IOTests {
    private InputStream is;
    private InputStreamReader isr;
    private BufferedReader bfr;


    private void setUp(String path){
        URL url = getClass().getResource(path);
        try {
            is = url.openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isr = new InputStreamReader(is);
        bfr = new BufferedReader(isr);
    }


    @Test
    public void io_test_fasta(){
        String path = "./mFasta.fasta";
        setUp(path);
        HashMap output = null;
        try {
            MultiFastAInput multiFastAInput = new MultiFastAInput(getClass().getResource(path).getPath());
            output = multiFastAInput.getCorrespondingData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Data from Pagani et al 2016, 100 Mitochondrial genomes from Modern Egyptians
        assertEquals(output.size(), 100);
        assertEquals(output.containsKey(">egypt.14AJ129"), true);
    }

    @Test
    public void io_test_genericInput(){
       // setUp("/generic_test_input.tsv");



    }

    @Test
    public void io_test_hsd(){
        String path = "./haplotest.hsd";
        setUp(path);

        HashMap output = null;

        try {
            System.out.println(getClass().getResource(path));
            HSDInput hsdInput = new HSDInput(getClass().getResource(path).getPath());
            output = hsdInput.getCorrespondingData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Data from our own publication... to be cited here!
        assertEquals(5, output.size());
        Entry e = (Entry) output.get("ID1"); //needs cast here
        assertEquals(e.getType(), "String");
        assertEquals(e.getIdentifier(), "ID1");
        assertEquals(e.getData(), "R0a2f");


    }





}
