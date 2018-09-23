package view.dialogues.settings;

import Logging.LogClass;
import controller.DatabaseConnectionController;
import io.Exceptions.DatabaseConnectionException;
import javafx.scene.control.*;
import view.MitoBenchWindow;
import view.dialogues.error.DatabaseErrorDialogue;
import controller.ATableController;

import java.sql.SQLException;

/**
 * Created by neukamm on 02.02.17.
 */
public class DatabaseConnectionDialogue extends ATabpaneDialogue {
    private DatabaseConnectionController databaseConnectionController;
    private Button loginBtn;
    private String username;
    private String password;
    private PasswordField password_field;
    private TextField username_field;
    private  ATableController table;
    private MitoBenchWindow mitoBenchWindow;

    public DatabaseConnectionDialogue(String title, ATableController tableUserDB, LogClass logClass, MitoBenchWindow mito,
                                      DatabaseConnectionController databaseConnectionController){
        super(title, logClass);
        mitoBenchWindow = mito;
        this.databaseConnectionController = databaseConnectionController;
        table = tableUserDB;
        LOG = logClass.getLogger(this.getClass());
        setComponents();
        addListener();


    }

    private void setComponents(){
        dialogGrid.setId("connect_to_database");

        Label title_label = new Label("Connect to database");
        title_label.setId("title_label");
        Label username_label = new Label("Username");
        username_label.setId("username_label");

        Label password_label = new Label("Password");
        password_label.setId("password_label");

        username_field = new TextField();
        username_field.setId("username_field");

        password_field = new PasswordField();
        password_field.setId("password_field");

        // test user
        username_field.setText("mitotest");
        password_field.setText("$2a$10$gsy2KTn6xznKETjG1AHcMOLC.6YNtkikttyOzNBP/2B2GMQSW6m4e");

        loginBtn = new Button("Login");
        loginBtn.setId("loginButton");

        dialogGrid.add(title_label, 0,0,2,1);
        dialogGrid.add(username_label,0,1,1,1);
        dialogGrid.add(username_field,1,1,1,1);
        dialogGrid.add(password_label,0,2,1,1);
        dialogGrid.add(password_field,1,2,1,1);
        dialogGrid.add(loginBtn,1,3,2,1);

    }


    private void addListener(){
        loginBtn.setOnAction(e -> {
            username = username_field.getText();
            password = password_field.getText();

            mitoBenchWindow.getTabpane_statistics().getTabs().remove(getTab());

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
                dbSearchDialogue.addFunctionality(table);
                mitoBenchWindow.getTabpane_statistics().getTabs().add(dbSearchDialogue.getTab());
                mitoBenchWindow.getTabpane_statistics().getSelectionModel().select(dbSearchDialogue.getTab());
                LOG.info("Login to database with username " + databaseConnectionController.getUserName()+ " successful.");

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

}
