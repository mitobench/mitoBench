package main.java.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import main.java.view.charts.BarPlot;


/**
 * Created by neukamm on 03.11.16.
 */
public class MitoBenchWindow extends Application{

    private BorderPane root;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        root = new BorderPane();
        root.setTop(getMenu());
        root.setRight(getRightHBox());
        //root.setBottom(getFooter());
        //root.setLeft(getLeftHBox());
        root.setCenter(getCenterPane());

        Scene scene = new Scene(root, 900, 500);
        primaryStage.setTitle("Mito Bench");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    private MenuBar getMenu()
    {
        MenuBar menuBar = new MenuBar();

        Menu menuFile = new Menu("File");
        Menu menuEdit = new Menu("Edit");
        Menu menuHelp = new Menu("Help");
        menuBar.getMenus().addAll(menuFile, menuEdit, menuHelp);

        return menuBar;
    }

    private HBox getRightHBox()
    {
        HBox hbox = new HBox();

        VBox vbox = new VBox(50);
        vbox.setPadding(new Insets(0, 20, 0, 20));
        vbox.setAlignment(Pos.CENTER);

        Pane plot = new Pane();
        BarPlot barchart = new BarPlot();
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
        TableView table = new TableView();

        final Label label = new Label("MT genome data");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        TableColumn firstNameCol = new TableColumn("ID");
        TableColumn lastNameCol = new TableColumn("Sequence");
        TableColumn emailCol = new TableColumn("Date");
        emailCol.setSortType(TableColumn.SortType.DESCENDING);

        table.getColumns().addAll(firstNameCol, lastNameCol, emailCol);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);

        stackPane.getChildren().addAll(vbox);

        return stackPane;
    }

}
