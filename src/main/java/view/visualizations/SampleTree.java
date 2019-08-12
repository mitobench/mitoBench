package view.visualizations;

import Logging.LogClass;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizV8Engine;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SampleTree  extends AChart {

    private BufferedImage image;
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
        //image = viz.render(Format.PNG).toImage();
        viz.render(Format.SVG).toFile(new File(imgFile));
        System.out.println("Finished rendering graph");

    }

    public Graphviz getViz() {
        return viz;
    }
}
