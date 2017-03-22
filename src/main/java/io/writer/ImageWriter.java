package io.writer;

import Logging.LogClass;
import io.Exceptions.ImageException;
import io.dialogues.Export.SaveAsDialogue;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Created by neukamm on 13.12.16.
 */
public class ImageWriter {

    private final Logger LOG;


    public ImageWriter(LogClass logClass){
        LogClass lc = logClass;
        LOG = lc.getLogger(this.getClass());
    }


    /**
     * write image to png
     * @param node
//     */
    public void saveImage(Node node) throws ImageException {

        int scale = 6; //6x resolution should be enough, users should downscale if required
        final SnapshotParameters spa = new SnapshotParameters();
        spa.setTransform(javafx.scene.transform.Transform.scale(scale, scale));

        FileChooser.ExtensionFilter fex = new FileChooser.ExtensionFilter("Image format (*.png)", "*.png");
        SaveAsDialogue saveAsDialogue = new SaveAsDialogue(fex);
        saveAsDialogue.start(new Stage());
        String outfile;
        if (saveAsDialogue.getOutFile() != null) {
            outfile = saveAsDialogue.getOutFile();
            //Initialize filepath properly
            if (!outfile.endsWith("png"))
                outfile = outfile+ ".png";
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(node.snapshot(spa, null), null), "png", new File(outfile));
                LOG.info("Save image: " + outfile);
            } catch (IOException e) {
                throw new ImageException("Image cannot be saved.");
            }

        }
    }
}
