package view.dialogues.settings;

import database.DatabaseQueryHandler;
import io.datastructure.Entry;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.CheckComboBox;
import view.MitoBenchWindow;

import java.io.InputStream;
import java.lang.reflect.Field;
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
    private Label imageLabel;
    private Label label_info_selected_items;
    private CheckComboBox<String> checkComboBoxAuthors;
    private CheckBox checkBox_authors;
    private CheckComboBox<String> country_sample_origin_combobox;
    private List<String> countries_africa;
    private List<String> countries_americas;
    private List<String> countries_asia;
    private List<String> countries_europe;
    private List<String> countries_oceania;
    private List<String> countries;
    private List<String> countries_antarctica;
    private HashMap<String, List<String>> continent_country_map;


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
        btn_importToMitoBench = new Button("Import into mitoBench");
        btn_importToMitoBench.setDisable(true);

        checkbox_connector_and =new CheckBox("and");
        checkbox_connector_and.setPadding(new Insets(5,5,5,5));
        checkbox_connector_or =new CheckBox("or");
        checkbox_connector_or.setPadding(new Insets(5,5,5,5));

        checkBox_SelectAllData = new CheckBox("Get all data from DB (takes about 1-2 min)");
        checkBox_SelectAllData.setPadding(new Insets(5,5,5,5));
        checkBox_Select100GP = new CheckBox("Get all data from 1000 Genome Project (phase3) // Is this useful?");
        checkBox_Select100GP.setPadding(new Insets(5,5,5,5));

        Set<String> authors_entry = databaseQueryHandler.getAuthorList();
        List<String> targetList = new ArrayList<>(authors_entry);
        java.util.Collections.sort(targetList);

        checkBox_authors = new CheckBox("Author,year");
        checkBox_authors.setPadding(new Insets(10,10,10,10));
        checkComboBoxAuthors = new CheckComboBox<>(FXCollections.observableList(targetList));


        label_no_data = new Label("");

        InputStream input = getClass().getResourceAsStream("/icons/icons8-info-30.png");

        String info_text = "";

        label_info_selected_items = new Label(info_text);

        Image image = new Image(input);
        ImageView imageView = new ImageView(image);

        imageLabel = new Label();
        imageLabel.setGraphic(imageView);
        final Tooltip tooltip = new Tooltip();
        hackTooltipStartTiming(tooltip);
        tooltip.setText("Select how you want to connect the database queries.\n'and' returns data that matches all search criteria.\n'or' returns data that matches one of the search criteria.\nThe selection here is also applied to the continent search.  ");
        imageLabel.setTooltip(tooltip);

        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        // filling top
        top.setPadding(new Insets(10,10,10,10));
        top.getChildren().add(new Label("Database query configurator\n\nThis is only a basic filtering based on continents " +
                "to reduce the number of samples.\nThe requested data is displayed in a separate window in which you can filter in more detail."));

        // filling center
        center.setPadding(new Insets(10,10,10,10));
        center.add(checkBox_SelectAllData,0,0,2,1);

        center.add(new Separator(),0,1,6,1);

        center.add(checkBox_Select100GP,0,2,2,1);

        center.add(new Separator(),0,3,6,1);

        continentFilter();

        //center.add(new Separator(),0,7,6,1);

        //center.add(checkbox_connector_and, 0,8,1,1);
        //center.add(checkbox_connector_or, 1,8,1,1);
        //center.add(imageLabel, 2,8,1,1);

        center.add(new Separator(),0,9,2,1);
        center.add(checkBox_authors, 0,10,1,1);
        center.add(checkComboBoxAuthors, 1,10,5,1);

        // filling bottom
        bottom.setPadding(new Insets(10,10,10,10));
        bottom.getChildren().addAll(label_info_selected_items, region1, label_no_data, btn_getData, btn_importToMitoBench);


        root.setTop(top);
        root.setCenter(center);
        root.setBottom(bottom);

    }


    private void continentFilter(){


        ObservableList<String> continent_list = FXCollections.observableList(new ArrayList<String>(){{add("Africa");
            add("Americas"); add("Antarctica"); add("Asia"); add("Europe"); add("Oceania");}});

        countries_africa = Arrays.asList("Algeria", "Egypt","Libya","Morocco","Sudan","Tunisia",
                "Western Sahara","British Indian Ocean Territory","Burundi","Comoros","Djibouti","Eritrea","Ethiopia",
                "French Southern Territories","Kenya","Madagascar","Malawi","Mauritius","Mayotte","Mozambique","Reunion",
                "Rwanda","Seychelles","Somalia","South Sudan","Uganda","United Republic of Tanzania","Zambia","Zimbabwe",
                "Angola","Cameroon","Central African Republic","Chad","Congo","Democratic Republic of the Congo",
                "Equatorial Guinea","Gabon","Sao Tome and Principe","Botswana","Eswatini","Lesotho","Namibia",
                "South Africa","Benin","Burkina Faso","Cape Verde","Ivory Coast","Gambia","Ghana","Guinea","Guinea-Bissau",
                "Liberia","Mali","Mauritania","Niger","Nigeria","Saint Helena","Senegal","Sierra Leone","Togo");


        countries_americas = Arrays.asList("Anguilla","Antigua and Barbuda","Aruba","Bahamas","Barbados",
                "Bonaire/Sint Eustatius/Saba","British Virgin Islands","Cayman Islands","Cuba","Curacao","Dominica",
                "Dominican Republic","Grenada","Guadeloupe","Haiti","Jamaica","Martinique","Montserrat","Puerto Rico",
                "Saint Barthelemy","Saint Kitts and Nevis","Saint Lucia","Saint Martin (French Part)","Saint Vincent and the Grenadines",
                "Sint Maarten (Dutch part)","Trinidad and Tobago","Turks and Caicos Islands","United States Virgin Islands",
                "Belize","Costa Rica","El Salvador","Guatemala","Honduras","Mexico","Nicaragua","Panama","Argentina",
                "Bolivia (Plurinational State of)","Bouvet Island","Brazil","Chile","Colombia","Ecuador","Falkland Islands (Malvinas)",
                "French Guiana","Guyana","Paraguay","Peru","South Georgia and the South Sandwich Islands","Suriname","Uruguay",
                "Venezuela (Bolivarian Republic of)","Bermuda","Canada","Greenland","Saint Pierre and Miquelon","United States of America","Antarctica");

        countries_asia = Arrays.asList("Kazakhstan","Kyrgyzstan","Tajikistan","Turkmenistan","Uzbekistan",
                "China","China/Hong Kong Special Administrative Region","China/Macao Special Administrative Region","Taiwan",
                "Democratic People s Republic of Korea","Japan","Mongolia","Republic of Korea","Brunei Darussalam",
                "Cambodia","Indonesia","Lao People s Democratic Republic","Malaysia","Myanmar","Philippines","Singapore",
                "Thailand","Timor-Leste","Viet Nam","Afghanistan","Bangladesh","Bhutan","India","Iran (Islamic Republic of)",
                "Maldives","Nepal","Pakistan","Sri Lanka","Armenia","Azerbaijan","Bahrain","Cyprus","Georgia","Iraq","Israel",
                "Jordan","Kuwait","Lebanon","Oman","Qatar","Saudi Arabia","State of Palestine","Syrian Arab Republic","Turkey",
                "United Arab Emirates","Yemen");


        countries_europe = Arrays.asList("Belarus","Bulgaria","Czechia","Hungary","Poland","Republic of Moldova",
                "Romania","Russian Federation","Slovakia","Ukraine","Aland Islands","Guernsey","Jersey","Denmark","Estonia",
                "Faroe Islands","Finland","Iceland","Ireland","Isle of Man","Latvia","Lithuania","Norway",
                "Svalbard and Jan Mayen Islands","Sweden","United Kingdom of Great Britain and Northern Ireland","Albania",
                "Andorra","Bosnia and Herzegovina","Croatia","Gibraltar","Greece","Holy See","Italy","Malta","Montenegro",
                "Portugal","San Marino","Kosovo","Serbia","Slovenia","Spain","The former Yugoslav Republic of Macedonia",
                "Austria","Belgium","France","Germany","Liechtenstein","Luxembourg","Monaco","Netherlands","Switzerland");


        countries_oceania = Arrays.asList("Australia","Christmas Island","Cocos (Keeling) Islands",
                "Heard Island and McDonald Islands","New Zealand","Norfolk Island","Fiji","New Caledonia",
                "Papua New Guinea","Solomon Islands","Vanuatu","Guam","Kiribati","Marshall Islands",
                "Micronesia (Federated States of)","Nauru","Northern Mariana Islands","Palau","United States Minor Outlying Islands",
                "American Samoa","Cook Islands","French Polynesia","Niue","Pitcairn","Samoa","Tokelau","Tonga","Tuvalu","Wallis and Futuna Islands");

        countries = Arrays.asList("Algeria","Egypt", "Libya", "Morocco", "Sudan", "Tunisia", "Western Sahara",
                "British Indian Ocean Territory", "Burundi", "Comoros", "Djibouti", "Eritrea", "Ethiopia", "French Southern Territories",
                "Kenya", "Madagascar", "Malawi", "Mauritius", "Mayotte", "Mozambique", "Reunion", "Rwanda", "Seychelles", "Somalia",
                "South Sudan", "Uganda", "United Republic of Tanzania", "Zambia", "Zimbabwe", "Angola", "Cameroon", "Central African Republic",
                "Chad", "Congo", "Democratic Republic of the Congo", "Equatorial Guinea", "Gabon", "Sao Tome and Principe", "Botswana",
                "Eswatini", "Lesotho", "Namibia", "South Africa", "Benin", "Burkina Faso", "Cape Verde", "Ivory Coast", "Gambia",
                "Ghana", "Guinea", "Guinea-Bissau", "Liberia", "Mali", "Mauritania", "Niger", "Nigeria", "Saint Helena", "Senegal",
                "Sierra Leone", "Togo", "Anguilla", "Antigua and Barbuda", "Aruba", "Bahamas", "Barbados", "Bonaire/Sint Eustatius/Saba",
                "British Virgin Islands", "Cayman Islands", "Cuba", "Curacao", "Dominica", "Dominican Republic", "Grenada", "Guadeloupe",
                "Haiti", "Jamaica", "Martinique", "Montserrat", "Puerto Rico", "Saint Barthelemy", "Saint Kitts and Nevis", "Saint Lucia",
                "Saint Martin (French Part)", "Saint Vincent and the Grenadines", "Sint Maarten (Dutch part)", "Trinidad and Tobago",
                "Turks and Caicos Islands", "United States Virgin Islands", "Belize", "Costa Rica", "El Salvador", "Guatemala",
                "Honduras", "Mexico", "Nicaragua", "Panama", "Argentina", "Bolivia (Plurinational State of)", "Bouvet Island",
                "Brazil", "Chile", "Colombia", "Ecuador", "Falkland Islands (Malvinas)", "French Guiana", "Guyana", "Paraguay",
                "Peru", "South Georgia and the South Sandwich Islands", "Suriname", "Uruguay", "Venezuela (Bolivarian Republic of)",
                "Bermuda", "Canada", "Greenland", "Saint Pierre and Miquelon", "United States of America", "Antarctica", "Kazakhstan",
                "Kyrgyzstan", "Tajikistan", "Turkmenistan", "Uzbekistan", "China", "China/Hong Kong Special Administrative Region",
                "China/Macao Special Administrative Region", "Taiwan", "Democratic Peoples Republic of Korea", "Japan", "Mongolia",
                "Republic of Korea", "Brunei Darussalam", "Cambodia", "Indonesia", "Lao People s Democratic Republic", "Malaysia",
                "Myanmar", "Philippines", "Singapore", "Thailand", "Timor-Leste", "Viet Nam", "Afghanistan", "Bangladesh", "Bhutan",
                "India", "Iran (Islamic Republic of)", "Maldives", "Nepal", "Pakistan", "Sri Lanka", "Armenia", "Azerbaijan", "Bahrain",
                "Cyprus", "Georgia", "Iraq", "Israel", "Jordan", "Kuwait", "Lebanon", "Oman", "Qatar", "Saudi Arabia", "State of Palestine",
                "Syrian Arab Republic", "Turkey", "United Arab Emirates", "Yemen", "Belarus", "Bulgaria", "Czechia", "Hungary",
                "Poland", "Republic of Moldova", "Romania", "Russian Federation", "Slovakia", "Ukraine", "Aland Islands", "Guernsey",
                "Jersey", "Denmark", "Estonia", "Faroe Islands", "Finland", "Iceland", "Ireland", "Isle of Man", "Latvia", "Lithuania",
                "Norway", "Svalbard and Jan Mayen Islands", "Sweden", "United Kingdom of Great Britain and Northern Ireland", "Albania",
                "Andorra", "Bosnia and Herzegovina", "Croatia", "Gibraltar", "Greece", "Holy See", "Italy", "Malta", "Montenegro",
                "Portugal", "San Marino", "Kosovo", "Serbia", "Slovenia", "Spain", "The former Yugoslav Republic of Macedonia", "Austria",
                "Belgium", "France", "Germany", "Liechtenstein", "Luxembourg", "Monaco", "Netherlands", "Switzerland", "Australia",
                "Christmas Island", "Cocos (Keeling) Islands", "Heard Island and McDonald Islands", "New Zealand", "Norfolk Island",
                "Fiji", "New Caledonia", "Papua New Guinea", "Solomon Islands", "Vanuatu", "Guam", "Kiribati", "Marshall Islands",
                "Micronesia (Federated States of)", "Nauru", "Northern Mariana Islands", "Palau", "United States Minor Outlying Islands",
                "American Samoa", "Cook Islands", "French Polynesia", "Niue", "Pitcairn", "Samoa", "Tokelau", "Tonga", "Tuvalu", "Wallis and Futuna Islands");

        countries_antarctica = Arrays.asList("Antarctica");

        java.util.Collections.sort(countries);
        java.util.Collections.sort(countries_africa);
        java.util.Collections.sort(countries_americas);
        java.util.Collections.sort(countries_asia);
        java.util.Collections.sort(countries_europe);
        java.util.Collections.sort(countries_oceania);
        java.util.Collections.sort(countries_antarctica);

        continent_country_map = new HashMap<>();
        continent_country_map.put("All", countries);
        continent_country_map.put("Africa", countries_africa);
        continent_country_map.put("Americas", countries_americas);
        continent_country_map.put("Asia", countries_asia);
        continent_country_map.put("Europe", countries_europe);
        continent_country_map.put("Oceania", countries_oceania);
        continent_country_map.put("Antarctica", countries_oceania);


        ObservableList<String> country_list = FXCollections.observableArrayList(countries);


        continents_sample_origin_combobox = new CheckComboBox<>(continent_list);
        country_sample_origin_combobox = new CheckComboBox<>(country_list);
        continents_sampling_combobox = new CheckComboBox<>(continent_list);
        continents_tma_inferred_combobox = new CheckComboBox<>(continent_list);

        Label l_sample_origin_continent = new Label("Continent (sample origin):");
        l_sample_origin_continent.setPadding(new Insets(10,10,10,10));
        center.add(l_sample_origin_continent, 0,4,1,1);
        center.add(continents_sample_origin_combobox, 1,4,1,1);


        Label l_sample_origin_country = new Label("Country (sample origin):");
        l_sample_origin_country.setPadding(new Insets(10,10,10,10));
        center.add(l_sample_origin_country, 0,5,1,1);
        center.add(country_sample_origin_combobox, 1,5,1,1);


        // --------------------------------------

        Label l_sampling = new Label("Continent (sampling):");
        l_sampling.setPadding(new Insets(10,10,10,10));
        center.add(l_sampling, 2,4,1,1);
        center.add(continents_sampling_combobox, 3,4,1,1);

        // --------------------------------------

        Label l_tma_inferred = new Label("Continent (TMA inferred):");
        l_tma_inferred.setPadding(new Insets(10,10,10,10));
        center.add(l_tma_inferred, 4,4,1,1);
        center.add(continents_tma_inferred_combobox, 5,4,1,1);





    }

    private void getData(Task task){
        mito.getProgressBarhandler().activate(task.progressProperty());

        task.setOnSucceeded((EventHandler<Event>) event -> {

            if(data_map.size()>0){

                long startTime = System.currentTimeMillis();
                mito.getLogClass().getLogger(this.getClass()).info("Import data from database.\nQuery: ");

                mito.getTableControllerDB().updateTable(data_map);
                this.label_info_selected_items.setText(mito.getTableControllerDB().getTable().getSelectionModel().getSelectedItems().size() + " / " +
                        mito.getTableControllerDB().getTable().getItems().size() +  " rows are selected");

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
                    System.out.println("Runtime of data import to configuration window: " + minutes + " minutes, and " + seconds + " seconds.");
                } else {
                    System.out.println("Runtime of data import to configuration window: " + runtime_s + " seconds.");
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

        continents_sample_origin_combobox.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) c -> {

            ObservableList<String> curr_items = country_sample_origin_combobox.getItems();
            country_sample_origin_combobox.getItems().removeAll(curr_items);

            if(continents_sample_origin_combobox.getCheckModel().getCheckedItems().size() == 0){
                country_sample_origin_combobox.getItems().addAll(continent_country_map.get("All"));

            } else {
                ObservableList<String> country_list = FXCollections.observableArrayList();

                for(String country_selected : continents_sample_origin_combobox.getCheckModel().getCheckedItems()){
                    country_list.addAll(continent_country_map.get(country_selected));
                }
                java.util.Collections.sort(country_list);
                country_sample_origin_combobox.getItems().addAll(country_list);
            }

        });

        mito.getTableControllerDB().addRowListener(label_info_selected_items);

        checkBox_SelectAllData.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                checkBox_Select100GP.setDisable(true);
                continents_sample_origin_combobox.setDisable(true);
                country_sample_origin_combobox.setDisable(true);
                continents_sampling_combobox.setDisable(true);
                continents_tma_inferred_combobox.setDisable(true);
                checkbox_connector_and.setDisable(true);
                checkbox_connector_or.setDisable(true);
                checkComboBoxAuthors.setDisable(true);
                checkBox_authors.setDisable(true);
            } else if(!newValue){
                checkBox_Select100GP.setDisable(false);
                continents_sample_origin_combobox.setDisable(false);
                country_sample_origin_combobox.setDisable(false);
                continents_sampling_combobox.setDisable(false);
                continents_tma_inferred_combobox.setDisable(false);
                checkbox_connector_and.setDisable(false);
                checkbox_connector_or.setDisable(false);
                checkComboBoxAuthors.setDisable(false);
                checkBox_authors.setDisable(false);

            }

        });


        continents_sample_origin_combobox.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) c -> {

            while (c.next()) {
                if (c.wasAdded()) {
                    if (c.toString().contains("All")) {
                        System.out.println(c.toString());


                    }
                }
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


            } if(checkComboBoxAuthors.getCheckModel().getCheckedItems().size() > 0){

                Task task = new Task() {
                    @Override
                    protected Object call() {
                        String query = "or=(";
                        ObservableList<String> selected_authors = checkComboBoxAuthors.getCheckModel().getCheckedItems();
                        for(String author_year : selected_authors){
                            query += "and(author.eq." + author_year.split(",")[0] + "," +
                                    "publication_date.eq." + author_year.split(",")[1] + "),";
                        }


                        if(query.endsWith(",")){
                            query = query.substring(0, query.length() - 1) + ")";
                        } else {
                            query += query + ")";
                        }

                        query = query.replace(" ", "%20");

                        data_map = databaseQueryHandler.getDataSelection(query);
                        return true;
                    }
                };
                label_no_data.setText("Getting data from database...\t");
                getData(task);


            } else {
                String query= "or=(" ;
                // building query
//                if(checkbox_connector_or.isSelected()){
//                    query = "or=(" ;
//                } else if(checkbox_connector_and.isSelected()){
//                    query = "and=(" ;
//                } else {
//                    query = "and=(" ;
//                }

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
                    query += query_tmp + ",";
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
            label_no_data.setText("Import data into workbench\t");
            HashMap<String, List<Entry>> data = mito.getTableControllerDB().parseDataToEntrylist();
            mito.getTableControllerUserBench().updateTable(data);
            mito.setInfo_selected_items_text(mito.getTableControllerUserBench().getTable().getSelectionModel().getSelectedItems().size() + " / " +
                    mito.getTableControllerUserBench().getTable().getItems().size() +  " rows are selected");
            stage.close();
            mito.getTableControllerDB().cleartable();
        });


    }


    public static void hackTooltipStartTiming(Tooltip tooltip) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(250)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Label getLabel_info_selected_items() {
        return label_info_selected_items;
    }

    public void setLabel_info_selected_items(Label label_info_selected_items) {
        this.label_info_selected_items = label_info_selected_items;
    }
}
