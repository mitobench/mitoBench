package io.datastructure.fastA;

/**
 * Created by peltzer on 17/11/2016.
 */
public class FastaEntry {
    private String header;
    private String sequence;


    public FastaEntry(String sequence, String header) {
        this.sequence = sequence;
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String seq) {
        this.sequence = seq;
    }
}