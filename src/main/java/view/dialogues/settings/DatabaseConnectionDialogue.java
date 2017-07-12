package view.dialogues.settings;

import Logging.LogClass;
import controller.DatabaseConnectionController;
import io.Exceptions.DatabaseConnectionException;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import view.MitoBenchWindow;
import view.dialogues.error.DatabaseErrorDialogue;
import view.table.controller.ATableController;

import java.sql.SQLException;

/**
 * Created by neukamm on 02.02.17.
 */
public class DatabaseConnectionDialogue {
    private DatabaseConnectionController databaseConnectionController;//extends APopupDialogue{


    private Button loginBtn;
    private String username;
    private String password;
    PasswordField password_field;
    private TextField usernamme_field;
    private  ATableController table;
    private MitoBenchWindow mitoBenchWindow;
    private GridPane dialogGrid;
    private LogClass logClass;
    private String username_root;
    private String password_root;

    public DatabaseConnectionDialogue(ATableController tableUserDB, LogClass logClass, MitoBenchWindow mito, Tab tab,
                                      DatabaseConnectionController databaseConnectionController){
        mitoBenchWindow = mito;
        this.databaseConnectionController = databaseConnectionController;
        table = tableUserDB;
        this.logClass = logClass;
        setComponents();
        addListener(tab);


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

        password_field = new PasswordField();
        password_field.setId("password_field");

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

            mitoBenchWindow.getTabpane_statistics().getTabs().remove(tab);

            databaseConnectionController.setPassword(password);
            databaseConnectionController.setUserName(username);
            databaseConnectionController.setTable(table);


            try {
                databaseConnectionController.login();
                // open search mask to specify which data should be loaded
                DBSearchDialogue dbSearchDialogue = new DBSearchDialogue(
                        "SQL statement configurator",
                        mitoBenchWindow,
                        databaseConnectionController);
                dbSearchDialogue.fillDialogue();
                dbSearchDialogue.addFunctionality(username, password, table);

            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            } catch (DatabaseConnectionException e1) {
                databaseConnectionController.setLoggedIn(false);
                DatabaseErrorDialogue databaseErrorDialogue = new DatabaseErrorDialogue();
            }

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

    public String getUsername_root() {
        return username_root;
    }

    public String getPassword_root() {
        return password_root;
    }
}
