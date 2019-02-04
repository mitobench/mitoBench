package view.visualizations;

import Logging.LogClass;
import graphvizapi.GraphViz;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SampleTree  extends AChart {

    private Image img;

    public SampleTree(String lable_xaxis, String label_yaxis, LogClass logClass) {
        super(lable_xaxis, label_yaxis, logClass);

        //setContextMenu();
    }


    @Override
    protected void layoutChartChildren(double v, double v1, double v2, double v3) {

    }

    public void start() throws IOException {

        String input = "haplogroups.hsd.dot";

        GraphViz gv = new GraphViz();
        gv.readSource(input);
        System.out.println(gv.getDotSource());

        //String type = "gif";
        //    String type = "dot";
        //    String type = "fig";    // open with xfig
        //    String type = "pdf";
        //    String type = "ps";
        //    String type = "svg";    // open with inkscape
        String type = "png";
        //      String type = "plain";


        String repesentationType= "dot";
        //		String repesentationType= "neato";
        //		String repesentationType= "fdp";
        //		String repesentationType= "sfdp";
        // 		String repesentationType= "twopi";
        //		String repesentationType= "circo";

        //File out = new File("samples_tree." + type);   // Linux
        //gv.writeGraphToFile( gv.getGraph(gv.getDotSource(), type, repesentationType), out );
        byte[] tree_img = gv.getGraph(gv.getDotSource(), type, repesentationType);
        img = new Image(new ByteArrayInputStream(tree_img));




    }

    public Image getImg() {
        return img;
    }
}
