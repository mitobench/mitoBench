package io.writer;

import io.Exceptions.ImageException;
import io.dialogues.Export.SaveAsDialogue;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Created by neukamm on 13.12.16.
 */
public class ImageWriter {

    public ImageWriter(){
    }


    /**
     * write image to png
     * @param stage
     * @param image
//     */
    public void saveImage(Stage stage, WritableImage image) throws ImageException {

        FileChooser.ExtensionFilter fex = new FileChooser.ExtensionFilter("Image png format (*.png)", "*.png");
        SaveAsDialogue saveAsDialogue = new SaveAsDialogue(fex);
        saveAsDialogue.start(new Stage());
        String outfile = "";
        if (saveAsDialogue.getOutFile() != null) {
            outfile = saveAsDialogue.getOutFile();
            //Initialize filepath properly
            if (!outfile.endsWith("png"))
                outfile =outfile+ ".png";

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File(outfile));
            } catch (IOException e) {
                throw new ImageException("Image cannot be saved.");
            }

        }
    }
}
