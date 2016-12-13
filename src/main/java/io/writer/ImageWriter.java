package io.writer;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.charts.ChartController;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Created by neukamm on 13.12.16.
 */
public class ImageWriter {

    public ImageWriter(){}



    public void saveImage(Stage stage, WritableImage image){
        FileChooser fileChooser = new FileChooser();
        //Show save file dialog
        File file = fileChooser.showSaveDialog(stage);

        if(file != null){
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                // TODO: handle exception here
            }
        }
    }
}
