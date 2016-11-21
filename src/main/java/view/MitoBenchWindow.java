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
import org.xml.sax.SAXException;
import view.charts.BarPlotHaplo;
import view.menus.EditMenu;
import view.menus.FileMenu;
import view.menus.HelpMenu;
import view.menus.ToolsMenu;
import view.table.*;
import view.tree.TreeHaploChooser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


/**
 * Created by neukamm on 03.11.16.
 */
public class MitoBenchWindow extends Application{

    private BorderPane root;
    private TableController tableManager;
    private BarPlotHaplo barPlotHaplo;


    @Override
    public void start(Stage primaryStage) throws Exception
    {

        root = new BorderPane();
        root.autosize();


        root.setRight(getRightHBox());
        root.setCenter(getCenterPane());
        root.setTop(getMenu());

        Scene scene = new Scene(root, 1200, 600);
        primaryStage.setTitle("Mito Bench");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }



    private MenuBar getMenu() throws Exception
    {
        MenuBar menuBar = new MenuBar();

        FileMenu fileMenu = new FileMenu(tableManager);
        EditMenu editMenu = new EditMenu();
        ToolsMenu menuTools = new ToolsMenu();
        HelpMenu helpMenu = new HelpMenu();

        menuBar.getMenus().addAll(fileMenu.getMenuFile(),
                                  editMenu.getMenuEdit() ,
                                  menuTools.getMenuTools(),
                                  helpMenu.getMenuHelp());

        return menuBar;
    }


    /**
     *
     *              Plotting and Statistics part
     *
     *
     * @return
     */
    private HBox getRightHBox()
    {
        HBox hbox = new HBox();

        VBox vbox = new VBox(50);
        vbox.setPadding(new Insets(0, 20, 0, 20));
        vbox.setAlignment(Pos.CENTER);

        Pane plot = new Pane();
        barPlotHaplo = new BarPlotHaplo("Haplogroups Summary \n(Own dataset)", "Haplogroup", "Count");

        plot.getChildren().addAll(barPlotHaplo.getBarChart());
        vbox.getChildren().addAll(plot,new Label("Place for some statistics"));

        Separator separator1 = new Separator();
        vbox.getChildren().add(1, separator1);
        hbox.getChildren().addAll(new Separator(Orientation.VERTICAL), vbox);

        return hbox;
    }




    /**
     *
     *              TABLE with Tree view
     *
     *
     * @return
     */
    private BorderPane getCenterPane()throws IOException, SAXException, ParserConfigurationException
    {
        BorderPane stackPane = new BorderPane();
        //stackPane.setAlignment(Pos.BASELINE_LEFT);

        // initialize columns
        tableManager = new TableController(new Label("\nInput MT data"));

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(tableManager.getLabel(), tableManager.getTable());



        stackPane.setCenter(vbox);

        TreeHaploChooser treeHaploChooser = new TreeHaploChooser(stackPane, tableManager, barPlotHaplo);

        // add reset table button
        Button reset = new Button("Reset table");

        // reset table to state before selection
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent paramT) {
                tableManager.resetTable();
            }
        });


        // add 'get selected rows' button
        Button getSelectedRowsButton = new Button("Get selected rows");
        getSelectedRowsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent paramT) {
                tableManager.updateView(tableManager.getTable().getSelectionModel().getSelectedItems());
            }
        });


        HBox buttons = new HBox();
        buttons.getChildren().addAll(reset,getSelectedRowsButton);
        buttons.setMargin(reset, new Insets(20, 20, 20, 20));
        buttons.setMargin(getSelectedRowsButton, new Insets(20, 20, 20, 20));
        stackPane.setBottom(buttons);


        return stackPane;
    }

}
