import io.datastructure.Entry;
import io.reader.GenericInputParser;
import io.reader.HSDInput;
import io.reader.MultiFastAInput;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
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
        assertEquals(output.containsKey("egypt.14AJ129"), true);
    }

    @Test
    public void io_test_genericInput(){
        String path = "./genericinput.tsv";
        setUp(path);

        HashMap output = null;

        try {
            GenericInputParser gi = new GenericInputParser(getClass().getResource(path).getPath());
            output = gi.getCorrespondingData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Testing data input here...
        assertEquals(2, output.size());

        ArrayList cast = (ArrayList) output.get("Test1");
        Entry entry_c14 = (Entry) cast.get(1);
        Entry entry_bone = (Entry) cast.get(2);
        Entry entry_moonphase = (Entry) cast.get(3);

        assertEquals("String", entry_c14.getType());
        assertEquals("C14-Date", entry_c14.getIdentifier());
        assertEquals("-3400", entry_c14.getData());




    }

    @Test
    public void io_test_hsd(){
        String path = "./haplotest.hsd";
        setUp(path);

        HashMap output = null;

        try {
            HSDInput hsdInput = new HSDInput(getClass().getResource(path).getPath());
            output = hsdInput.getCorrespondingData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Data from our own publication... to be cited here!
        assertEquals(5, output.size());

        ArrayList cast = (ArrayList) output.get("ID1"); //needs cast here
        Entry entry = (Entry) cast.get(0);

        assertEquals(entry.getType(), "String");
        assertEquals(entry.getIdentifier(), "Haplogroup");
        assertEquals(entry.getData(), "R0a2f");



    }





}
