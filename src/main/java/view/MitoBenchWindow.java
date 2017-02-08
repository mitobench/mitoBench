package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
import view.tree.HaplotreeController;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by neukamm on 03.11.16.
 */
public class MitoBenchWindow extends Application{

    private final String MITOBENCH_VERSION = "0.1";

    private BorderPane pane_root;
    private TableControllerUserBench tableControllerUserBench;
    private TableControllerDB tableControllerDB;
    private HaplotreeController treeController;
    private Scene scene;
    private TabPane tabpane_visualization;
    private Stage primaryStage;
    private TabPane tabpane_statistics;
    private BorderPane pane_table;
    private VBox pane_table_userBench;
    private Label info_selected_items=new Label("");


    @Override
    public void start(Stage primaryStage) throws Exception
    {

        pane_root = new BorderPane();
        pane_root.setId("mainBorderPane");
        scene = new Scene(pane_root);

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Mito Bench");
        this.primaryStage.setScene(scene);
        this.primaryStage.setResizable(true);
        primaryStage.setMaximized(true);
        //primaryStage.setAlwaysOnTop(true);


        // bind width and height to scene to enable resizing
        pane_root.prefHeightProperty().bind(scene.heightProperty());
        pane_root.prefWidthProperty().bind(scene.widthProperty());

        // initialize table
        tableControllerUserBench = new TableControllerUserBench();
        tableControllerUserBench.init();
        tableControllerUserBench.addRowListener(info_selected_items);
        tableControllerUserBench.getTable().setId("mainEntryTable");
        tableControllerUserBench.createContextMenu();

        // this binding is responsible for binding table to main window
        tableControllerUserBench.getTable().prefHeightProperty().bind(scene.heightProperty());
        tableControllerUserBench.getTable().prefWidthProperty().bind(scene.widthProperty());

        tableControllerDB = new TableControllerDB();
        tableControllerDB.init();
        tableControllerDB.getTable().setId("dbtable");
        // this binding is responsible for binding table to main window
        tableControllerDB.getTable().prefHeightProperty().bind(scene.heightProperty());
        tableControllerDB.getTable().prefWidthProperty().bind(scene.widthProperty());

        BorderPane borderpane_center = new BorderPane();
        // initialize haplotree with search function
        Pane pane_tree = new Pane();
        treeController = new HaplotreeController(tableControllerUserBench);
        treeController.configureSearch(pane_tree);
        treeController.setAnimation();
        borderpane_center.setTop(pane_tree);

        // get all components of central part
        borderpane_center.setCenter(getCenterPane());

        // set all components to main window
        pane_root.setCenter(borderpane_center);
        pane_root.setTop(getMenuPane());

        this.primaryStage.show();
    }


    private MenuBar getMenuPane() throws Exception
    {
        MenuBar menuBar = new MenuBar();
        menuBar.setId("menuBar");

        EditMenu editMenu = new EditMenu();
        StatisticsMenu toolsMenu = new StatisticsMenu(tableControllerUserBench, treeController, tabpane_statistics, scene, primaryStage);
        FileMenu fileMenu = new FileMenu(tableControllerUserBench, MITOBENCH_VERSION, primaryStage, toolsMenu,
                this, tableControllerDB);
        TableMenu tableMenu = new TableMenu(tableControllerUserBench);
        GraphicsMenu graphicsMenu = new GraphicsMenu(tableControllerUserBench, tabpane_visualization, treeController, primaryStage, scene, tabpane_statistics);
        HelpMenu helpMenu = new HelpMenu();

        menuBar.getMenus().addAll(fileMenu.getMenuFile(),
                                  editMenu.getMenuEdit() ,
                                  toolsMenu.getMenuTools(),
                                  tableMenu.getMenuTable(),
                                  graphicsMenu.getMenuGraphics(),
                                  helpMenu.getMenuHelp());

        tableControllerUserBench.addEditMenue(editMenu);
        tableControllerDB.addEditMenue(editMenu);
        return menuBar;
    }



    public SplitPane  getCenterPane() throws ParserConfigurationException, SAXException, IOException {

        SplitPane splitpane_center = new SplitPane();
        splitpane_center.setOrientation(Orientation.VERTICAL);
        // add table and vis/statistics pane
        splitpane_center.getItems().addAll(configureVisAndStatisticsPane(), configureTablePane());
        return splitpane_center;

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
        tabpane_visualization = new TabPane();
        tabpane_visualization.setId("mainVizTabpane");
        tabpane_visualization.setPadding(new Insets(10, 10, 10,10));

        return tabpane_visualization;
    }


    /**
     *  This method creates pane for table view.
     *
     *  @return
     */
    private BorderPane configureTablePane() throws IOException, SAXException, ParserConfigurationException
    {
        final Label label = new Label("User table");
        String info_text = tableControllerUserBench.getTable().getSelectionModel().getSelectedItems().size() + " rows are selected";
        info_selected_items.setText(info_text);



        pane_table = new BorderPane();
        pane_table.setId("mainEntryTablePane");
        pane_table_userBench = new VBox();
        pane_table_userBench.setSpacing(10);
        pane_table_userBench.setPadding(new Insets(20, 10, 10, 10));
        pane_table_userBench.getChildren().addAll(label, tableControllerUserBench.getTable(), info_selected_items);
        pane_table.setCenter(pane_table_userBench);

        return pane_table;
    }


    public void splitTablePane(TableControllerDB tableControllerDB){
        SplitPane splitPane_table = new SplitPane();
        splitPane_table.setOrientation(Orientation.HORIZONTAL);

        final Label label = new Label("Database");

        VBox pane_table_DB = new VBox();
        pane_table_DB.setSpacing(10);
        pane_table_DB.setPadding(new Insets(20, 10, 10, 10));
        pane_table_DB.getChildren().addAll(label, tableControllerDB.getTable());
        pane_table.getChildren().remove(pane_table_userBench);
        pane_table.setCenter(splitPane_table);

        splitPane_table.getItems().addAll(pane_table_userBench, pane_table_DB);

    }


    /**
     * This method creates pane for visualizations and statistics.
     * @return
     */

    private SplitPane configureVisAndStatisticsPane() {

        SplitPane splitpane_viz_statistics = new SplitPane();
        splitpane_viz_statistics.setOrientation(Orientation.HORIZONTAL);
        splitpane_viz_statistics.getItems().addAll(configureVisPane(), configureStatisticsPane());

        return splitpane_viz_statistics;

    }

    /**
     * This method creates pane for statistics
     * @return
     */
    private BorderPane configureStatisticsPane(){
        BorderPane borderpane_statistics = new BorderPane();
        borderpane_statistics.setId("mainWindowLeftpart");

        tabpane_statistics = new TabPane();
        borderpane_statistics.setCenter(tabpane_statistics);

        return borderpane_statistics;
    }
    

    public Scene getScene() {
        return scene;
    }


}
