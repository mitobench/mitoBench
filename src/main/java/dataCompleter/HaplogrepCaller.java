package dataCompleter;

import genepi.haplogrep.main.Haplogrep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HaplogrepCaller {

    /**
     * Run HaploGrep2
     *
     * @param f Fasta file
     * @throws IOException
     * @throws InterruptedException
     */
    public void call(String f) {

        String[] command = new String[] {
                "--format", "fasta",
                "--in",f,
                "--out", "haplogroups.hsd",
                "--extend-report"
        };

        try{
            Haplogrep haplogrep = new Haplogrep(command);
            haplogrep.start();
        } catch (Exception e) {
            System.out.println("Running HaploGrep2 was not successful.");
            System.exit(0);
        }
    }


    /**
     * Delete all temporary files that were created while running haploGrep2.
     */
    public void deleteTmpFiles() {

        try {
            if (Files.exists(new File("haplogroups.hsd").toPath()))
                Files.delete(new File("haplogroups.hsd").toPath());


            if (Files.exists(new File("haplogroups.hsd_lineage.txt").toPath()))
                Files.delete(new File("haplogroups.hsd_lineage.txt").toPath());


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
