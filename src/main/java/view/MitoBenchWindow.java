package view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import view.charts.BarPlot;
import view.table.*;

import java.io.File;
import java.io.IOException;


/**
 * Created by neukamm on 03.11.16.
 */
public class MitoBenchWindow extends Application{

    private BorderPane root;
    private TableManager tableManager;


    @Override
    public void start(Stage primaryStage) throws Exception
    {
        root = new BorderPane();
        root.setTop(getMenu());
        root.setRight(getRightHBox());
        root.setCenter(getCenterPane());

        Scene scene = new Scene(root, 900, 500);
        primaryStage.setTitle("Mito Bench");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    private MenuBar getMenu() throws Exception
    {
        MenuBar menuBar = new MenuBar();

        Menu menuFile = new Menu("File");
        Menu menuEdit = new Menu("Edit");
        Menu menuStatistics = new Menu("Statistics");
        Menu menuHelp = new Menu("Help");
        menuBar.getMenus().addAll(menuFile, menuEdit, menuStatistics, menuHelp);

        MenuItem importFile = new MenuItem("Import file");
        importFile.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                ImportDialogue importDialogue = new ImportDialogue();
                importDialogue.start(new Stage());
            }
        });



        MenuItem exportFile = new MenuItem("Export DB file");
        exportFile.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                ExportDialogue exportDialogue = new ExportDialogue();
                exportDialogue.start(new Stage());
                String outFileDB = exportDialogue.getOutFile();

                try{
                    CSVWriter csvWriter = new CSVWriter(tableManager.getData());
                    csvWriter.writeExcel(outFileDB);
                } catch (Exception e) {
                    System.err.println("Caught Exception: " + e.getMessage());
                }


            }
        });

        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.exit(0);
            }
        });

        menuFile.getItems().addAll(importFile, exportFile, new SeparatorMenuItem(), exit);

        return menuBar;
    }

    private HBox getRightHBox()
    {
        HBox hbox = new HBox();

        VBox vbox = new VBox(50);
        vbox.setPadding(new Insets(0, 20, 0, 20));
        vbox.setAlignment(Pos.CENTER);

        Pane plot = new Pane();
        BarPlot barchart = new BarPlot("Country Summary", "Country", "Value");

        plot.getChildren().addAll(barchart.getBarChart());
        vbox.getChildren().addAll(plot,new Label("Place for some statistics"));

        Separator separator1 = new Separator();
        vbox.getChildren().add(1, separator1);
        hbox.getChildren().addAll(new Separator(Orientation.VERTICAL), vbox);


        return hbox;
    }


    private StackPane getCenterPane()
    {
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER);

        tableManager = new TableManager(new Label("Mt database selection"));
        tableManager.addColumn("ID");
        tableManager.addColumn("MTsequence");
        tableManager.addColumn("dating");

        // fill table with content
        tableManager.addEntry(new TableDataModel("1", "AAGGCTGATA", "1804"));
        tableManager.addEntry(new TableDataModel("2", "AAGGCTGATA", "1803"));
        tableManager.addEntry(new TableDataModel("3", "AAGGCTGATA", "1806"));
        tableManager.addEntry(new TableDataModel("4", "AAGGCTGATA", "1810"));



        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(tableManager.getLabel(), tableManager.getTable());

        stackPane.getChildren().addAll(vbox);

        return stackPane;
    }



}
