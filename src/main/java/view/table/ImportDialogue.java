package view.table;/**
 * Created by neukamm on 07.11.16.
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ImportDialogue extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }


    @Override
    public void start(Stage primaryStage) {


        // dialogue to import csv files (to fill database)
        primaryStage.setTitle("importFile");
        Label label =new Label("A File Chooser");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a File");

        //to create an extensionFilter for ".csv" and add it to the fileChooser.
        FileChooser.ExtensionFilter extensionFilter=new FileChooser.ExtensionFilter("CSV", "*.csv");
        fileChooser.getExtensionFilters().add(extensionFilter);

        //for showing the popup window to open file
        File file=fileChooser.showOpenDialog(primaryStage);
        label.setText("You have opend a file "+file);
        BorderPane root = new BorderPane();
        root.setCenter(label);
        Scene scene = new Scene(root, 100, 50);
        primaryStage.setScene(scene);
        primaryStage.show();
    }



}
