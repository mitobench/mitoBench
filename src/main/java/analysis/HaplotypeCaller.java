package analysis;


import Logging.LogClass;
import io.Exceptions.HSDException;
import io.datastructure.Entry;
import io.datastructure.generic.GenericInputData;
import io.inputtypes.CategoricInputType;
import io.reader.HSDInput;
import io.reader.HSDReaderIntern;
import io.writer.MultiFastaWriter;
import org.apache.log4j.Logger;
import org.json.*;
import view.table.MTStorage;
import controller.TableControllerUserBench;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by neukamm on 17.05.17.
 */
public class HaplotypeCaller {
    private TableControllerUserBench tableController;
    private MTStorage mtStorage;
    private Logger LOG;


    public HaplotypeCaller(TableControllerUserBench tableControllerUserBench, LogClass logClass){
        this.mtStorage = tableControllerUserBench.getDataTable().getMtStorage();
        this.tableController = tableControllerUserBench;
        LOG = logClass.getLogger(this.getClass());

    }

    public void call() throws JSONException, IOException, InterruptedException {

        String file = "multifasta.fa";

        // generate fasta file with all sequences for which haplogroups have to be determined
        MultiFastaWriter multiFastaWriter = new MultiFastaWriter(this.mtStorage, tableController.getSelectedRows());
        multiFastaWriter.writeData(file, tableController);

        start(file);

        update();


    }


    private void update() {
        try {
            HSDReaderIntern hsd_reader = new HSDReaderIntern("haplogroups.hsd", LOG, "Haplogroup Phylotree17", "Haplotype Phlyotree17");
            tableController.updateTable(hsd_reader.getCorrespondingData());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (HSDException e) {
            e.printStackTrace();
        }


    }


    private void start(String f) throws IOException, InterruptedException {

        String dirpath = System.getProperty("user.dir") +  "/jar/haplogrep-2.2-beta.jar";
        System.out.println(dirpath);
        //String dirpath = this.getClass().getResource("/jar/haplogrep-2.1.1.jar").toExternalForm();
        String haplogrep2_jar = dirpath.split(":")[1];
        String[] command = new String[] { "java", "-jar", haplogrep2_jar,
                "--format fasta",
                "--in",f,
                "--out", "haplogroups.hsd",
                "--phylotree","17"};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        process.waitFor();

        System.out.println("Haplogroups are determined");

        // parse and delete temporary files
        Files.delete(new File(f).toPath());

        LOG.info("Calculate Haplogroups.");

    }
}
