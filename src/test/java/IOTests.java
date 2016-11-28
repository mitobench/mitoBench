import io.Exceptions.ARPException;
import io.Exceptions.FastAException;
import io.Exceptions.HSDException;
import io.datastructure.Entry;
import io.reader.ARPReader;
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


    private void setUp(String path) {
        URL url = getClass().getResource(path);
        try {
            is = url.openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isr = new InputStreamReader(is);
        bfr = new BufferedReader(isr);
    }


    /**
     * FastA reader tests
     * @throws FastAException
     */

    @Test
    public void io_test_fasta() throws FastAException {
        String path = "./mFasta.fasta";
        setUp(path);
        HashMap output = null;
        try {
            MultiFastAInput multiFastAInput = new MultiFastAInput(getClass().getResource(path).getPath());
            output = multiFastAInput.getCorrespondingData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Data from Pagani et al 2016, 100 Mitochondrial genomes from Modern Egyptians (we just use 2 for testing here...)
        assertEquals(output.size(), 2);
        assertEquals(output.containsKey("egypt.10AJ137"), true);
    }

    @Test(expected = FastAException.class)
    public void io_test_fasta_lengths() throws FastAException {
        String path = "./fasta_incorrect_sequence_lengths.fasta";
        setUp(path);
        HashMap output = null;
        try {
            MultiFastAInput multiFastAInput = new MultiFastAInput(getClass().getResource(path).getPath());
            output = multiFastAInput.getCorrespondingData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = FastAException.class)
    public void io_test_fasta_incorrect() throws FastAException {
        String path = "./fasta_incorrect_letters.fasta";
        setUp(path);
        HashMap output = null;
        try {
            MultiFastAInput multiFastAInput = new MultiFastAInput(getClass().getResource(path).getPath());
            output = multiFastAInput.getCorrespondingData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = FastAException.class)
    public void io_test_fasta_incorrect_lenghts_second_case() throws FastAException {
        String path = "./fasta_incorrect_sequence_lengths_multiple.fasta";
        setUp(path);
        HashMap output = null;
        try {
            MultiFastAInput multiFastAInput = new MultiFastAInput(getClass().getResource(path).getPath());
            output = multiFastAInput.getCorrespondingData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Generic Input tests
     */

    @Test
    public void io_test_genericInput() {
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

    /**
     * Haplogrep 2 Format reader Tests
     *
     */

    @Test
    public void io_test_hsd() {
        String path = "./haplotest.hsd";
        setUp(path);

        HashMap output = null;

        try {
            HSDInput hsdInput = null;
            try {
                hsdInput = new HSDInput(getClass().getResource(path).getPath());
            } catch (HSDException e) {
                e.printStackTrace();
            }
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

    @Test(expected = HSDException.class)
    public void io_test_hsd_incorrect() throws HSDException{
        String path = "./hsd_incorrect.hsd";
        setUp(path);

        HashMap output = null;
        try {
            HSDInput hsdInput = new HSDInput(getClass().getResource(path).getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * ARP Format reader test
     */

    @Test
    public void io_test_arp(){
        String path = "./arp_test_correct.arp";
        setUp(path);

        HashMap output = null;

        try {
            ARPReader arpinput = null;
            try {
                arpinput = new ARPReader(getClass().getResource(path).getPath());
            } catch (ARPException e) {
                e.printStackTrace();
            }
            output = arpinput.getCorrespondingData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Data from our own publication... to be cited here!
        assertEquals(5, output.size());

        ArrayList cast = (ArrayList) output.get("test1"); //needs cast here
        Entry entry = (Entry) cast.get(0);

        assertEquals(entry.getType(), "String");
        assertEquals(entry.getIdentifier(), "MTSequence");
    }

    @Test(expected = ARPException.class)
    public void io_test_arp_incorrect() throws ARPException{
        String path = "./arp_test_incorrect.arp";
        setUp(path);

        HashMap output = null;
        try {
            ARPReader arpReader = new ARPReader(getClass().getResource(path).getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
