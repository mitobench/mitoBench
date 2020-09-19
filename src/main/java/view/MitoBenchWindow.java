package view;

import Logging.LogClass;
import Logging.LoggerSettingsDialogue;
import analysis.ProgressBarHandler;
import controller.*;
import database.DatabaseQueryHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.CancelButton;
import view.dialogues.information.AboutDialogue;
import view.dialogues.information.DBstatisticsDialogue;
import view.menus.*;
import view.tree.TreeView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


/**
 * Created by neukamm on 03.11.16.
 */
public class MitoBenchWindow extends Application {

    private final String MITOBENCH_VERSION = "1.0";

    private BorderPane pane_root;

    private Scene scene;
    private TabPane tabpane_visualization;
    private Stage primaryStage;
    private TabPane tabpane_statistics;
    private BorderPane pane_table;
    private VBox pane_table_userBench;
    private Label info_selected_items;
    private CancelButton btn_cancel;
    private LogClass logClass;
    private TreeView treeView;
    private boolean anotherProjectLoaded;
    private FileMenu fileMenu;
    private ProgressBarHandler progressBarhandler;
    private DatabaseQueryHandler databaseQueryHandler;

    private ChartController chartController;
    private MenuController menuController;
    private GroupController groupController;
    private TableControllerUserBench tableControllerUserBench;
    private TableControllerDB tableControllerDB;
    private HaplotreeController treeController;
    private FileReaderController fileReaderController;
    private DialogueController dialogueController;


    @Override
    public void start(Stage primaryStage) throws Exception
    {
        logClass = new LogClass();
        logClass.updateLog4jConfiguration(System.getProperty("user.dir") + "/mito_log_tmp.log");
        info_selected_items = new Label("");

        init(primaryStage);
        overrideCloseSettings();



    }

    /**
     * This method initializes all components of the mitoBench window.
     * @param stage
     * @throws Exception
     */

    public void init(Stage stage) throws Exception {

        anotherProjectLoaded = false;

        // init Logger
        logClass.setUp();

        // set main window properties
        pane_root = new BorderPane();
        pane_root.setId("mainBorderPane");
        scene = new Scene(pane_root);

        // set stage properties
        primaryStage = stage;
        primaryStage.setTitle("mitoBench v" + getMITOBENCH_VERSION());
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMaximized(true);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo/mitoBenchLogo.jpg")));

        // init database and menu controller
        databaseQueryHandler = new DatabaseQueryHandler();
        menuController = new MenuController(databaseQueryHandler, this);
        dialogueController = new DialogueController(this);


        // bind width and height to scene to enable resizing
        pane_root.prefHeightProperty().bind(scene.heightProperty());
        pane_root.prefWidthProperty().bind(scene.widthProperty());

        btn_cancel = new CancelButton();

        // initialize table
        tableControllerUserBench = new TableControllerUserBench(logClass);
        tableControllerUserBench.init();
        tableControllerUserBench.addRowListener(info_selected_items);
        tableControllerUserBench.getTable().setId("mainEntryTable");
        tableControllerUserBench.createContextMenu();

        tableControllerDB = new TableControllerDB(logClass);
        tableControllerDB.init();
        tableControllerDB.getTable().setId("dbtable");
        // this binding is responsible for binding table to main window
        tableControllerDB.getTable().prefHeightProperty().bind(scene.heightProperty());
        tableControllerDB.getTable().prefWidthProperty().bind(scene.widthProperty());


        // init group controller
        groupController = new GroupController(tableControllerUserBench);
        tableControllerUserBench.setGroupController(groupController);
        tableControllerDB.setGroupController(groupController);

        // this binding is responsible for binding table to main window
        tableControllerUserBench.getTable().prefHeightProperty().bind(scene.heightProperty());
        tableControllerUserBench.getTable().prefWidthProperty().bind(scene.widthProperty());


        fileReaderController = new FileReaderController(tableControllerUserBench, logClass, this);

        // initialize haplotree with search function
        BorderPane borderpane_center = new BorderPane();


        treeController = new HaplotreeController(tableControllerUserBench, logClass, this);
        treeView = new TreeView(treeController.getTree());
        treeController.setTreeView(treeView);

        chartController = new ChartController();
        chartController.init(tableControllerUserBench, treeController.getTreeMap());

        // get all components of central part
        borderpane_center.setCenter(getCenterPane());

        // set all components to main window
        pane_root.setCenter(borderpane_center);
        pane_root.setTop(getTopPane());

        // add drag and drop files to data table view
        tableControllerUserBench.addDragAndDropFiles(this);

        menuController.setTableController(tableControllerUserBench);

        primaryStage.show();

    }

    private VBox getTopPane() {
        VBox topPane = new VBox();
        topPane.getChildren().addAll(getMenuPane(), getToolbarpane());

        return topPane;

    }

    private Toolbarpane getToolbarpane() {

        progressBarhandler = new ProgressBarHandler(btn_cancel);
        progressBarhandler.create();

        Toolbarpane toolBar = new Toolbarpane(fileReaderController, this, progressBarhandler.getProgressBar(), btn_cancel);

        return toolBar;
    }

    private MenuBar getMenuPane() {

        MenuBar menuBar = new MenuBar();
        menuBar.setId("menuBar");

        ProgressBar bar = new ProgressBar();
        bar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);

        EditMenu editMenu = new EditMenu(this);
        GroupMenu groupMenu = new GroupMenu(this);

        StatisticsMenu statisticsMenu = new StatisticsMenu(this);
        fileMenu = new FileMenu( this);
        AnalysisMenu analysisMenu = new AnalysisMenu(this, statisticsMenu);
        ToolsMenu toolsMenu = new ToolsMenu(this, groupMenu, analysisMenu, statisticsMenu);
        TableMenu tableMenu = new TableMenu(this);
        VisualizationMenu visualizationMenu = new VisualizationMenu(this);
        HelpMenu helpMenu = new HelpMenu();

        menuBar.getMenus().addAll(fileMenu.getMenuFile(),
                                  editMenu.getMenuEdit() ,
                                  toolsMenu.getMenuTools(),
                                  //groupMenu.getMenuGroup(),
                                  //analysisMenu.getMenuAnalysis(),
                                  //statisticsMenu.getMenuTools(),
                                  //tableMenu.getMenuTable(),
                                  visualizationMenu.getMenuGraphics(),
                                  helpMenu.getMenuHelp());


        tableControllerUserBench.addGroupmenu(groupMenu);
        tableControllerDB.addGroupmenu(groupMenu);
        return menuBar;
    }



    public SplitPane  getCenterPane() {
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
    private BorderPane configureTablePane()
    {
        String info_text = tableControllerUserBench.getTable().getSelectionModel().getSelectedItems().size() + " / " +
                tableControllerUserBench.getTable().getItems().size() +  " rows are selected";

        info_selected_items.setText(info_text);

        pane_table = new BorderPane();
        pane_table.setId("mainEntryTablePane");
        pane_table_userBench = new VBox();
        pane_table_userBench.setSpacing(10);
        pane_table_userBench.setPadding(new Insets(5, 5, 5, 5));
        pane_table_userBench.getChildren().addAll(tableControllerUserBench.getTable(), info_selected_items);
        pane_table.setCenter(pane_table_userBench);

        return pane_table;
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
        Tab welcomeTab = new Tab("Welcome to mitoBench", new AboutDialogue("Welcome",
                "If you need some help, read the documentation first:").getDialogGrid());

        Tab dbStatsTab = new Tab("Database statistics", new DBstatisticsDialogue(databaseQueryHandler, getMITOBENCH_VERSION()).getDialogGrid());

        tabpane_statistics.getTabs().addAll(welcomeTab, dbStatsTab);
        borderpane_statistics.setCenter(tabpane_statistics);

        return borderpane_statistics;
    }


    private void overrideCloseSettings() {
        Platform.setImplicitExit(false);

        primaryStage.setOnCloseRequest(we -> {
            we.consume();
            // delete haplogrep files
            if (Files.exists(new File("haplogroups.hsd.dot").toPath())) {
                try {
                    Files.delete(new File("haplogroups.hsd.dot").toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            LoggerSettingsDialogue loggerSettingsDialogue =
                    new LoggerSettingsDialogue("Log file configuration", logClass, primaryStage);
        });
    }



    public Scene getScene() {
        return scene;
    }

    public Button getBtn_cancel() {
        return btn_cancel;
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

    public void setInfo_selected_items_text(String info_selected_items_text) {
        this.info_selected_items.setText(info_selected_items_text);
    }

    public GroupController getGroupController() {
        return groupController;
    }

    public boolean isAnotherProjectLoaded() {
        return anotherProjectLoaded;
    }

    public void setAnotherProjectLoaded(boolean anotherProjectLoaded) {
        this.anotherProjectLoaded = anotherProjectLoaded;
    }

    public FileMenu getFileMenu() {
        return fileMenu;
    }

    public void setFileMenu(FileMenu fileMenu) {
        this.fileMenu = fileMenu;
    }


    public ChartController getChartController() {
        return chartController;
    }

    public ProgressBarHandler getProgressBarhandler() {
        return progressBarhandler;
    }

    public void setProgressBarhandler(ProgressBarHandler progressBarhandler) {
        this.progressBarhandler = progressBarhandler;
    }

    public FileReaderController getFileReaderController() {
        return fileReaderController;
    }

    public MenuController getMenuController() {
        return menuController;
    }

    public DatabaseQueryHandler getDatabaseQueryHandler() {
        return databaseQueryHandler;
    }

    public DialogueController getDialogueController() {
        return dialogueController;
    }

    public void setDialogueController(DialogueController dialogueController) {
        this.dialogueController = dialogueController;
    }
}
