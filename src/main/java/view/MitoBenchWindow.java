package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.xml.sax.SAXException;
import view.menus.*;
import view.table.TableControllerDB;
import view.table.TableControllerUserBench;
import view.tree.TreeHaploController;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


/**
 * Created by neukamm on 03.11.16.
 */
public class MitoBenchWindow extends Application{

    private final String MITOBENCH_VERSION = "0.1";

    private BorderPane root;
    private TableControllerUserBench tableControllerUserBench;
    private TableControllerDB tableControllerDB;
    private Scene scene;
    private TreeHaploController treeController;
    private TabPane tabPane;
    private Stage primaryStage;
    private TabPane statsTabpane;
    private BorderPane tablePane;
    private VBox table_userBench;
    private VBox table_DB;


    @Override
    public void start(Stage primaryStage) throws Exception
    {

        root = new BorderPane();
        root.setId("mainBorderPane");
        scene = new Scene(root);

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Mito Bench");
        this.primaryStage.setScene(scene);
        this.primaryStage.setResizable(true);
        primaryStage.setMaximized(true);
        primaryStage.toFront();

        // bind width and height to scene to enable resizing
        root.prefHeightProperty().bind(scene.heightProperty());
        root.prefWidthProperty().bind(scene.widthProperty());

        // initialize table
        tableControllerUserBench = new TableControllerUserBench();
        //tableControllerUserBench.setScene(scene);
        tableControllerUserBench.init();
        tableControllerUserBench.setDragDrop();
        tableControllerUserBench.getTable().prefHeightProperty().bind(scene.heightProperty());
        tableControllerUserBench.getTable().prefWidthProperty().bind(scene.widthProperty());


        BorderPane center = new BorderPane();
        // tree view

        // set haplotree - search view
        Pane treepane = new Pane();

        treeController = new TreeHaploController(tableControllerUserBench);
        treeController.configureSearch(treepane);
        treeController.setAnimation();

        center.setTop(treepane);
        center.setCenter(getCenterPane());
        root.setCenter(center);

        root.setTop(getMenuPane());

        this.primaryStage.show();
    }


    private MenuBar getMenuPane() throws Exception
    {
        MenuBar menuBar = new MenuBar();
        menuBar.setId("menuBar");

        EditMenu editMenu = new EditMenu();
        StatisticsMenu toolsMenu = new StatisticsMenu(tableControllerUserBench, treeController, statsTabpane, scene);
        FileMenu fileMenu = new FileMenu(tableControllerUserBench, MITOBENCH_VERSION, primaryStage, toolsMenu, this, tableControllerDB);
        TableMenu tableMenu = new TableMenu(tableControllerUserBench);
        GraphicsMenu graphicsMenu = new GraphicsMenu(tableControllerUserBench, tabPane, treeController, primaryStage, scene, statsTabpane);
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
        center.getItems().addAll(configureVisAndStatsisticsPane(), configureTablePane());

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
    private TabPane configureVisPane()
    {
        tabPane = new TabPane();
        tabPane.prefHeightProperty().bind(scene.heightProperty());
        tabPane.prefWidthProperty().bind(scene.widthProperty());
        tabPane.setPadding(new Insets(10, 10, 10,10));

        return tabPane;
    }


    /**
     *  This method creates pane for table view.
     *
     *  @return
     */
    private BorderPane configureTablePane() throws IOException, SAXException, ParserConfigurationException
    {
        tablePane = new BorderPane();
        tablePane.setId("mainEntryTable");

        table_userBench = new VBox();
        table_userBench.setSpacing(10);
        table_userBench.setPadding(new Insets(10, 10, 10, 10));
        table_userBench.prefHeightProperty().bind(scene.heightProperty());
        table_userBench.prefWidthProperty().bind(scene.widthProperty());
        table_userBench.getChildren().addAll(tableControllerUserBench.getTable());
        tablePane.setCenter(table_userBench);

        return tablePane;
    }


    public void splitTablePane(){
        SplitPane splitPane_table = new SplitPane();
        splitPane_table.prefHeightProperty().bind(tablePane.heightProperty());
        splitPane_table.prefWidthProperty().bind(tablePane.widthProperty());
        splitPane_table.setOrientation(Orientation.HORIZONTAL);

        table_DB = new VBox();
        splitPane_table.getItems().addAll(table_userBench, table_DB);
        tablePane.getChildren().remove(table_userBench);
        tablePane.setCenter(splitPane_table);

    }


    /**
     * This method creates pane for visualizations and statistics.
     * @return
     */

    private SplitPane configureVisAndStatsisticsPane() {

        SplitPane plot_stats = new SplitPane();
        plot_stats.setOrientation(Orientation.HORIZONTAL);
        plot_stats.getItems().addAll(configureVisPane(), configureStatisticsPane());

        return plot_stats;

    }

    /**
     * This method creates pane for statistics
     * @return
     */
    private BorderPane configureStatisticsPane(){
        BorderPane statsBorderPane = new BorderPane();
        statsBorderPane.prefHeightProperty().bind(scene.heightProperty());
        statsBorderPane.prefWidthProperty().bind(scene.widthProperty());
        statsBorderPane.setId("mainWindowLeftpart");

        statsTabpane = new TabPane();
        statsBorderPane.setCenter(statsTabpane);

        return statsBorderPane;
    }


    public VBox getTable_DB() {
        return table_DB;
    }
}
