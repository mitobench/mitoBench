package view.dialogues.settings;

import Logging.LogClass;
import database.DataAccessor;
import io.datastructure.Entry;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.Select;
import view.MitoBenchWindow;
import view.table.controller.ATableController;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;


/**
 * Created by neukamm on 09.02.17.
 */
public class DBSearchDialogue {


    private final MitoBenchWindow mito;
    private final GridPane dialogGrid;
    private final Tab tab;
    private TextField textfield_selection_table;
    private TextField textfield_sql_statement_advanced;
    private Label message;
    private Button btnSend;
    private CheckBox checkBox_write_own_query;
    private CheckBox checkBox_get_all_data;
    private HashMap<String, List<Entry>> data;
    private LogClass logClass;

    public DBSearchDialogue(String title, MitoBenchWindow mitoBenchWindow){
        //super(title, mitoBenchWindow.getLogClass());
        logClass = mitoBenchWindow.getLogClass();
        mito = mitoBenchWindow;
        dialogGrid = new GridPane();
        dialogGrid.setAlignment(Pos.CENTER);
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);
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
        //dialog.setResizable(true);

        //show(600,450);

        // add to tab
        tab = new Tab(title);
        tab.setContent(dialogGrid);
        mitoBenchWindow.getTabpane_statistics().getTabs().add(tab);
    }


    public void fillDialogue() {
        dialogGrid.add(checkBox_get_all_data, 0,0,2,1);

        dialogGrid.add(new Separator(), 0,1,3,1);

        dialogGrid.add(new Label("select"), 0,2,1,1);
        dialogGrid.add(textfield_selection_table, 1,2,1,1);
        dialogGrid.add(new Label("from sequence_data"), 2,2,1,1);

        dialogGrid.add(new Separator(), 0,3,3,1);

        dialogGrid.add(checkBox_write_own_query, 0,4,1,1);
        dialogGrid.add(textfield_sql_statement_advanced, 0,5,3,1);
        dialogGrid.add(btnSend,2,6,3,1);
        dialogGrid.add(message, 0,6,2,1);

    }

    public void addFunctionality(String username, String password, ATableController tablecontroller) {

        btnSend.setOnAction(e ->
            performSendAction(username, password, tablecontroller)
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

    private void performSendAction(String username, String password, ATableController tablecontroller){
        DataAccessor accessor = new DataAccessor();
        try {
            accessor.connectToDatabase("org.postgresql.Driver",
                    "jdbc:postgresql://peltzer.com.de:5432/mitoDbTesting",
                    username, password);

            String query;
            if(checkBox_write_own_query.isSelected()){
                query = textfield_sql_statement_advanced.getText();
            } else {
                query = "SELECT " + textfield_selection_table.getText() + "FROM sequence_data";
            }


            CCJSqlParserManager pm = new CCJSqlParserManager();
            try {
                net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(query));
                if(isQueryValid(statement)) {
                    logClass.getLogger(this.getClass()).info("Import data from mitoDB. Query: " + query);
                    data = accessor.getEntries(query);
                    message.setText("");
                    tablecontroller.updateTable(data);
                    //dialog.close();
                    mito.splitTablePane(mito.getTableControllerDB());
                    mito.getTabpane_statistics().getTabs().remove(tab);
                }
            } catch (JSQLParserException e) {
                message.setTextFill(Color.RED);
                message.setText("Query was not correct");
            }



        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        try {
            accessor.shutdown();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

    }
}
