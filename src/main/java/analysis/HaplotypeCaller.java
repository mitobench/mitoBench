package analysis;


import Logging.LogClass;
import io.Exceptions.HSDException;
import io.reader.HSDInput;
import io.writer.MultiFastaWriter;
import org.apache.log4j.Logger;
import org.json.*;
import view.table.MTStorage;
import controller.TableControllerUserBench;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;


/**
 * Created by neukamm on 17.05.17.
 */
public class HaplotypeCaller {
    private TableControllerUserBench tableController;
    private MTStorage mtStorage;
    private Logger LOG;
    private String phylotreeVersion = "17";


    public HaplotypeCaller(TableControllerUserBench tableControllerUserBench, LogClass logClass){
        this.mtStorage = tableControllerUserBench.getDataTable().getMtStorage();
        this.tableController = tableControllerUserBench;
        LOG = logClass.getLogger(this.getClass());

    }

    public void call(String lineage) throws IOException, InterruptedException {
        String file = "multifasta.fasta";
        System.out.println(file);

        // generate fasta file with all sequences for which haplogroups have to be determined
        MultiFastaWriter multiFastaWriter = new MultiFastaWriter(this.mtStorage, tableController.getSelectedRows());
        multiFastaWriter.writeData(file, tableController);

        start(file, lineage);

    }


    public void update() {

        //HSDReaderIntern hsd_reader = new HSDReaderIntern("haplogroups.hsd", LOG, "Haplogroup Phylotree17", "Haplotype Phlyotree17");
        HSDInput hsdInput = null;
        try {
            hsdInput = new HSDInput("haplogroups.hsd", LOG);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (HSDException e) {
            e.printStackTrace();
        }
        tableController.updateTable(hsdInput.getCorrespondingData());



    }

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


    private void start(String f, String linegae) throws IOException, InterruptedException {

        URL url = this.getClass().getResource("/jars/haplogrep-2.1.18.jar");
        String dirpath = url.getPath();
        String[] command = new String[] { "java", "-jar", dirpath,
                "--format", "fasta",
                "--in",f,
                "--out", "haplogroups.hsd",
                linegae};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        process.waitFor();
//
        while (process.isAlive()){
            // wait until haplogroups are calculated
        }
        System.out.println("Haplogroups are determined");

        //delete temporary fasta file
        Files.delete(new File(f).toPath());

        LOG.info("Calculate Haplogroups with Phylotree version " + phylotreeVersion + " and haplogrep-2.1.18");

    }
}
