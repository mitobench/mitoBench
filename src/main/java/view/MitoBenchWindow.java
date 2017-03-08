package view;

import Logging.LogClass;
import Logging.LoggerSettingsDialogue;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.xml.sax.SAXException;
import view.groups.GroupController;
import view.menus.*;
import view.table.controller.TableControllerDB;
import view.table.controller.TableControllerUserBench;
import view.tree.HaplotreeController;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


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
    private Label info_selected_items = new Label("");
    private GroupController groupController;
    private SplitPane splitPane_table;
    private VBox pane_table_DB;
    private Button enableDBBtn;
    private LogClass logClass;


    @Override
    public void start(Stage primaryStage) throws Exception
    {
        logClass = new LogClass();
        logClass.updateLog4jConfiguration(System.getProperty("user.dir") + "/mito_log_tmp.log");
        continueInit(primaryStage);
        overrideCloseSettings();
    }

    private void overrideCloseSettings() {
        Platform.setImplicitExit(false);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                we.consume();
                LoggerSettingsDialogue loggerSettingsDialogue =
                        new LoggerSettingsDialogue("Log file configuration", logClass, primaryStage);
            }
        });
    }


    public void continueInit(Stage stage) throws Exception {

        // init Logger
        logClass.setUp();

        // set main window properties
        pane_root = new BorderPane();
        pane_root.setId("mainBorderPane");
        scene = new Scene(pane_root);

        primaryStage = stage;
        primaryStage.setTitle("Mito Bench");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMaximized(true);
        primaryStage.getIcons().add(new Image("file:logo/mitoBenchLogo.jpg"));

        // bind width and height to scene to enable resizing
        pane_root.prefHeightProperty().bind(scene.heightProperty());
        pane_root.prefWidthProperty().bind(scene.widthProperty());

        // initialize table
        tableControllerUserBench = new TableControllerUserBench(logClass);
        tableControllerUserBench.init();
        tableControllerUserBench.addRowListener(info_selected_items);
        tableControllerUserBench.getTable().setId("mainEntryTable");
        tableControllerUserBench.createContextMenu();

        // init group controller
        groupController = new GroupController(tableControllerUserBench);
        tableControllerUserBench.setGroupController(groupController);

        // this binding is responsible for binding table to main window
        tableControllerUserBench.getTable().prefHeightProperty().bind(scene.heightProperty());
        tableControllerUserBench.getTable().prefWidthProperty().bind(scene.widthProperty());

        tableControllerDB = new TableControllerDB(logClass);
        tableControllerDB.init();
        tableControllerDB.getTable().setId("dbtable");
        // this binding is responsible for binding table to main window
        tableControllerDB.getTable().prefHeightProperty().bind(scene.heightProperty());
        tableControllerDB.getTable().prefWidthProperty().bind(scene.widthProperty());

        // initialize haplotree with search function
        BorderPane borderpane_center = new BorderPane();
        treeController = new HaplotreeController(tableControllerUserBench, logClass);
        // get all components of central part
        borderpane_center.setCenter(getCenterPane());

        // set all components to main window
        pane_root.setCenter(borderpane_center);
        pane_root.setTop(getMenuPane());

        primaryStage.show();

    }

    private MenuBar getMenuPane() throws Exception
    {
        MenuBar menuBar = new MenuBar();
        menuBar.setId("menuBar");

        EditMenu editMenu = new EditMenu(this);
        StatisticsMenu statisticsMenu = new StatisticsMenu(this);
        FileMenu fileMenu = new FileMenu( statisticsMenu, this);
        GroupMenu groupMenu = new GroupMenu(this);
        TableMenu tableMenu = new TableMenu(this);
        GraphicsMenu graphicsMenu = new GraphicsMenu(this);
        HelpMenu helpMenu = new HelpMenu();

        menuBar.getMenus().addAll(fileMenu.getMenuFile(),
                                  editMenu.getMenuEdit() ,
                                  groupMenu.getMenuGroup(),
                                  statisticsMenu.getMenuTools(),
                                  tableMenu.getMenuTable(),
                                  graphicsMenu.getMenuGraphics(),
                                  helpMenu.getMenuHelp());

        tableControllerUserBench.addGroupmenu(groupMenu);
        tableControllerDB.addGroupmenu(groupMenu);
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

        BorderPane topbar = new BorderPane();
        enableDBBtn = new Button("Enable DB table");
        enableDBBtn.setVisible(false);
        tableControllerUserBench.addButtonFunctionality(enableDBBtn, this);
        topbar.setLeft(label);
        topbar.setRight(enableDBBtn);

        pane_table = new BorderPane();
        pane_table.setId("mainEntryTablePane");
        pane_table_userBench = new VBox();
        pane_table_userBench.setSpacing(10);
        pane_table_userBench.setPadding(new Insets(20, 10, 10, 10));
        pane_table_userBench.getChildren().addAll(topbar, tableControllerUserBench.getTable(), info_selected_items);
        pane_table.setCenter(pane_table_userBench);

        return pane_table;
    }


    public void splitTablePane(TableControllerDB tableControllerDB){
        splitPane_table = new SplitPane();
        splitPane_table.setOrientation(Orientation.HORIZONTAL);

        final Label label = new Label("Database");

        pane_table_DB = new VBox();
        HBox topPanel = new HBox();
        topPanel.setSpacing(10);

        HBox buttonHBox = new HBox();
        buttonHBox.setSpacing(10);
        buttonHBox.setAlignment(Pos.TOP_RIGHT);
        Button addAllBtn = new Button("Add all");
        Button addSelectedBtn = new Button("Add selected");
        Button disableBtn = new Button("Disable DB table");
        tableControllerDB.addButtonFunctionality(addAllBtn, addSelectedBtn, disableBtn, this);

        buttonHBox.getChildren().addAll(addAllBtn, addSelectedBtn, disableBtn);
        topPanel.getChildren().addAll(label, buttonHBox);

        pane_table_DB.setSpacing(10);
        pane_table_DB.setPadding(new Insets(20, 10, 10, 10));
        pane_table_DB.getChildren().addAll(topPanel, tableControllerDB.getTable());
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


    public void disableBDTable() {
        splitPane_table.getItems().remove(pane_table_DB);
    }


    public void enableBDTable() {
        splitPane_table.getItems().add(pane_table_DB);
    }


    public Button getEnableDBBtn() {
        return enableDBBtn;
    }

    public LogClass getLogClass() {
        return logClass;
    }

    public String getMITOBENCH_VERSION() {
        return MITOBENCH_VERSION;
    }

    public BorderPane getPane_root() {
        return pane_root;
    }

    public TableControllerUserBench getTableControllerUserBench() {
        return tableControllerUserBench;
    }

    public TableControllerDB getTableControllerDB() {
        return tableControllerDB;
    }

    public HaplotreeController getTreeController() {
        return treeController;
    }

    public TabPane getTabpane_visualization() {
        return tabpane_visualization;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public TabPane getTabpane_statistics() {
        return tabpane_statistics;
    }

    public BorderPane getPane_table() {
        return pane_table;
    }

    public VBox getPane_table_userBench() {
        return pane_table_userBench;
    }

    public Label getInfo_selected_items() {
        return info_selected_items;
    }

    public GroupController getGroupController() {
        return groupController;
    }

    public SplitPane getSplitPane_table() {
        return splitPane_table;
    }

    public VBox getPane_table_DB() {
        return pane_table_DB;
    }
}
