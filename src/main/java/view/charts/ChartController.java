package view.charts;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.chart.BarChart;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Created by neukamm on 08.12.16.
 */
public class ChartController {

    public ChartController(){

    }


    public void addMouseListener(BarChart chart){
        chart.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                MouseButton button = event.getButton();
                if(button==MouseButton.SECONDARY) {
                    System.out.println("SECONDARY button clicked on button");
                }

            }
        });


    }


}
