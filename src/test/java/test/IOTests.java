package test;


import io.reader.MultiFastAInput;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
         is = getClass().getResourceAsStream(path);
         isr = new InputStreamReader(is);
         bfr = new BufferedReader(bfr);

    }


    @Test
    public void io_test_fasta(){
        setUp("/io/mFasta.fasta");
        HashMap output = null;
        try {
            MultiFastAInput multiFastAInput = new MultiFastAInput("/io/mFasta.fasta");
            output = multiFastAInput.getCorrespondingData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Data from Pagani et al 2016, 100 Mitochondrial genomes from Modern Egyptians
        assertEquals(output.size(), 100);
        assertEquals(output.containsValue(">egypt.10AJ136"), true);



    }

    @Test
    public void io_test_genericInput(){
        setUp("/generic_test_input.tsv");


    }

    @Test
    public void io_test_hsd(){
        setUp("/test_input_hsd.tsv");

    }





}
