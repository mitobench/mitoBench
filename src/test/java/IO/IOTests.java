package IO;

import io.Exceptions.ARPException;
import io.Exceptions.FastAException;
import io.Exceptions.HSDException;
import io.datastructure.Entry;
import io.datastructure.arp.ArpProfile;
import io.datastructure.arp.ArpStructure;
import io.datastructure.generic.GenericInputData;
import io.datastructure.radiocarbon.RadioCarbonData;
import io.inputtypes.CategoricInputType;
import io.inputtypes.RadioCarbonInputType;
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
        System.out.println(url);
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

        //Testing view.data input here...
        assertEquals(2, output.size());

        ArrayList cast = (ArrayList) output.get("Test1");
        Entry entry_c14 = (Entry) cast.get(1);
        Entry entry_bone = (Entry) cast.get(2);
        Entry entry_moonphase = (Entry) cast.get(3);

        assertEquals(new RadioCarbonData("cal BC 338-256", RadioCarbonData.PARSE_C14_DATE_INFORMATION).getTableInformation(), entry_c14.getData().getTableInformation());
        assertEquals("C14-Date", entry_c14.getIdentifier());
        assertEquals(new RadioCarbonInputType("C14").getTypeInformation(), entry_c14.getType().getTypeInformation());


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

        assertEquals(entry.getType().getTypeInformation(), new CategoricInputType("String").getTypeInformation());
        assertEquals(entry.getIdentifier(), "Haplogroup");
        assertEquals(entry.getData().getTableInformation(), new GenericInputData( "R0a2f").getTableInformation());
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

        assertEquals(entry.getType().getTypeInformation(), new CategoricInputType("String").getTypeInformation());
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

    /**
     * C14 Dating Test - we need to be able to parse quite some formats here, to make sure that things are handled properly, some tests here :-)
     */

    @Test
    public void io_test_c14_ad() {

        String test1 = "cal AD 235-336";

        RadioCarbonData radioCarbonData = new RadioCarbonData(test1, RadioCarbonData.PARSE_C14_DATE_INFORMATION);

        assertEquals(235, radioCarbonData.getLower_limit());
        assertEquals(336, radioCarbonData.getUpper_limit());
        assertEquals(235.0 + Math.abs(radioCarbonData.getUpper_limit() - radioCarbonData.getLower_limit()) / 2, radioCarbonData.getAverage());
    }

    @Test
    public void io_test_c14_bc() {
        String test2 = "cal BC 1304-1136";
        RadioCarbonData radioCarbonData = new RadioCarbonData(test2, RadioCarbonData.PARSE_C14_DATE_INFORMATION);

        assertEquals(-1304, radioCarbonData.getLower_limit());
        assertEquals(-1136, radioCarbonData.getUpper_limit());
        assertEquals(-1304.0 + Math.abs(radioCarbonData.getUpper_limit() - radioCarbonData.getLower_limit()) / 2, radioCarbonData.getAverage());
    }

    @Test
    public void io_test_c14_adbc_mixed() {
        String test3 = "cal BC 44-cal AD 16";
        RadioCarbonData radioCarbonData = new RadioCarbonData(test3, RadioCarbonData.PARSE_C14_DATE_INFORMATION);

        assertEquals(-44, radioCarbonData.getLower_limit());
        assertEquals(16, radioCarbonData.getUpper_limit());
        assertEquals(-44.0 + Math.abs(radioCarbonData.getLower_limit() - radioCarbonData.getUpper_limit()) / 2, radioCarbonData.getAverage());
    }



}
