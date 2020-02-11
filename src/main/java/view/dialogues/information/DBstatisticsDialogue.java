package view.dialogues.information;

import database.DatabaseQueryHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class DBstatisticsDialogue {

    private final GridPane dialogGrid;
    private final DatabaseQueryHandler databaseQueryHandler;
    private Hyperlink link;

    public DBstatisticsDialogue(DatabaseQueryHandler db_controller){

        this.databaseQueryHandler = db_controller;

        dialogGrid = new GridPane();
        dialogGrid.setAlignment(Pos.CENTER);
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);

        link = new Hyperlink();
        link.setText("Publications");

        databaseQueryHandler.calculateDBstats();
        addComponents();
        addListener();

    }




    private void addComponents() {

        int row=0;

        dialogGrid.add(new Label("Samples"),0,row,1,1);
        dialogGrid.add(new Label(String.valueOf(databaseQueryHandler.getNumber_of_samples())),1,row,1,1);

        dialogGrid.add(new Label("Countries"),0,++row,1,1);
        dialogGrid.add(new Label(String.valueOf(databaseQueryHandler.getNumber_of_countries_covered())),1,row,1,1);

        dialogGrid.add(link,0,++row,1,1);
        dialogGrid.add(new Label(String.valueOf(databaseQueryHandler.getNumber_of_publications())),1,row,1,1);

    }


    private void addListener() {

        link.setOnAction(event -> new Thread(() -> {
            try {
                Desktop.getDesktop().browse(new URI("https://docs.google.com/spreadsheets/d/1sNki9nNWoH8xf_6fJGzFSBKC9pPsmn7cSsRboGQwSxY/edit?usp=sharing"));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        }).start());
    }

    public GridPane getDialogGrid() {
        return dialogGrid;
    }
}
