package io.datastructure.arp;

/**
 * Created by peltzer on 30/11/2016.
 */

public class ArpProfile  {
    private String name;
    private String numberofsamples;
    private String datatype;
    private String missingdata;
    /**
     * [Profile]
     Title="Three Databases 16126-16369"
     NbSamples=38
     GenotypicData=0
     DataType=DNA
     LocusSeparator=NONE
     MissingData="N"
     */

    public ArpProfile(String name, String numberofSamples, String datatype, String missingdata) {
        this.name = name;
        this.numberofsamples = numberofSamples;
        this.datatype = datatype;
        this.missingdata = missingdata;
    }

    public String getProfile(){
        return

                "[Profile]" + "\n\n\n" +
                        "Title=\"" + name + "\"" + "\n" +
                        "NbSamples="+numberofsamples+"\n"+
                        "GenotypicData=0\n"+
                        "DataType="+datatype+"\n"+
                        "LocusSeparator=NONE\n"+
                        "MissingData=\""+missingdata+"\"\n\n";

    }
}