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
import view.charts.BarPlotTest;
import view.table.*;
import view.tree.TreeHaploChooser;

import java.util.Arrays;


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
        root.autosize();

        root.setTop(getMenu());
        root.setRight(getRightHBox());
        root.setCenter(getCenterPane());





        Scene scene = new Scene(root, 1200, 600);
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

        /*
                        IMPORT DIALOGUE

         */

        MenuItem importFile = new MenuItem("Import file");
        importFile.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                ImportDialogue importDialogue = new ImportDialogue();
                importDialogue.start(new Stage());
                // read file, parse to table
                CSVReader csvReader = new CSVReader();
                csvReader.populateTable(tableManager, importDialogue.getInputCSVFile(), false);

            }
        });




        /*
                        EXPORT DIALOGUE

         */

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


        /*

                EXIT OPTION


         */
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.exit(0);
            }
        });

        menuFile.getItems().addAll(importFile, exportFile, new SeparatorMenuItem(), exit);

        return menuBar;
    }


    /**
     *              Plotting and Statistics part
     * @return
     */
    private HBox getRightHBox()
    {
        HBox hbox = new HBox();

        VBox vbox = new VBox(50);
        vbox.setPadding(new Insets(0, 20, 0, 20));
        vbox.setAlignment(Pos.CENTER);

        Pane plot = new Pane();
        BarPlotTest barchart = new BarPlotTest("Country Summary", "Country", "Value");

        barchart.addData("data1", Arrays.asList( new double[][]{{1, 45263.37}, {2, 117320.16}, {3, 14845.27}}));

        plot.getChildren().addAll(barchart.getBarChart());
        vbox.getChildren().addAll(plot,new Label("Place for some statistics"));

        Separator separator1 = new Separator();
        vbox.getChildren().add(1, separator1);
        hbox.getChildren().addAll(new Separator(Orientation.VERTICAL), vbox);


        return hbox;
    }




    /**
     *              TABLE with Tree view
     * @return
     */
    private StackPane getCenterPane()
    {
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.BASELINE_LEFT);

        tableManager = new TableManager(new Label("\nOwn Mt database"));
        tableManager.addColumn("ID");
        tableManager.addColumn("MTsequence");
        tableManager.addColumn("Dating");
        tableManager.addColumn("Haplogroup");


        // fill table with content
        tableManager.addEntry(new TableDataModel(new String[]{"1", "AAGGC...", "1804", "N"}));
        tableManager.addEntry(new TableDataModel(new String[]{"2", "AAGGC...", "1804", "N"}));
        tableManager.addEntry(new TableDataModel(new String[]{"3", "AAGGC...", "1804", "H"}));
        tableManager.addEntry(new TableDataModel(new String[]{"4", "AAGGC...", "1804", "H"}));

        tableManager.copyData();

        final VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(tableManager.getLabel(), tableManager.getTable());

        stackPane.getChildren().addAll(vbox);

        TreeHaploChooser treeHaploChooser = new TreeHaploChooser(stackPane, tableManager);

        // add reset table button
        Button reset = new Button("Reset table");

        // reset table to state before selection
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent paramT) {
                tableManager.resetTable();
            }
        });
        stackPane.getChildren().add(reset);
        stackPane.setAlignment(Pos.TOP_RIGHT);
        StackPane.setMargin(reset, new Insets(10, 10, 0, 0));

        return stackPane;
    }

}
