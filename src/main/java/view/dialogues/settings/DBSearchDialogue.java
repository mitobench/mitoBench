package view.dialogues.settings;

import controller.DatabaseConnectionController;
import database.DatabaseQueryHandler;
import io.datastructure.Entry;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import view.MitoBenchWindow;
import controller.ATableController;
import Logging.LogClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;


/**
 * Created by neukamm on 09.02.17.
 */
public class DBSearchDialogue extends ATabpaneDialogue {


    private MitoBenchWindow mito;
    private DatabaseConnectionController databaseConnectionController;
    private TextField textfield_selection_table;
    private TextField textfield_sql_statement_advanced;
    private Label message;
    private Button btnSend;
    private CheckBox checkBox_write_own_query;
    private CheckBox checkBox_get_all_data;
    private LogClass logClass;

    private Connection connection ;


    public DBSearchDialogue(String title, MitoBenchWindow mitoBenchWindow,
                            DatabaseConnectionController databaseConnectionController){
        super(title, mitoBenchWindow.getLogClass());


        logClass = mitoBenchWindow.getLogClass();

        mito = mitoBenchWindow;
        this.databaseConnectionController = databaseConnectionController;
        //dialogGrid.setId("configure_sql_query");

        checkBox_write_own_query = new CheckBox("Own SQL statement");
        checkBox_get_all_data = new CheckBox("Get all data from mitoDB");
        textfield_selection_table = new TextField();
        textfield_sql_statement_advanced = new TextField("Enter your own SQL statement here.");
        message = new Label();
        btnSend = new Button("Send");

        // set IDs
        dialogGrid.setId("set_database_query");
        textfield_selection_table.setId("textfield_selection_table");
        textfield_sql_statement_advanced.setId("textfield_sql_statement_advanced");
        btnSend.setId("db_sendBtn");


        checkBox_write_own_query.setSelected(false);
        textfield_sql_statement_advanced.setEditable(false);
        textfield_sql_statement_advanced.setDisable(true);

    }



    public void fillDialogue() {

        TabPane base = new TabPane();

        Tab tab_geo = new Tab();
        tab_geo.setText("Geo filter");

        Tab tab_getall = new Tab();
        tab_geo.setText("Get all");

        base.getTabs().addAll(tab_getall, tab_geo);

        int rowindex=0;
        dialogGrid.add(checkBox_get_all_data, 0,rowindex,2,1);
        dialogGrid.add(new Separator(), 0,++rowindex,3,1);
        dialogGrid.add(btnSend,2,++rowindex,3,1);
        dialogGrid.add(message, 0,++rowindex,2,1);

    }

    public void addFunctionality(ATableController tablecontroller) {

        btnSend.setOnAction(e ->
            performSendAction(tablecontroller)
        );


        checkBox_write_own_query.selectedProperty().addListener((ov, old_val, new_val) -> {
            if(new_val){
                textfield_sql_statement_advanced.setEditable(true);
                textfield_sql_statement_advanced.setDisable(false);
            } else {
                textfield_sql_statement_advanced.setEditable(false);
                textfield_sql_statement_advanced.setDisable(true);
            }
        });

        textfield_sql_statement_advanced.setOnMouseClicked(e ->
                textfield_sql_statement_advanced.clear());
    }


    private void performSendAction(ATableController tablecontroller){

        DatabaseQueryHandler databaseQueryHandler = new DatabaseQueryHandler();
        if(nothingSelected()){
            System.out.println("Nothing selected!");
        } else if(checkBox_get_all_data.isSelected()){
            HashMap<String, List<Entry>> data_map = databaseQueryHandler.getAllData();

            logClass.getLogger(this.getClass()).info("Import data from mitoDB.\nQuery: ");
            tablecontroller.updateTable(data_map);
            mito.splitTablePane(mito.getTableControllerDB());
            mito.getTabpane_statistics().getTabs().remove(getTab());
            mito.getTableControllerDB().addFilter();

        } else if(!checkBox_get_all_data.isSelected()){

        }


    }



    private boolean nothingSelected() {

        if(!checkBox_get_all_data.isSelected())
            return true;
        else
            return false;
    }

}
