package view.visualizations;

import Logging.LogClass;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
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

    public void start() throws IOException {

        String input = "haplogroups.hsd.dot";
        MutableGraph g = Parser.read(new File(input));
        viz = Graphviz.fromGraph(g);

        image = viz.render(Format.PNG).toImage();

    }

    public Image getImg() {
        //return img;
        return SwingFXUtils.toFXImage(this.image, null);
    }

    public Graphviz getViz() {
        return viz;
    }
}
