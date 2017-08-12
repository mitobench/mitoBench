package view.dialogues.settings;

import controller.DatabaseConnectionController;
import database.DatabaseAccessor;
import io.datastructure.Entry;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.Select;
import view.MitoBenchWindow;
import controller.ATableController;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;


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
    private HashMap<String, List<Entry>> data;

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

    private boolean isQueryValid(net.sf.jsqlparser.statement.Statement statement) {
        // todo: verify user input
        if (statement instanceof Select) {
            return true;
        }
       return false;

    }

    private boolean dataEmpty() {
        if(data.keySet().size()==1){
            for(String key : data.keySet()){
                if(key==null)
                    return true;
            }
        }

        return false;
    }

    private void performSendAction(ATableController tablecontroller, DatabaseConnectionController databaseConnectionController){
        DatabaseAccessor accessor = databaseConnectionController.getDatabaseAccessor();
        try {

            String query;
            if(checkBox_write_own_query.isSelected()){
                query = textfield_sql_statement_advanced.getText();
            } else if (checkBox_get_all_data.isSelected()){
                //query = "SELECT * FROM sequence_data";
                query = "SELECT * FROM samples s " +
                        "INNER JOIN publications p on s.publications = p.mitodb_publications_id " +
                        "INNER JOIN technical_information t on s.technicalinfo_id = t.technical_info_id";
            } else {
                query = "SELECT " + textfield_selection_table.getText() + "FROM sequence_data";
            }

            CCJSqlParserManager pm = new CCJSqlParserManager();
            try {
                net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(query));
                if(isQueryValid(statement)) {
                    logClass.getLogger(this.getClass()).info("Import data from mitoDB.\nQuery: " + query);
                    data = accessor.getEntries(query);
                    message.setText("");
                    tablecontroller.updateTable(data);
                    //dialog.close();
                    mito.splitTablePane(mito.getTableControllerDB());
                    mito.getTabpane_statistics().getTabs().remove(getTab());
                    mito.getTableControllerDB().addFilter();
                }
            } catch (JSQLParserException e) {
                message.setTextFill(Color.RED);
                message.setText("Query was not correct");
            }

        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        try {
            accessor.shutdown();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

    }
}
