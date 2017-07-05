package view.dialogues.settings;

import Logging.LogClass;
import controller.DatabaseConnectionController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import view.MitoBenchWindow;
import view.table.controller.ATableController;

/**
 * Created by neukamm on 02.02.17.
 */
public class DatabaseConnectionDialogue {
    private final DatabaseConnectionController databaseConnectionController;//extends APopupDialogue{


    private Button loginBtn;
    private String username;
    private String password;
    private TextField password_field;
    private TextField usernamme_field;
    private  ATableController table;
    private MitoBenchWindow mitoBenchWindow;
    private GridPane dialogGrid;
    private LogClass logClass;

    public DatabaseConnectionDialogue(ATableController tableUserDB, LogClass logClass, MitoBenchWindow mito, Tab tab,
                                      DatabaseConnectionController databaseConnectionController){
        //super(title, logClass);
        mitoBenchWindow = mito;
        this.databaseConnectionController = databaseConnectionController;
        table = tableUserDB;
        this.logClass = logClass;
        setComponents();
        addListener(tab);
        //show();

    }

    private void setComponents(){
        dialogGrid = new GridPane();
        dialogGrid.setAlignment(Pos.CENTER);
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);
        dialogGrid.setId("connect_to_database");

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


    private void addListener(Tab tab){
        loginBtn.setOnAction(e -> {
            username = usernamme_field.getText();
            password = password_field.getText();

            //For testing and stuff
            usernamme_field.setText("mitodbreader");
            password_field.setText("*****************");
            password = "LKeAFGVqSZdtr8peTPOv";
            username = "mitodbreader";

            mitoBenchWindow.getTabpane_statistics().getTabs().remove(tab);

            databaseConnectionController.setPassword(password);
            databaseConnectionController.setUserName(username);
            databaseConnectionController.setTable(table);

            // open search mask to specify which data should be loaded
            DBSearchDialogue dbSearchDialogue = new DBSearchDialogue("SQL statement configurator", mitoBenchWindow);
            dbSearchDialogue.fillDialogue();
            dbSearchDialogue.addFunctionality(username, password, table);
        });
    }


    public GridPane getDialogGrid() {
        return dialogGrid;
    }

    public Button getLoginBtn() {
        return loginBtn;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }



}
