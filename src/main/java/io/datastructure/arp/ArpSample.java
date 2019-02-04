package io.datastructure.arp;

/**
 * Created by peltzer on 30/11/2016.
 */

import io.datastructure.fastA.FastaEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by peltzer on 14/02/16.
 */
public class ArpSample {
    private ArrayList<FastaEntry> data;
    private HashMap<String,String> regions;
    private HashMap<String,String> storagetmp;
    private HashMap<String, Integer> storagecount;
    private int groups;


    public ArpSample( ArrayList<FastaEntry> data, HashMap<String,String> regions, int groups){
        this.data = data;
        this.regions = regions;
        this.groups = groups;
        storagetmp = new HashMap<>();
        storagecount = new HashMap<>();
    }




    public String getARPSample(){

        String multiHeader =  "[Data]\n"
                + "[[Samples]]\n\n\n";


        for (FastaEntry fa: data){
            String head = fa.getHeader();
            String seq = fa.getSequence();
            if(regions.containsKey(head)){
                String groupID = regions.get(head);
                String tmp = "";
                if(storagetmp.containsKey(groupID)){
                    tmp = storagetmp.get(groupID);
                }

                if(storagecount.containsKey(groupID)){
                    storagecount.put(groupID,storagecount.get(groupID)+1);
                } else {
                    storagecount.put(groupID,1);
                }
                storagetmp.put(groupID,tmp+head+"\t"+"1"+"\t"+seq+"\n");
            }
        }

        //Now put everything together as it should be
        String samples = "";
        Iterator iter = storagetmp.keySet().iterator();
        while ((iter.hasNext())) {
            String entry = (String) iter.next();
            samples = samples + "SampleName=\""+entry+"\""+"\n" +
                    "SampleSize="+storagecount.get(entry)+"\n"
                    + "SampleData = {\n" + storagetmp.get(entry) + "}\n";
        }


        return multiHeader + samples;
    }


}