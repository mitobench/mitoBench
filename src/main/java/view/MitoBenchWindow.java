package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.xml.sax.SAXException;
import view.charts.BarPlotHaplo;
import view.charts.StackedBar;
import view.menus.*;
import view.table.*;
import view.tree.TreeHaploController;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


/**
 * Created by neukamm on 03.11.16.
 */
public class MitoBenchWindow extends Application{

    private BorderPane root;
    private TableController tableController;
    private Scene scene;
    private VBox vBox;


    @Override
    public void start(Stage primaryStage) throws Exception
    {


        root = new BorderPane();
        scene = new Scene(root, 1200, 600);

        // bind width and height to scene to enable resizing
        root.prefHeightProperty().bind(scene.heightProperty());
        root.prefWidthProperty().bind(scene.widthProperty());

        root.setCenter(getCenterPane());
        root.setTop(getMenuPane());



        primaryStage.setTitle("Mito Bench");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }



    private MenuBar getMenuPane() throws Exception
    {
        MenuBar menuBar = new MenuBar();

        FileMenu fileMenu = new FileMenu(tableController);
        EditMenu editMenu = new EditMenu();
        ToolsMenu toolsMenu = new ToolsMenu();
        TableMenu tableMenu = new TableMenu(tableController);
        GraphicsMenu graphicsMenu = new GraphicsMenu(tableController, vBox);
        HelpMenu helpMenu = new HelpMenu();

        menuBar.getMenus().addAll(fileMenu.getMenuFile(),
                                  editMenu.getMenuEdit() ,
                                  toolsMenu.getMenuTools(),
                                  tableMenu.getMenuTable(),
                                  graphicsMenu.getMenuGraphics(),
                                  helpMenu.getMenuHelp());

        return menuBar;
    }



    public SplitPane  getCenterPane() throws ParserConfigurationException, SAXException, IOException {


        //HBox center = new HBox(getTablePane(), getPlotPane());
        SplitPane center = new SplitPane();
        center.getItems().addAll(getTablePane(), getPlotPane());


        // bind center to scene --> resizing
        center.prefHeightProperty().bind(scene.heightProperty());
        center.prefWidthProperty().bind(scene.widthProperty());

        return center;

    }

    /**
     *
     *              Plotting and Statistics part
     *
     *
     * @return
     */
    private HBox getPlotPane()
    {
        HBox hbox = new HBox();
        vBox = new VBox();
        vBox.setPadding(new Insets(0, 20, 0, 20));

        vBox.prefHeightProperty().bind(scene.heightProperty());
        vBox.prefWidthProperty().bind(scene.widthProperty());

//        barPlotHaplo = new BarPlotHaplo("Haplogroup frequency", "Frequency", vbox);
//        barPlotHaplo.setDragAndMove();
//
//        Pane group = new Pane(barPlotHaplo.getBarChart());
//        StackPane zoomPane = barPlotHaplo.createZoomPane(group);


        //stackedBarPlot = new StackedBar("Haplogroup frequency per group");
        //stackedBarPlot.setDragAndMove();

        //Pane group = new Pane(barPlotHaplo.getBarChart());
        //StackPane zoomPane = barPlotHaplo.createZoomPane(group);

        //zoomPane.prefHeightProperty().bind(scene.heightProperty());
        //zoomPane.prefWidthProperty().bind(scene.widthProperty());

        //vbox.getChildren().addAll(zoomPane);
        //vbox.getChildren().addAll(stackedBarPlot.getSbc());
        //VBox.setVgrow(zoomPane, Priority.ALWAYS);
        //VBox.setVgrow(stackedBarPlot.getSbc(), Priority.ALWAYS);

        vBox.setAlignment(Pos.CENTER);

        Separator separator1 = new Separator();
        //vbox.getChildren().add(1, separator1);
        hbox.getChildren().addAll(vBox);

        return hbox;
    }


    /**
     *
     *              TABLE with Tree view
     *
     *
     * @return
     */
    private BorderPane getTablePane()throws IOException, SAXException, ParserConfigurationException
    {
        BorderPane stackPane = new BorderPane();

        // initialize columns
        tableController = new TableController(scene);

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(40, 10, 10, 10));
        vbox.prefHeightProperty().bind(scene.heightProperty());
        vbox.prefWidthProperty().bind(scene.widthProperty());
        vbox.getChildren().addAll(tableController.getTable());


        stackPane.setCenter(vbox);
        TreeHaploController treeHaploChooser = new TreeHaploController(stackPane, tableController);


        return stackPane;
    }

}
