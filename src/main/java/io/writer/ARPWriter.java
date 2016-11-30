package io.writer;

import io.IOutputData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by peltzer on 30/11/2016.
 */
public class ARPWriter implements IOutputData {
    private FileWriter fileWriter;
    private BufferedWriter bfWriter;
    private String groups = "";


    @Override
    public void writeData(String file) throws IOException {
        //Initialize properly
        if (!file.endsWith("arp")) {
            file = file + ".arp";
        }

        fileWriter = new FileWriter(new File(file));
        bfWriter = new BufferedWriter(fileWriter);

        //Write profile first

        /* TODO
         ArpProfile arpprofile = new ArpProfile(samplenames,String.valueOf(groups.size()),"DNA","N");
        bfwr.write(arpprofile.getProfile());
        ARPSample arpsample = new ARPSample(fastaEntries, this.regions, groups.size());
        bfwr.write(arpsample.getARPSample());
        ArpStructure arpstructure = new ArpStructure(groups.size(), this.groups);
        bfwr.write(arpstructure.getArpStructure());
        bfwr.flush();
        bfwr.close();
         */


    }

    @Override
    public void setGroups(String groupID) {
        this.groups = groupID;
    }
}
