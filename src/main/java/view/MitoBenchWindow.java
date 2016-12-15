package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.xml.sax.SAXException;
import view.menus.*;
import view.table.*;
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



    @Override
    public void start(Stage primaryStage) throws Exception
    {


        root = new BorderPane();
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

        FileMenu fileMenu = new FileMenu(tableController, MITOBENCH_VERSION);
        EditMenu editMenu = new EditMenu();
        ToolsMenu toolsMenu = new ToolsMenu();
        TableMenu tableMenu = new TableMenu(tableController);
        GraphicsMenu graphicsMenu = new GraphicsMenu(tableController, tabPane, treeController, primaryStage);
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
        center.getItems().addAll(getLeftSplitPane(), getPlotPane());


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
    private BorderPane getLeftSplitPane()throws IOException, SAXException, ParserConfigurationException
    {
        BorderPane borderPane = new BorderPane();

        // initialize table
        tableController = new TableController(scene);

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(40, 10, 10, 10));
        vbox.prefHeightProperty().bind(scene.heightProperty());
        vbox.prefWidthProperty().bind(scene.widthProperty());
        vbox.getChildren().addAll(tableController.getTable());
        borderPane.setCenter(vbox);

        // set haplotree - seach view
        Pane treepane = new Pane();

        treeController = new TreeHaploController(treepane, tableController);

        borderPane.setTop(treepane);

        return borderPane;
    }

}
