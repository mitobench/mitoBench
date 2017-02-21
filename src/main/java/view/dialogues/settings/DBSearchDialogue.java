package view.dialogues.settings;

import database.DataAccessor;
import io.datastructure.Entry;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import view.table.ATableController;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 09.02.17.
 */
public class DBSearchDialogue extends APopupDialogue{


    private TextField textfield_selection_table = new TextField();
    private Label message = new Label("");
    private Button btnSend = new Button("Send");
    private HashMap<String, List<Entry>> data;

    public DBSearchDialogue(String title){
        super(title);
        dialogGrid.setId("set_database_query");
        dialog.setResizable(true);

        textfield_selection_table.setId("textfield_selection_table");
        btnSend.setId("db_sendBtn");
        show();
    }


    public void fillDialogue() {
        dialogGrid.add(new Label("Please complete search query"), 0,0,3,1);
        dialogGrid.add(new Label("select"), 0,1,1,1);
        dialogGrid.add(textfield_selection_table, 1,1,1,1);
        dialogGrid.add(new Label("from sequence_data"), 2,1,1,1);
        dialogGrid.add(btnSend,0,2,1,1);
        dialogGrid.add(message, 2,2,1,1);

    }

    public void addButtonFunc(String username, String password, ATableController table) {
        btnSend.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                DataAccessor accessor = new DataAccessor();
                try {
                    accessor.connectToDatabase("org.postgresql.Driver",
                            "jdbc:postgresql://peltzer.com.de:5432/mitoDbTesting",
                            username, password);

                    String query = "select " + textfield_selection_table.getText() + " from sequence_data";
                    if(isQueryValid(query)){
                        data = accessor.getEntries(query);
                        message.setText("");
                        table.updateTable(data);
                        dialog.close();
                    } else {
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
        });

    }

    private boolean isQueryValid(String query) {
        // todo: verify user input
        return true;
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
}
