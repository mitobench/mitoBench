package view.dialogues.settings;

import database.DatabaseQueryHandler;
import io.datastructure.Entry;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.table.TableFilter;
import org.controlsfx.control.textfield.TextFields;
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
    private CheckComboBox<String> controll_sample_city;
    private CheckComboBox<String> controll_sample_country;
    private CheckComboBox<String> controll_sample_subregion;
    private CheckComboBox<String> controll_sample_region;
    private CheckComboBox<String> controll_sample_inter_region;

    public SqlQueryBuilderWindow(MitoBenchWindow mitoBenchWindow){

        databaseQueryHandler = new DatabaseQueryHandler();
        mito = mitoBenchWindow;

        root = new BorderPane();
        stage = new Stage();
        stage.setTitle("Database search configurator");
        stage.setScene(new Scene(root, 900, 450));

        fillWindow();
        addAction();

        stage.show();

    }


    public void fillWindow(){



//        TabPane configurator_tabs = new TabPane();
//        configurator_tabs.setSide(Side.LEFT);
//        configurator_tabs.setRotateGraphic(true);
//        configurator_tabs.setTabMinHeight(100);
//        configurator_tabs.setTabMaxHeight(100);
//        configurator_tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
//
//        configurator_tabs.getTabs().addAll(fillLocationTab(), fillPublicationTab(), fillSampleInfoTab());
//        root.setCenter(configurator_tabs);
//
        HBox bottom = new HBox();
        btn_getData = new Button("Send");
        checkBox_SelectAllData = new CheckBox("Get all data from DB");
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);
        bottom.setPadding(new Insets(10,10,10,10));
        bottom.getChildren().addAll(checkBox_SelectAllData, region1, btn_getData);

        Label database_filtering_explanation = new Label("To filter data, right-click on the column name.\nSearch field is case sensitive.");
        database_filtering_explanation.setPadding(new Insets(10,10,10,10));

        HashMap<String, List<Entry>> data_map = databaseQueryHandler.getMetaData();
        mito.getLogClass().getLogger(this.getClass()).info("Import data from mitoDB.\nQuery: ");
        mito.getTableControllerDB().updateTable(data_map);
        TableView table = mito.getTableControllerDB().getTable();
        TableFilter filter = new TableFilter(table);

        root.setTop(database_filtering_explanation);
        root.setCenter(mito.getTableControllerDB().getTable());
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

                mito.getTableControllerUserBench().updateTable(databaseQueryHandler.getGenerellData(accession_ids));
            }

            stage.close();
            mito.getTableControllerDB().cleartable();

        });


//        btn_getData.setOnAction(e -> {
//            if (checkBox_SelectAllData.isSelected()) {
//
//                HashMap<String, List<Entry>> data_map = databaseQueryHandler.getAllData();
//
//                mito.getLogClass().getLogger(this.getClass()).info("Import data from mitoDB.\nQuery: ");
//                mito.getTableControllerDB().updateTable(data_map);
               // mito.splitTablePane(mito.getTableControllerDB());
//
//                mito.getTableControllerDB().addFilter();
//                stage.close();
//            } else {
//                // build query
//
//                // location
//                ObservableList<String> checked_regions = controll_sample_region.getCheckModel().getCheckedItems();
//
////                for(String s : checked_regions){
////                    mito.getTableControllerDB().updateTable(databaseQueryHandler.getGenerellData("meta?or=(sample_origin_region=eq." + s));
////
////                }
//
//               //mito.getTableControllerDB().updateTable(databaseQueryHandler.getGenerellData("meta?or=(sample_origin_region.eq.Asia,sample_origin_region.eq.Europe)"));
//                mito.splitTablePane(mito.getTableControllerDB());
//                mito.getTableControllerDB().addFilter();
//                stage.close();
//            }
//        });




    }

    private Tab fillLocationTab(){
        Tab tab_location = new Tab();
        Label l = new Label("Location");
        l.setRotate(90);
        StackPane stp = new StackPane(new Group(l));
        stp.setRotate(90);
        tab_location.setGraphic(stp);

        // components grid pane
        Label label_sample_origin = new Label("Sample origin");
        Label label_sample_origin_lat = new Label("Latitude");
        ComboBox comboBox_latitude = new ComboBox();
        comboBox_latitude.setEditable(true);
        comboBox_latitude.getItems().add("Choice 1");
        comboBox_latitude.getItems().add("Choice 2");
        comboBox_latitude.getItems().add("Choice 3");

        TextFields.bindAutoCompletion(comboBox_latitude.getEditor(), comboBox_latitude.getItems());


        Label label_sample_origin_long = new Label("Longitude");
        ComboBox comboBox_sample_origin_long = new ComboBox();
        comboBox_sample_origin_long.setEditable(true);
        comboBox_sample_origin_long.getItems().add("Choice 1");
        comboBox_sample_origin_long.getItems().add("Choice 2");
        comboBox_sample_origin_long.getItems().add("Choice 3");

        TextFields.bindAutoCompletion(comboBox_sample_origin_long.getEditor(), comboBox_sample_origin_long.getItems());


        Label label_sample_origin_region = new Label("Region");
        ObservableList<String> items = FXCollections.observableArrayList();
        Set<String> regions = databaseQueryHandler.getLocationData("sample_origin_region");
        items.add("All");
        items.addAll(regions);
        controll_sample_region = getControll(items);

        Label label_sample_origin_subregion = new Label("Subregion");
        ObservableList<String> items_subregion = FXCollections.observableArrayList();
        Set<String> subregions = databaseQueryHandler.getLocationData("sample_origin_subregion");
        items_subregion.add("All");
        items_subregion.addAll(subregions);
        controll_sample_subregion = getControll(items_subregion);

        Label label_sample_origin_intermediate_region = new Label("Interm. region");
        ObservableList<String> items_inter_region = FXCollections.observableArrayList();
        Set<String> inter_regions = databaseQueryHandler.getLocationData("sample_origin_intermediate_region");
        items_inter_region.add("All");
        items_inter_region.addAll(inter_regions);
        controll_sample_inter_region = getControll(items_inter_region);


        Label label_sample_origin_country = new Label("Country");
        ObservableList<String> items_sample_country = FXCollections.observableArrayList();
        Set<String> sample_country = databaseQueryHandler.getLocationData("sample_origin_country");
        items_sample_country.add("All");
        items_sample_country.addAll(sample_country);
        controll_sample_country = getControll(items_sample_country);


        Label label_sample_origin_city = new Label("City");
        ObservableList<String> items_sample_city = FXCollections.observableArrayList();
        Set<String> sample_city = databaseQueryHandler.getLocationData("sample_origin_city");
        items_sample_city.add("All");
        items_sample_city.addAll(sample_city);
        controll_sample_city = getControll(items_sample_city);


        // fill grid pane
        GridPane gridPane_location = new GridPane();
        gridPane_location.setAlignment(Pos.CENTER);
        gridPane_location.setHgap(10);
        gridPane_location.setVgap(10);
        int rowindex = 0;

        gridPane_location.add(label_sample_origin, 0,rowindex,1,1);
        gridPane_location.add(getNewVerticalSeparator(), 1,rowindex,1,1);

        gridPane_location.add(label_sample_origin_lat, 2,rowindex,1,1);
        gridPane_location.add(comboBox_latitude, 3, rowindex,1,1);
        gridPane_location.add(label_sample_origin_long, 4,rowindex,1,1);
        gridPane_location.add(comboBox_sample_origin_long, 5, rowindex,1,1);

        gridPane_location.add(getNewVerticalSeparator(), 1,++rowindex,1,1);
        gridPane_location.add(label_sample_origin_region, 2,rowindex,1,1);
        gridPane_location.add(controll_sample_region, 3, rowindex,1,1);
        gridPane_location.add(label_sample_origin_subregion, 4,rowindex,1,1);
        gridPane_location.add(controll_sample_subregion, 5, rowindex,1,1);

        gridPane_location.add(getNewVerticalSeparator(), 1,++rowindex,1,1);
        gridPane_location.add(label_sample_origin_intermediate_region, 2,rowindex,1,1);
        gridPane_location.add(controll_sample_inter_region, 3, rowindex,1,1);
        gridPane_location.add(label_sample_origin_country, 4,rowindex,1,1);
        gridPane_location.add(controll_sample_country, 5, rowindex,1,1);

        gridPane_location.add(getNewVerticalSeparator(), 1,++rowindex,1,1);
        gridPane_location.add(label_sample_origin_city, 2,rowindex,1,1);
        gridPane_location.add(controll_sample_city, 3, rowindex,1,1);


        gridPane_location.add(new Separator(), 0, ++rowindex, 6,1);

        gridPane_location.add(new Label("Sampling"), 0,++rowindex,1,1);
        gridPane_location.add(getNewVerticalSeparator(), 1,rowindex,1,1);

        gridPane_location.add(new Separator(), 0, ++rowindex, 6,1);

        gridPane_location.add(new Label("TMA inferred"), 0,++rowindex,1,1);
        gridPane_location.add(getNewVerticalSeparator(), 1,rowindex,1,1);



        tab_location.setContent(gridPane_location);

        return tab_location;
    }


    private Tab fillPublicationTab(){
        Tab tab_publication = new Tab();
        Label l2 = new Label("Publication");
        l2.setRotate(90);
        StackPane stp2 = new StackPane(new Group(l2));
        stp2.setRotate(90);
        tab_publication.setGraphic(stp2);

        return tab_publication;
    }

    private Tab fillSampleInfoTab(){
        Tab tab_sampleInfo = new Tab();
        Label l2 = new Label("Sample");
        l2.setRotate(90);
        StackPane stp2 = new StackPane(new Group(l2));
        stp2.setRotate(90);
        tab_sampleInfo.setGraphic(stp2);

        return tab_sampleInfo;
    }


    private Separator getNewVerticalSeparator(){
        Separator sep = new Separator();
        sep.setOrientation(Orientation.VERTICAL);
        return sep;
    }

    private CheckComboBox<String> getControll(ObservableList items){

        CheckComboBox<String> controll = new CheckComboBox<>(items);
        controll.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) c -> {

            while (c.next()) {
                if (c.wasAdded()) {
                    if (c.toString().contains("All")) {

                        // if we call the getCheckModel().clearChecks() this will
                        // cause to "remove" the 'All' too at least inside the model.
                        // So we need to manually clear everything else
                        for (int i = 1; i < items.size(); i++) {
                            controll.getCheckModel().clearCheck(i);
                        }

                    } else {
                        // check if the "All" option is selected and if so remove it
                        if (controll.getCheckModel().isChecked(0)) {
                            controll.getCheckModel().clearCheck(0);
                        }

                    }
                }
            }
        });


        return controll;
    }
}
