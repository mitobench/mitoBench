package IO;

import Logging.LogClass;
import io.datastructure.arp.ArpProfile;
import io.datastructure.arp.ArpStructure;
import io.writer.ARPWriter;
import org.junit.Before;
import org.junit.Test;
import view.table.controller.TableControllerUserBench;

import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by neukamm on 30.01.17.
 */
public class IOProfileTest {

    private TableControllerUserBench tableController;


    @Before
    public void setup() throws TimeoutException {

        LogClass logClass = new LogClass();
        logClass.setUp();

    }

    @Test
    public void arp_profile_Test() {

        ARPWriter arpWriter = new ARPWriter(tableController);

        String name = "path/to/file.arp";
        String numberofsamples = "5";
        ArpProfile arpProfile = new ArpProfile(name, numberofsamples, "DNA", "N");
        String out_corr = "[Profile]" + "\n\n\n" +
                "Title=\"" + name + "\"" + "\n" +
                "NbSamples="+numberofsamples+"\n"+
                "GenotypicData=0\n"+
                "DataType="+"DNA"+"\n"+
                "LocusSeparator=NONE\n"+
                "MissingData=\""+"N"+"\"\n\n";
        assertEquals(out_corr, arpProfile.getProfile());


        HashMap<String,Integer>  groupnames = new HashMap();
        int groups = 2;
        groupnames.put("Ancient", 91);
        groupnames.put("Egyptian", 100);
        ArpStructure arpStructure = new ArpStructure(groups, groupnames);

        String structure_tmp =
                          "\n\n"+"[[Structure]]" + "\n\n"
                        + "StructureName=\"MitoBenchStarter Exported Structure\"\n"
                        + "NbGroups="+groups+"\n\n\n"
                        + "Group={\n"+"\""+ "Ancient" +"\""+ "\n" +"}" + "\n\n"
                        + "Group={\n"+"\""+ "Egyptian" +"\""+ "\n" +"}" + "\n\n";
        assertEquals(structure_tmp, arpStructure.getArpStructure());

//
//        ArpSample arpSample = new ArpSample(arpWriter.getSequenceData(), arpWriter.returnRegions(), groups);
//        String multiHeader =  "[Data]\n[[Samples]]\n\n\n";
//        String samples = "";
//        String sample_tmp = multiHeader + samples;
//        assertEquals(sample_tmp, arpSample.getARPSample());

    }



}
