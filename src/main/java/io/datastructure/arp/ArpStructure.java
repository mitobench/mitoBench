package io.datastructure.arp;

/**
 * Created by peltzer on 30/11/2016.
 */

import java.util.HashMap;
import java.util.Iterator;

public class ArpStructure {
    private int groups;
    private HashMap<String,Integer> groupnames;

    public ArpStructure(int groups, HashMap<String,Integer> groupnames){
        this.groups = groups;
        this.groupnames = groupnames;

    }

    public String getArpStructure(){
        String tmp =
                "\n\n"+"[[Structure]]" + "\n\n"
                        + "StructureName=\"New Edited Structure\"\n"
                        + "NbGroups="+groups+"\n\n\n";
        Iterator iter = groupnames.keySet().iterator();
        while(iter.hasNext()){
            String name = (String) iter.next();
            tmp+="Group={\n"+"\""+ name+"\""+ "\n" +"}" + "\n\n";
        }



        return tmp;

    }
}