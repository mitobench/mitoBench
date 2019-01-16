package controller;

import database.DatabaseAccessor;
import io.Exceptions.DatabaseConnectionException;

import java.sql.SQLException;

/**
 * Created by neukamm on 05.07.17.
 */
public class DatabaseConnectionController {

    private final String username_root;
    private final String password_root;
    private boolean loggedIn;
    private String userName;
    private String password;
    private ATableController table;
    private DatabaseAccessor databaseAccessor;

    public DatabaseConnectionController(){
        //For accessing database
        username_root = "mito_user";
        password_root = "a4cxkghaZ";
    }


    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public ATableController getTable() {
        return table;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTable(ATableController table) {
        this.table = table;
    }

    public void login() throws SQLException, ClassNotFoundException, DatabaseConnectionException {

        databaseAccessor = new DatabaseAccessor();
        loggedIn = true;//databaseAccessor.connectToDatabase("org.postgresql.Driver",
                //"jdbc:postgres://mito_user:a4cxkghaZ@db:5432/mito_db",
                //username_root, password_root);

        if(!loggedIn){
            throw new DatabaseConnectionException();
        }

    }

    public DatabaseAccessor getDatabaseAccessor() {
        return databaseAccessor;
    }
}
