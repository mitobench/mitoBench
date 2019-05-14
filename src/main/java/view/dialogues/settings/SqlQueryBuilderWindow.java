package view.dialogues.settings;

import analysis.ProgressBarHandler;
import database.DatabaseQueryHandler;
import io.datastructure.Entry;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.controlsfx.control.table.TableFilter;
import view.MitoBenchWindow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SqlQueryBuilderWindow {


    private final BorderPane root;
    private final MitoBenchWindow mito;
    private final Stage stage;
    private final DatabaseQueryHandler databaseQueryHandler;
    private Button btn_getData;
    private CheckBox checkBox_SelectAllData;
    private ProgressBarHandler progressBarhandler;
    private HashMap<String, List<Entry>> data_map;


    public SqlQueryBuilderWindow(MitoBenchWindow mitoBenchWindow){

        databaseQueryHandler = new DatabaseQueryHandler();
        mito = mitoBenchWindow;
        progressBarhandler = mitoBenchWindow.getProgressBarhandler();

        root = new BorderPane();
        stage = new Stage();
        stage.setTitle("Database search configurator");
        stage.setScene(new Scene(root, 900, 450));

        fillWindow();
        addAction();

        stage.show();

    }


    public void fillWindow(){

        HBox bottom = new HBox();
        btn_getData = new Button("Send");
        checkBox_SelectAllData = new CheckBox("Get all data from DB");
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);
        bottom.setPadding(new Insets(10,10,10,10));
        bottom.getChildren().addAll(checkBox_SelectAllData, region1, btn_getData);

        Label database_filtering_explanation = new Label("To filter data, right-click on the column name.\nSearch field is case sensitive.");
        database_filtering_explanation.setPadding(new Insets(10,10,10,10));

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                 data_map = databaseQueryHandler.getMetaData();
                return true;
            }
        };

        mito.getProgressBarhandler().activate(task.progressProperty());

        task.setOnSucceeded((EventHandler<Event>) event -> {
            mito.getLogClass().getLogger(this.getClass()).info("Import data from mitoDB.\nQuery: ");
            mito.getTableControllerDB().updateTable(data_map);
            TableView table = mito.getTableControllerDB().getTable();
            TableFilter filter = new TableFilter(table);
            root.setCenter(mito.getTableControllerDB().getTable());
            mito.getProgressBarhandler().stop();
        });


        root.setTop(database_filtering_explanation);
        root.setCenter(new Label("Getting data from database..."));
        root.setBottom(bottom);

    }

    public void addAction(){
        btn_getData.setOnAction(e -> {
            if(checkBox_SelectAllData.isSelected()){

                HashMap<String, List<Entry>> data_map = databaseQueryHandler.getAllData();
                mito.getTableControllerUserBench().updateTable(data_map);

            } else {
                int index_accession_id = mito.getTableControllerDB().getColIndex("ID");
                TableView search_content = mito.getTableControllerDB().getTable();
                Set<String> accession_ids = new HashSet<>();

                for(int i = 0; i < search_content.getItems().size(); i++){
                    Object row = search_content.getItems().get(i);
                    List<String> list = (List<String>) row;
                    accession_ids.add(list.get(index_accession_id));
                }

                mito.getTableControllerUserBench().updateTable(databaseQueryHandler.getFilteredData(accession_ids));
            }

            stage.close();
            mito.getTableControllerDB().cleartable();

        });

    }




}
