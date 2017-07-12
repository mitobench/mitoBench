package analysis;


import Logging.LogClass;
import io.datastructure.Entry;
import io.datastructure.generic.GenericInputData;
import io.inputtypes.CategoricInputType;
import io.writer.MultiFastaWriter;
import org.apache.log4j.Logger;
import org.json.*;
import view.table.MTStorage;
import view.table.controller.TableControllerUserBench;

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


    public HaplotypeCaller(TableControllerUserBench tableControllerUserBench, MTStorage mtStorage, LogClass logClass){
        this.mtStorage = mtStorage;
        this.tableController = tableControllerUserBench;
        LOG = logClass.getLogger(this.getClass());

    }

    public void call() throws JSONException, IOException, InterruptedException {

        String file = "/home/neukamm/Desktop/testmultifasta.fa";

        // generate fasta file with all sequences where haplogroups have to be assigned
        MultiFastaWriter multiFastaWriter = new MultiFastaWriter(tableController, this.mtStorage);
        multiFastaWriter.writeData(file);

        //File f = new File(file);

        //start(file);
        //POST file
//        ClientResource cr = new ClientResource("https://haplogrep.uibk.ac.at/haplogrep-ws");
//        final FormDataSet fds = new FormDataSet();
//        fds.setMultipart(true);
//        final FormData fileRep = new FormData("importfile", new FileRepresentation(f, MediaType.APPLICATION_ALL));
//        fds.getEntries().add(fileRep);
//        cr.post(fds);
//
//        //Response
//        JSONArray jsonArray = new JSONArray(cr.getResponse().getEntityAsText());
//
//        // collect all ids and Haplogroups
//        List<String> ids = new ArrayList<>();
//        List<String> haplogroups = new ArrayList<>();
//        for (int i = 0; i < jsonArray.length(); i++) {
//            JSONObject object = jsonArray.getJSONObject(i);
//            String id = (String) object.get("id");
//            String hg = (String) object.get("haplogroup");
//            ids.add(id);
//            haplogroups.add(hg);
//            //Status status = cr.getResponse().getStatus();
//        }

        //update(ids, haplogroups);

    }


    private void update(List<String> ids, List<String> haplogroups) {
        tableController.addColumn("Haplogroup HaploGrep2/PhyloTree17",
                tableController.getCurrentColumnNames().size());

        HashMap<String, List<Entry>> map = new HashMap<>();

        for(int i = 0; i < ids.size(); i++){
            Entry entry = new Entry("Haplogroup HaploGrep2/PhyloTree17",
                                             new CategoricInputType("String"),
                                             new GenericInputData(haplogroups.get(i)));
            List<Entry> entries = new ArrayList<>();
            entries.add(entry);
            map.put(ids.get(i), entries);
        }

        tableController.updateTable(map);

    }


    private void start(String f) throws IOException, InterruptedException {

        String dirpath = System.getProperty("user.dir") +  "/jar/haplogrep-2.1.1.jar";
        System.out.println(dirpath);
        //String dirpath = this.getClass().getResource("/jar/haplogrep-2.1.1.jar").toExternalForm();
        String haplogrep2_jar = dirpath.split(":")[1];
        String[] command = new String[] { "java", "-jar", haplogrep2_jar,
                "--in",f,
                "--out","/home/neukamm/",
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
