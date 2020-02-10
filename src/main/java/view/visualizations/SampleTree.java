package view.visualizations;

import Logging.LogClass;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import view.dialogues.error.SampleTreeError;

import java.io.File;
import java.io.IOException;

public class SampleTree  extends AChart {
;
    private Graphviz viz;

    public SampleTree(String lable_xaxis, String label_yaxis, LogClass logClass) {
        super(lable_xaxis, label_yaxis, logClass);
    }


    @Override
    protected void layoutChartChildren(double v, double v1, double v2, double v3) {

    }

    public void start(String imgFile) throws IOException {

        String input = "haplogroups.hsd.dot";
        System.out.println("Start reading/parsing dot file");
        MutableGraph g = Parser.read(new File(input));
        System.out.println("Start creating graph");
        viz = Graphviz.fromGraph(g);
        System.out.println("Start rendering graph ");

        try{
            viz.render(Format.SVG).toFile(new File(imgFile));
            System.out.println("Finished rendering graph");
        } catch (Exception e){

            new SampleTreeError(
                    e,
                    "The svg cannot be created. Please use the dot file (" + System.getProperty("user.dir")
                    +"/haplogroups.hsd.dot) to visualize the tree with a tool of your choice");

        }


    }

}
