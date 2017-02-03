package view.dialogues.popup;

import database.DataAccessor;
import io.datastructure.Entry;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.table.ATableController;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 02.02.17.
 */
public class DatabaseConnectionDialogue {


    private GridPane dialogVbox;
    private Stage dialog;
    private Button loginBtn;
    private String username;
    private String password;
    private TextField password_field;
    private TextField usernamme_field;
    private HashMap<String, List<Entry>> data;
    private  ATableController table;

    public DatabaseConnectionDialogue(ATableController tableUserDB){
        table = tableUserDB;
        dialog = new Stage();
        dialog.setTitle("Database Login");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(new Stage());

        dialogVbox = new GridPane();
        dialogVbox.setHgap(10);
        dialogVbox.setVgap(10);
        dialogVbox.setId("connect_to_database");
        setComponents();
        addListener();
        show();

    }

    private void setComponents(){
        Label title_label = new Label("Connect to database");
        Label username_label = new Label("Username");
        Label password_label = new Label("Password");
        usernamme_field = new TextField();
        password_field = new TextField();
        usernamme_field.setText("mitodbreader");
        password_field.setText("*****************");
        loginBtn = new Button("Login");

        dialogVbox.add(title_label, 0,0,2,1);
        dialogVbox.add(username_label,0,1,1,1);
        dialogVbox.add(usernamme_field,1,1,1,1);
        dialogVbox.add(password_label,0,2,1,1);
        dialogVbox.add(password_field,1,2,1,1);
        dialogVbox.add(loginBtn,1,3,2,1);

    }


    private void addListener(){
        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                username = usernamme_field.getText();
                password = password_field.getText();

                //For testing and stuff
                usernamme_field.setText("mitodbreader");
                password_field.setText("*****************");
                password = "LKeAFGVqSZdtr8peTPOv";
                username = "mitodbreader";

                DataAccessor accessor = new DataAccessor();
                try {
                    accessor.connectToDatabase("org.postgresql.Driver",
                            "jdbc:postgresql://peltzer.com.de:5432/mitoDbTesting",
                            username, password);
                    data = accessor.getEntries("select * from sequence_data");
                    table.updateTable(data);

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
                dialog.close();
            }
        });
    }




    /**
     * This method displays dialogue.
     */
    private void show(){
        Scene dialogScene = new Scene(dialogVbox);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public HashMap<String, List<Entry>> getData() {
        return data;
    }
}
