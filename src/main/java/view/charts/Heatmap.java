package view.charts;


import Logging.LogClass;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Random;

/**
 * Created by neukamm on 12.07.17.
 */
public class Heatmap extends AChart{


    private int width;
    private int length;
    private GridPane grid;

    public Heatmap(String lable_xaxis, String label_yaxis, LogClass logClass) {
        super(lable_xaxis, label_yaxis, logClass);
    }

    public void setSize(int width, int hight){
        length = hight;
        this.width = width;

    }

    public void start(Stage primaryStage) {

        grid = new GridPane();

        for(int y = 0; y < length; y++){
            for(int x = 0; x < width; x++){

                Random rand = new Random();
                int rand1 = rand.nextInt(2);

                // Create a new TextField in each Iteration
                TextField tf = new TextField();
                tf.setPrefHeight(50);
                tf.setPrefWidth(50);
                tf.setAlignment(Pos.CENTER);
                tf.setEditable(false);
                tf.setDisable(true);

                // Iterate the Index using the loops
                grid.setRowIndex(tf,y);
                grid.setColumnIndex(tf,x);
                grid.getChildren().add(tf);
            }
        }


    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public GridPane getGrid() {
        return grid;
    }

    public void setGrid(GridPane grid) {
        this.grid = grid;
    }
}
