package view.dialogues.popup;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import view.table.ATableController;

/**
 * Created by neukamm on 02.02.17.
 */
public class DatabaseConnectionDialogue extends APopupDialogue{


    private Button loginBtn;
    private String username;
    private String password;
    private TextField password_field;
    private TextField usernamme_field;
    private  ATableController table;
    private Boolean loggedIn = false;

    public DatabaseConnectionDialogue(ATableController tableUserDB, String title){
        super(title);
        table = tableUserDB;
        dialogGrid.setId("connect_to_database");
        setComponents();
        addListener();
        show();

    }

    private void setComponents(){
        Label title_label = new Label("Connect to database");
        title_label.setId("title_label");
        Label username_label = new Label("Username");
        username_label.setId("username_label");

        Label password_label = new Label("Password");
        password_label.setId("password_label");

        usernamme_field = new TextField();
        usernamme_field.setId("usernamme_field");

        password_field = new TextField();
        password_field.setId("password_field");

        usernamme_field.setText("mitodbreader");
        password_field.setText("*****************");

        loginBtn = new Button("Login");
        loginBtn.setId("loginButton");

        dialogGrid.add(title_label, 0,0,2,1);
        dialogGrid.add(username_label,0,1,1,1);
        dialogGrid.add(usernamme_field,1,1,1,1);
        dialogGrid.add(password_label,0,2,1,1);
        dialogGrid.add(password_field,1,2,1,1);
        dialogGrid.add(loginBtn,1,3,2,1);

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

                // open search mask to specify which data should be loaded
                DBSearchDialogue dbSearchDialogue = new DBSearchDialogue("DB Search mask");
                dbSearchDialogue.fillDialogue();
                dbSearchDialogue.addButtonFunc(username, password, table);
                dialog.close();

            }
        });
    }


}
