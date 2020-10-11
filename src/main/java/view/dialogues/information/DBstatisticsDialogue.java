package view.dialogues.information;

import com.mashape.unirest.http.exceptions.UnirestException;
import database.DatabaseQueryHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import view.dialogues.settings.ATabpaneDialogue;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class DBstatisticsDialogue{

    private final GridPane dialogGrid;
    private final DatabaseQueryHandler databaseQueryHandler;
    private final Text header;
    private Hyperlink link;

    public DBstatisticsDialogue(DatabaseQueryHandler db_controller, String mitobench_version){

        this.databaseQueryHandler = db_controller;

        dialogGrid = new GridPane();
        dialogGrid.setAlignment(Pos.CENTER);
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);

        link = new Hyperlink();
        link.setText("Publications");


        header = new Text();
        header.setText("Database content statistics");
        header.setFont(Font.font ("Verdana", FontWeight.BOLD, 20));
        header.setFill(Color.BLACK);

        if(connectionPossible())
            databaseQueryHandler.calculateDBstats();
        addComponents(mitobench_version);
        addListener();

    }

    private boolean connectionPossible() {
        return databaseQueryHandler.connecting();
    }


    private void addComponents(String mitobench_version) {

        int row=0;

        dialogGrid.add(header,0,row,3,1);
        dialogGrid.add(new Label("mitoBench version " + mitobench_version + " | " + java.time.LocalDate.now()),0,++row,3,1);

        dialogGrid.add(new Separator(),0,++row,3,1);

        dialogGrid.add(new Label(" Samples (total)"),0,++row,1,1);
        dialogGrid.add(new Label(String.valueOf(databaseQueryHandler.getNumber_of_samples())),2,row,1,1);

        dialogGrid.add(new Label(" Samples (modern)"),0,++row,1,1);
        dialogGrid.add(new Label(String.valueOf(databaseQueryHandler.getNumber_of_modern_samples())),2,row,1,1);

        dialogGrid.add(new Label(" Samples (ancient)"),0,++row,1,1);
        dialogGrid.add(new Label(String.valueOf(databaseQueryHandler.getNumber_of_ancient_samples())),2,row,1,1);

        dialogGrid.add(new Separator(),0,++row,3,1);

        dialogGrid.add(new Label(" Continents"),0,++row,1,1);
        dialogGrid.add(new Label(String.valueOf(databaseQueryHandler.getNumber_of_continents())),2,row,1,1);

        dialogGrid.add(new Separator(),0,++row,3,1);

        dialogGrid.add(new Label(" Countries"),0,++row,1,1);
        dialogGrid.add(new Label(String.valueOf(databaseQueryHandler.getNumber_of_countries_covered())),2,row,1,1);

        dialogGrid.add(new Separator(),0,++row,3,1);

        dialogGrid.add(link,0,++row,1,1);
        dialogGrid.add(new Label(String.valueOf(databaseQueryHandler.getNumber_of_publications())),2,row,1,1);

        dialogGrid.add(new Separator(),0,++row,3,1);

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
