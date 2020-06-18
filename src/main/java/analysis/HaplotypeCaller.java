package analysis;


import Logging.LogClass;
import genepi.haplogrep.main.Haplogrep;
import htsjdk.samtools.SAMException;
import io.reader.HSDParser;
import io.writer.MultiFastaWriter;
import org.apache.log4j.Logger;
import model.MTStorage;
import controller.TableControllerUserBench;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


/**
 * Created by neukamm on 17.05.17.
 */
public class HaplotypeCaller {
    private TableControllerUserBench tableController;
    private MTStorage mtStorage;
    private Logger LOG;
    private String phylotreeVersion = "17";
    private String haplogrepversion = "2.2.5";


    public HaplotypeCaller(TableControllerUserBench tableControllerUserBench, LogClass logClass){

        this.mtStorage = tableControllerUserBench.getDataTable().getMtStorage();
        this.tableController = tableControllerUserBench;
        LOG = logClass.getLogger(this.getClass());

    }


    /**
     * Run HaploGrep2.
     *
     * @param lineage   Lineage parameter. Can be true or false (default = false). Creates dot file ('tree').
     */
    public void call(String lineage) throws IOException {
        String file = "multifasta.fasta";
        //System.out.println("Writing fasta sequences to " + file);
        //System.out.println(file);

        // generate fasta file with all sequences for which haplogroups have to be determined
        MultiFastaWriter multiFastaWriter = new MultiFastaWriter(this.mtStorage, tableController.getSelectedRows(), true);
        multiFastaWriter.writeData(file, tableController);

        // todo: error handling when task cancelled

        String[] command = new String[] {
                "classify",
                "--format", "fasta",
                "--in",file,
                "--out", "haplogroups.hsd",
                "--extend-report",
                lineage};

        try {
            Haplogrep haplogrep = new Haplogrep(command);
            haplogrep.start();

            LOG.info("Calculate Haplogroups with Phylotree version " + phylotreeVersion + " and haplogrep-" + haplogrepversion);
            System.out.println("Haplogrep finished");
        } catch (SAMException e){
            System.err.println("Task cancelled");
        }
    }


    /**
     * Parse HaploGrep2 output file and update data.
     */
    public void update() {

        HSDParser hsdInput;

        try {
            if(new File("haplogroups.hsd").exists()){
                hsdInput = new HSDParser("haplogroups.hsd", LOG);
                tableController.updateTable(hsdInput.getCorrespondingData());
            }    else {
                LOG.info("HaploGrep2 did not run properly, 'haplogroups.hsd' does not exists.");
            }
        } catch (Exception e) {
            System.out.println("HaploGrep2 did not run properly, 'haplogroups.hsd' does not exists.");
            e.printStackTrace();
        }

    }

    /**
     * Delete all temporary files that were created while running HaploGrep2
     */
    public void deleteTmpFiles() {

        try {
            if (Files.exists(new File("haplogroups.hsd").toPath()))
                Files.delete(new File("haplogroups.hsd").toPath());

            if (Files.exists(new File("multifasta.fasta").toPath()))
                Files.delete(new File("multifasta.fasta").toPath());

            if (Files.exists(new File("haplogroups.hsd_lineage.txt").toPath()))
                Files.delete(new File("haplogroups.hsd_lineage.txt").toPath());


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
