package view.dialogues.settings;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import controller.DatabaseConnectionController;
import database.ColumnNameMapper;
import database.DatabaseAccessor;
import database.JsonDataParser;
import io.datastructure.Entry;
import io.datastructure.generic.GenericInputData;
import io.inputtypes.CategoricInputType;
import javafx.scene.control.*;
import net.sf.jsqlparser.statement.select.Select;
import org.json.JSONObject;
import view.MitoBenchWindow;
import controller.ATableController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


/**
 * Created by neukamm on 09.02.17.
 */
public class DBSearchDialogue extends ATabpaneDialogue{


    private final MitoBenchWindow mito;
    private final DatabaseConnectionController databaseConnectionController;
    private TextField textfield_selection_table;
    private TextField textfield_sql_statement_advanced;
    private Label message;
    private Button btnSend;
    private CheckBox checkBox_write_own_query;
    private CheckBox checkBox_get_all_data;
    private JsonDataParser jsonDataParser = new JsonDataParser();

    public DBSearchDialogue(String title, MitoBenchWindow mitoBenchWindow,
                            DatabaseConnectionController databaseConnectionController){

        super(title, mitoBenchWindow.getLogClass());
        mito = mitoBenchWindow;
        this.databaseConnectionController = databaseConnectionController;
        dialogGrid.setId("configure_sql_query");

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
        dialogGrid.add(checkBox_get_all_data, 0,0,2,1);

        dialogGrid.add(new Separator(), 0,1,3,1);

//        dialogGrid.add(new Label("select"), 0,2,1,1);
//        dialogGrid.add(textfield_selection_table, 1,2,1,1);
//        dialogGrid.add(new Label("from sequence_data"), 2,2,1,1);
//
//        dialogGrid.add(new Separator(), 0,3,3,1);
//
//        dialogGrid.add(checkBox_write_own_query, 0,4,1,1);
//        dialogGrid.add(textfield_sql_statement_advanced, 0,5,3,1);
        dialogGrid.add(btnSend,2,2,3,1);
        dialogGrid.add(message, 0,3,2,1);

    }

    public void addFunctionality(ATableController tablecontroller) {

        btnSend.setOnAction(e ->
            performSendAction(tablecontroller, databaseConnectionController)
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


    private void performSendAction(ATableController tablecontroller, DatabaseConnectionController databaseConnectionController){
        try {

            final HttpResponse<JsonNode> response = Unirest.get("http://ec2-54-173-159-49.compute-1.amazonaws.com:3000/meta").asJson();
            HashMap<String, List<Entry>> data_map = jsonDataParser.getData(response);

            logClass.getLogger(this.getClass()).info("Import data from mitoDB.\nQuery: ");
            tablecontroller.updateTable(data_map);
            mito.splitTablePane(mito.getTableControllerDB());
            mito.getTabpane_statistics().getTabs().remove(getTab());
            mito.getTableControllerDB().addFilter();


        } catch (UnirestException e1) {
            e1.printStackTrace();
        }

    }

}
