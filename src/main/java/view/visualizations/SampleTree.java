package view.visualizations;

import Logging.LogClass;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import org.apache.log4j.Logger;
import view.dialogues.error.SampleTreeError;

import java.io.File;
import java.io.IOException;

public class SampleTree extends AChart {
    private final Logger log;
    private Graphviz viz;

    public SampleTree(String lable_xaxis, String label_yaxis, LogClass logClass) {
        super(lable_xaxis, label_yaxis, logClass);

        log = lc.getLogger(this.getClass());
    }


    @Override
    protected void layoutChartChildren(double v, double v1, double v2, double v3) {

    }

    /**
     * Generate visualization of tree (in dot format) using the Graphviz library.
     * The tree is saved as svg file, together with the dot file.
     *
     * @param imgFile
     * @param outfolder
     * @throws IOException
     */
    public void start(String imgFile, String outfolder) throws IOException {

        String input = outfolder + File.separator + "haplogroups.hsd.dot";
        log.info("Start reading/parsing dot file");
        MutableGraph g = Parser.read(new File(input));
        log.info("Start creating graph");
        viz = Graphviz.fromGraph(g);
        log.info("Start rendering graph ");

        try{
            viz.totalMemory(24000000).render(Format.SVG).toFile(new File(imgFile));
            log.info("Finished rendering graph");
        } catch (Exception e){
            new SampleTreeError(
                    e,
                    "The svg cannot be created. Please use the dot file (" + System.getProperty("user.dir")
                    +"/haplogroups.hsd.dot) to visualize the tree with a tool of your choice");
        }
    }
}
