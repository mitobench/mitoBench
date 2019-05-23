package view.dialogues.settings;

import database.DatabaseQueryHandler;
import io.datastructure.Entry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import view.MitoBenchWindow;

import java.util.*;

public class SqlQueryBuilderWindow {


    private final BorderPane root;
    private final MitoBenchWindow mito;
    private final Stage stage;
    private final DatabaseQueryHandler databaseQueryHandler;

    private Button btn_getData;
    private CheckBox checkBox_SelectAllData;
    private CheckBox checkBox_Select100GP;
    private CheckBox checkbox_connector_and;
    private CheckBox checkbox_connector_or;
    private HashMap<String, List<Entry>> data_map;

    private Button btn_importToMitoBench;
    private CheckComboBox<String> continents_sampling_combobox;
    private CheckComboBox<String> continents_tma_inferred_combobox;
    private CheckComboBox<String> continents_sample_origin_combobox;

    private Label label_no_data;
    private GridPane center;


    public SqlQueryBuilderWindow(MitoBenchWindow mitoBenchWindow){

        databaseQueryHandler = new DatabaseQueryHandler();
        mito = mitoBenchWindow;

        root = new BorderPane();
        stage = new Stage();
        stage.setTitle("Database search configurator");
        stage.setScene(new Scene(root));

        fillWindow();
        addAction();

        stage.show();

    }


    public void fillWindow(){

        HBox bottom = new HBox();
        HBox top = new HBox();
        center = new GridPane();
        center.setPadding(new Insets(5,5,5,5));

        btn_getData = new Button("Get data");
        btn_getData.setDisable(false);
        btn_importToMitoBench = new Button("Import to mitoBench");
        btn_importToMitoBench.setDisable(true);

        checkbox_connector_and =new CheckBox("and");
        checkbox_connector_and.setPadding(new Insets(5,5,5,5));
        checkbox_connector_or =new CheckBox("or");
        checkbox_connector_or.setPadding(new Insets(5,5,5,5));

        checkBox_SelectAllData = new CheckBox("Get all data from DB (takes about 1-2 min)");
        checkBox_SelectAllData.setPadding(new Insets(5,5,5,5));
        checkBox_Select100GP = new CheckBox("Get all data from 1000 Genome Project (phase3)");
        checkBox_Select100GP.setPadding(new Insets(5,5,5,5));

        label_no_data = new Label("");


        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        // filling top
        top.setPadding(new Insets(10,10,10,10));
        top.getChildren().add(new Label("Database query configurator\n\nThis is only a basic filtering based on continents " +
                "to reduce the number of samples.\nThe requested data is displayed in a separate window in which you can filter in more detail."));

        // filling center
        center.setPadding(new Insets(10,10,10,10));
        center.add(checkBox_SelectAllData,0,0,2,1);

        center.add(new Separator(),0,1,2,1);

        center.add(checkBox_Select100GP,0,2,2,1);

        center.add(new Separator(),0,3,2,1);

        center.add(checkbox_connector_and, 0,4,1,1);
        center.add(checkbox_connector_or, 1,4,1,1);

        continentFilter();

        // filling bottom
        bottom.setPadding(new Insets(10,10,10,10));
        bottom.getChildren().addAll(region1, label_no_data, btn_getData, btn_importToMitoBench);


        root.setTop(top);
        root.setCenter(center);
        root.setBottom(bottom);

    }


    private void continentFilter(){


        ObservableList<String> continent_list = FXCollections.observableList(new ArrayList<String>(){{add("Africa");
            add("Americas"); add("Antarctica"); add("Asia"); add("Europe"); add("Oceania");}});

        continents_sample_origin_combobox = new CheckComboBox<>(continent_list);
        continents_sampling_combobox = new CheckComboBox<>(continent_list);
        continents_tma_inferred_combobox = new CheckComboBox<>(continent_list);

        Label l_sample_origin = new Label("Continent (sample origin):");
        l_sample_origin.setPadding(new Insets(5,5,5,5));
        center.add(l_sample_origin, 0,5,1,1);
        center.add(continents_sample_origin_combobox, 1,5,1,1);

        Label l_sampling = new Label("Continent (sampling):");
        l_sampling.setPadding(new Insets(5,5,5,5));
        center.add(l_sampling, 0,6,1,1);
        center.add(continents_sampling_combobox, 1,6,1,1);

        Label l_tma_inferred = new Label("Continent (TMA inferred):");
        l_tma_inferred.setPadding(new Insets(5,5,5,5));
        center.add(l_tma_inferred, 0,7,1,1);
        center.add(continents_tma_inferred_combobox, 1,7,1,1);


    }

    private void getData(Task task){
        mito.getProgressBarhandler().activate(task.progressProperty());

        task.setOnSucceeded((EventHandler<Event>) event -> {

            if(data_map.size()>0){

                long startTime = System.currentTimeMillis();
                System.out.println("Import data from database.");
                mito.getLogClass().getLogger(this.getClass()).info("Import data from database.\nQuery: ");

                mito.getTableControllerDB().updateTable(data_map);

                root.setCenter(mito.getTableControllerDB().getTable());

                btn_importToMitoBench.setDisable(false);
                btn_getData.setDisable(true);
                label_no_data.setText("Ready\t");

                mito.getProgressBarhandler().stop();
                long currtime_post_execution = System.currentTimeMillis();
                long diff = currtime_post_execution - startTime;

                long runtime_s = diff / 1000;
                if(runtime_s > 60) {
                    long minutes = runtime_s / 60;
                    long seconds = runtime_s % 60;
                    System.out.println("Runtime of data import to mitoBench: " + minutes + " minutes, and " + seconds + " seconds.");
                } else {
                    System.out.println("Runtime of data import to mitoBench: " + runtime_s + " seconds.");
                }
            } else {
                mito.getProgressBarhandler().stop();
                label_no_data.setText("Filtering results in no data.\t");
            }

        });
        new Thread(task).start();
    }

    /**
     * Add functionality to the buttons.
     */
    public void addAction(){


        checkBox_SelectAllData.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                checkBox_Select100GP.setDisable(true);
                continents_sample_origin_combobox.setDisable(true);
                continents_sampling_combobox.setDisable(true);
                continents_tma_inferred_combobox.setDisable(true);
            } else if(!newValue){
                checkBox_Select100GP.setDisable(false);
                continents_sample_origin_combobox.setDisable(false);
                continents_sampling_combobox.setDisable(false);
                continents_tma_inferred_combobox.setDisable(false);
            }

        });


        checkbox_connector_and.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
               checkbox_connector_or.setDisable(true);
            } else if (!newValue){
                checkbox_connector_or.setDisable(false);
            }
        });

        checkbox_connector_or.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                checkbox_connector_and.setDisable(true);
            } else if (!newValue){
                checkbox_connector_and.setDisable(false);
            }
        });


        btn_getData.setOnAction(e -> {
            if(checkBox_SelectAllData.isSelected()){

                Task task = new Task() {
                    @Override
                    protected Object call() {
                        data_map = databaseQueryHandler.getAllData();
                        return true;
                    }
                };
                label_no_data.setText("Getting data from database...\t");
                getData(task);


            } else {
                String query = "";
                // building query
                if(checkbox_connector_or.isSelected()){
                    query = "or=(" ;
                } else if(checkbox_connector_and.isSelected()){
                    query = "and=(" ;
                } else {
                    query = "and=(" ;
                }

                String query_tmp = "";

                if(checkBox_Select100GP.isSelected()) {
                    query_tmp = "author.eq.The%201000%20Genomes%20Project%20Consortium,";
                    query += query_tmp;
                }

                ObservableList<String> continents_origin_checked = continents_sample_origin_combobox.getCheckModel().getCheckedItems();

                if (continents_origin_checked.size()>0){
                    query_tmp = "or(";
                    for (String continent : continents_origin_checked){
                        query_tmp += "sample_origin_region.eq." + continent + ",";
                    }

                    query_tmp = query_tmp.substring(0, query_tmp.length()-1) + ")";
                    query+=query_tmp + ",";
                }

                ObservableList<String> continents_sampling_checked = continents_sampling_combobox.getCheckModel().getCheckedItems();

                if(continents_sampling_checked.size()>0) {
                    query_tmp = "or(";
                    for (String continent : continents_sampling_checked) {
                        query_tmp += "sampling_region.eq." + continent + ",";
                    }

                    query_tmp = query_tmp.substring(0, query_tmp.length() - 1) + ")";
                    query += query_tmp + ",";
                }

                ObservableList<String> continents_tma_inferred = continents_tma_inferred_combobox.getCheckModel().getCheckedItems();

                if(continents_tma_inferred.size()>0){
                    query_tmp = "or(";
                    for (String continent : continents_tma_inferred){
                        query_tmp += "geographic_info_tma_inferred_region.eq." + continent + ",";
                    }

                    query_tmp = query_tmp.substring(0, query_tmp.length()-1) + ")";
                }


                if(query.endsWith(",")){
                    query = query.substring(0, query.length() - 1) + ")";
                } else {
                    query += query_tmp + ")";
                }


                // run search
                String finalQuery = query;
                Task task = new Task() {
                    @Override
                    protected Object call() {

                        data_map = databaseQueryHandler.getDataSelection(finalQuery);
                        return true;
                    }
                };
                label_no_data.setText("Getting data from database...\t");
                getData(task);


            }
        });

        btn_importToMitoBench.setOnAction(e -> {

            HashMap<String, List<Entry>> data = mito.getTableControllerDB().parseDataToEntrylist();
            mito.getTableControllerUserBench().updateTable(data);
            stage.close();
            mito.getTableControllerDB().cleartable();
        });

    }

}
