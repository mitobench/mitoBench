package view.dialogues.settings;

import database.DatabaseQueryHandler;
import io.datastructure.Entry;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.controlsfx.control.CheckComboBox;
import view.MitoBenchWindow;

import java.util.*;

public class DatabaseConfigDownloadDialogue {


    private final BorderPane root;
    private final MitoBenchWindow mito;
    private final DatabaseQueryHandler databaseQueryHandler;

    private Button btn_getData;
    private CheckBox checkBox_SelectAllData;
    private HashMap<String, List<Entry>> data_map;

    private Button btn_importToMitoBench;
    private CheckComboBox<String> population_combobox;
    private CheckComboBox<String> continents_sample_origin_combobox;

    private Label label_no_data;
    private GridPane center;
    private Label label_info_selected_items;
    private CheckComboBox<String> checkComboBoxAuthors;
    private ComboBox<String> checkComboBoxModernAncient;
    private CheckComboBox<String> country_sample_origin_combobox;
    private List<String> countries_africa;
    private List<String> countries_americas;
    private List<String> countries_asia;
    private List<String> countries_europe;
    private List<String> countries_oceania;
    private List<String> countries;
    private List<String> countries_antarctica;
    private HashMap<String, List<String>> continent_country_map;
    private Label l_sample_origin_continent;
    private Label l_sample_origin_country;
    private Label label_population;
    private Set<String> population_entries;
    private Set<String> authors_entries;
    private ObservableList<String> continent_list;
    private Label label_authors;
    private Tab sqlConfigTab;
    private Label label_ancient;


    public DatabaseConfigDownloadDialogue(MitoBenchWindow mitoBenchWindow){

        databaseQueryHandler = mitoBenchWindow.getDatabaseQueryHandler();
        mito = mitoBenchWindow;

        root = new BorderPane();

        initContinentFilter();

        fillWindow();
        addAction();

        fillCenterGrid();
        //stage.show();

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

        checkBox_SelectAllData = new CheckBox("Get all data from database");
        checkBox_SelectAllData.setPadding(new Insets(5,5,5,5));

        authors_entries = databaseQueryHandler.getAuthorList();
        List<String> targetList = new ArrayList<>(authors_entries);
        java.util.Collections.sort(targetList);


        label_authors = new Label("Modern/Ancient");
        label_authors.setPadding(new Insets(10,10,10,10));

        label_ancient = new Label("Modern/Ancient");
        label_ancient.setPadding(new Insets(10,10,10,10));
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Modern",
                        "Ancient"
                );
        checkComboBoxModernAncient = new ComboBox(options);


        label_authors = new Label("Author, year");
        label_authors.setPadding(new Insets(10,10,10,10));
        checkComboBoxAuthors = new CheckComboBox<>(FXCollections.observableList(targetList));

        label_population = new Label("Population");
        label_population.setPadding(new Insets(10,10,10,10));
        population_entries = new HashSet<>(databaseQueryHandler.getColumnSet("population", "String"));
        List<String> targetList_pop = new ArrayList<>(population_entries);
        java.util.Collections.sort(targetList_pop);
        population_combobox = new CheckComboBox<>(FXCollections.observableList(targetList_pop));

        label_no_data = new Label("");

        String info_text = "";

        label_info_selected_items = new Label(info_text);

        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        // filling top
        top.setPadding(new Insets(10,10,10,10));
        top.getChildren().add(new Label("Database query configurator\n\n" +
                "This is only a basic filtering based on location/population/publication.\n" +
                "The requested data is displayed in a separate window in which a more detailed filtering is possible."));


        // filling bottom
        bottom.setPadding(new Insets(10,10,10,10));
        bottom.getChildren().addAll(label_info_selected_items, region1, label_no_data, btn_getData, btn_importToMitoBench);


        root.setTop(top);
        root.setCenter(center);
        root.setBottom(bottom);

        sqlConfigTab = new Tab("DB search config");
        sqlConfigTab.setContent(root);

    }

    private void initContinentFilter(){


        continent_list = FXCollections.observableList(new ArrayList<String>(){{add("Africa");
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

        l_sample_origin_continent = new Label("Continent (sample origin):");
        l_sample_origin_continent.setPadding(new Insets(10,10,10,10));

        l_sample_origin_country = new Label("Country (sample origin):");
        l_sample_origin_country.setPadding(new Insets(10,10,10,10));

    }

    private void getData(Task task){
        mito.getProgressBarhandler().activate(task);

        task.setOnCancelled((EventHandler<Event>) event -> {
            mito.getProgressBarhandler().stop();
        });

        task.setOnSucceeded((EventHandler<Event>) event -> {

            if(data_map.size()>0){

                // display duplicates, let user decide which one to keep
                //this.databaseQueryHandler.openDuplicateDecisionMaker(data_map.get(accession), getEntries(map, mapper), accession);

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

        checkBox_SelectAllData.selectedProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue){

                continents_sample_origin_combobox.setDisable(true);
                country_sample_origin_combobox.setDisable(true);
                checkComboBoxAuthors.setDisable(true);
                label_authors.setDisable(true);
                l_sample_origin_continent.setDisable(true);
                l_sample_origin_country.setDisable(true);
                label_population.setDisable(true);
                population_combobox.setDisable(true);

            } else if(!newValue){

                continents_sample_origin_combobox.setDisable(false);
                country_sample_origin_combobox.setDisable(false);
                checkComboBoxAuthors.setDisable(false);
                label_authors.setDisable(false);
                l_sample_origin_continent.setDisable(false);
                l_sample_origin_country.setDisable(false);
                label_population.setDisable(false);
                population_combobox.setDisable(false);
            }

        });




        population_combobox.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) c -> {

            //disable / enable other fields
            if(population_combobox.getCheckModel().getCheckedItems().size()==0){
                checkBox_SelectAllData.setDisable(false);
                continents_sample_origin_combobox.setDisable(false);
                country_sample_origin_combobox.setDisable(false);
                l_sample_origin_continent.setDisable(false);
                l_sample_origin_country.setDisable(false);
                checkComboBoxAuthors.setDisable(false);
                checkComboBoxModernAncient.setDisable(false);
                label_authors.setDisable(false);



            } else {
                checkBox_SelectAllData.setDisable(true);
                continents_sample_origin_combobox.setDisable(true);
                country_sample_origin_combobox.setDisable(true);
                l_sample_origin_continent.setDisable(true);
                l_sample_origin_country.setDisable(true);
                checkComboBoxAuthors.setDisable(true);
                checkComboBoxModernAncient.setDisable(true);
                label_authors.setDisable(true);
            }

        });


        checkComboBoxAuthors.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) c -> {

            //disable / enable other fields
            if(checkComboBoxAuthors.getCheckModel().getCheckedItems().size()==0){
                checkBox_SelectAllData.setDisable(false);
                label_population.setDisable(false);
                population_combobox.setDisable(false);
                continents_sample_origin_combobox.setDisable(false);
                country_sample_origin_combobox.setDisable(false);
                checkComboBoxModernAncient.setDisable(false);
                l_sample_origin_continent.setDisable(false);
                l_sample_origin_country.setDisable(false);


            } else {
                checkBox_SelectAllData.setDisable(true);
                label_population.setDisable(true);
                population_combobox.setDisable(true);
                continents_sample_origin_combobox.setDisable(true);
                country_sample_origin_combobox.setDisable(true);
                l_sample_origin_continent.setDisable(true);
                checkComboBoxModernAncient.setDisable(true);
                l_sample_origin_country.setDisable(true);
            }

        });



        continents_sample_origin_combobox.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) c -> {

            //disable / enable other fields
            if(continents_sample_origin_combobox.getCheckModel().getCheckedItems().size()==0){
                checkBox_SelectAllData.setDisable(false);
                checkComboBoxAuthors.setDisable(false);
                label_authors.setDisable(false);
                label_population.setDisable(false);
                checkComboBoxModernAncient.setDisable(false);
                population_combobox.setDisable(false);
            } else {
                checkBox_SelectAllData.setDisable(true);
                checkComboBoxAuthors.setDisable(true);
                checkComboBoxModernAncient.setDisable(true);
                label_authors.setDisable(true);
                label_population.setDisable(true);
                population_combobox.setDisable(true);
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


            } if(checkComboBoxModernAncient.getSelectionModel().getSelectedItem() != null){
                Task task = new Task() {
                    @Override
                    protected Object call() {
                        String query = "or=(";
                        String selected_item = checkComboBoxModernAncient.getSelectionModel().getSelectedItem();
                        query += "ancient_modern.eq." + selected_item + ")";
                        query = query.replace(" ", "%20");
                        data_map = databaseQueryHandler.getDataSelection(query);
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


            } if(population_combobox.getCheckModel().getCheckedItems().size() > 0){
                Task task = new Task() {
                    @Override
                    protected Object call() {
                        String query = "or=(";
                        ObservableList<String> selected_populations = population_combobox.getCheckModel().getCheckedItems();
                        for(String pop : selected_populations){
                            query += "population.eq." + pop + ",";
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

                ObservableList<String> continents_origin_checked = continents_sample_origin_combobox.getCheckModel().getCheckedItems();

                if (continents_origin_checked.size()>0){
                    for (String continent : continents_origin_checked){
                        query += "sample_origin_region.eq." + continent + ",";
                    }

                }

                if(query.endsWith(",")){
                    query = query.substring(0, query.length() - 1) + ")";
                } else {
                    query += ")";
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

            mito.getTableControllerDB().addRowListener(label_info_selected_items);
        });


        btn_importToMitoBench.setOnAction(e -> {
            label_no_data.setText("Import data into workbench\t");
            HashMap<String, List<Entry>> data = mito.getTableControllerDB().parseDataToEntrylist();
            mito.getTableControllerUserBench().updateTable(data);
            mito.setInfo_selected_items_text(mito.getTableControllerUserBench().getTable().getSelectionModel().getSelectedItems().size() + " / " +
                    mito.getTableControllerUserBench().getTable().getItems().size() +  " rows are selected");
            mito.getTableControllerDB().cleartable();
            mito.getTabpane_statistics().getTabs().remove(sqlConfigTab);
        });



    }


    private void fillCenterGrid(){

        int row = 0;

        // filling center
        center.setPadding(new Insets(10,10,10,10));
        center.add(checkBox_SelectAllData,0,row,3,1);

        center.add(new Separator(),0,++row,3,1);

        center.add(new Separator(),0,++row,3,1);

        center.add(l_sample_origin_continent, 0,++row,1,1);
        center.add(continents_sample_origin_combobox, 1,row,2,1);

        center.add(new Separator(),0,++row,3,1);
        center.add(label_ancient, 0,++row,1,1);
        center.add(checkComboBoxModernAncient, 1,row,2,1);

        center.add(new Separator(),0,++row,3,1);
        center.add(label_authors, 0,++row,1,1);
        center.add(checkComboBoxAuthors, 1,row,2,1);

        center.add(new Separator(), 0,++row,3,1);
        center.add(label_population,0, ++row, 1, 1);
        center.add(population_combobox,1, row, 2, 1);


    }

    public Button getBtn_importToMitoBench() {
        return btn_importToMitoBench;
    }
    public Label getLabel_no_data() {
        return label_no_data;
    }
    public Tab getSqlConfigTab() {
        return sqlConfigTab;
    }
}
