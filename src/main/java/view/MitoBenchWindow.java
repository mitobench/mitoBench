package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.xml.sax.SAXException;
import view.menus.*;
import view.table.TableController;
import view.tree.TreeHaploController;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


/**
 * Created by neukamm on 03.11.16.
 */
public class MitoBenchWindow extends Application{

    private final String MITOBENCH_VERSION = "0.1";

    private BorderPane root;
    private TableController tableController;
    private Scene scene;
    private TreeHaploController treeController;
    private TabPane tabPane;
    private Stage primaryStage;
    private TabPane statsTabpane;


    @Override
    public void start(Stage primaryStage) throws Exception
    {


        root = new BorderPane();
        root.setId("mainBorderPane");
        scene = new Scene(root, 1200, 600);

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Mito Bench");
        this.primaryStage.setScene(scene);
        this.primaryStage.setResizable(true);

        // bind width and height to scene to enable resizing
        root.prefHeightProperty().bind(scene.heightProperty());
        root.prefWidthProperty().bind(scene.widthProperty());

        root.setCenter(getCenterPane());
        root.setTop(getMenuPane());



        this.primaryStage.show();

    }


    private MenuBar getMenuPane() throws Exception
    {
        MenuBar menuBar = new MenuBar();
        menuBar.setId("menuBar");


        EditMenu editMenu = new EditMenu();
        ToolsMenu toolsMenu = new ToolsMenu(tableController, treeController, statsTabpane, scene);
        FileMenu fileMenu = new FileMenu(tableController, MITOBENCH_VERSION, primaryStage, toolsMenu);
        TableMenu tableMenu = new TableMenu(tableController);
        GraphicsMenu graphicsMenu = new GraphicsMenu(tableController, tabPane, treeController, primaryStage, scene);
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


        SplitPane center = new SplitPane();
        center.setOrientation(Orientation.VERTICAL);
        center.getItems().addAll(getRightSplitPane(), getLeftSplitPane());


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
    private TabPane getPlotPane()
    {
        tabPane = new TabPane();
        tabPane.prefHeightProperty().bind(scene.heightProperty());
        tabPane.prefWidthProperty().bind(scene.widthProperty());
        tabPane.setPadding(new Insets(10, 10, 10,10));

        return tabPane;
    }


    /**
     *
     *              TABLE with Tree view
     *
     *
     * @return
     */
    private BorderPane getLeftSplitPane() throws IOException, SAXException, ParserConfigurationException
    {
        BorderPane borderPane = new BorderPane();
        borderPane.setId("mainEntryTable");

        // initialize table
        tableController = new TableController(scene);

        VBox vbox_center = new VBox();
        vbox_center.setSpacing(10);
        vbox_center.setPadding(new Insets(10, 10, 10, 10));
        vbox_center.prefHeightProperty().bind(scene.heightProperty());
        vbox_center.prefWidthProperty().bind(scene.widthProperty());
        vbox_center.getChildren().addAll(tableController.getTable());
        borderPane.setCenter(vbox_center);

        // set haplotree - seach view
        Pane treepane = new Pane();

        treeController = new TreeHaploController(treepane, tableController);

        borderPane.setTop(treepane);

        return borderPane;
    }


    private SplitPane getRightSplitPane() {

        SplitPane plot_stats = new SplitPane();
        plot_stats.setOrientation(Orientation.HORIZONTAL);
        plot_stats.getItems().addAll(getPlotPane(), getStatsPane());

        return plot_stats;

    }

    private BorderPane getStatsPane(){
        BorderPane statsBorderPane = new BorderPane();
        statsBorderPane.prefHeightProperty().bind(scene.heightProperty());
        statsBorderPane.prefWidthProperty().bind(scene.widthProperty());
        statsBorderPane.setId("mainWindowLeftpart");

        statsTabpane = new TabPane();
        statsBorderPane.setCenter(statsTabpane);

        return statsBorderPane;
    }

}
