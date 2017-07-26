package controller;

import database.DatabaseAccessor;
import io.Exceptions.DatabaseConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

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
        //For testing and stuff
        username_root = "mitodbreader";
        password_root = "LKeAFGVqSZdtr8peTPOv";
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
        loggedIn = databaseAccessor.connectToDatabase("org.postgresql.Driver",
                "jdbc:postgresql://peltzer.com.de:5432/mitoDbTesting",
                username_root, password_root, userName, password);

        if(!loggedIn){
            throw new DatabaseConnectionException();
        }

    }


    public ObservableList<String> getTableAttributes(String tablename) throws SQLException {

        String query;
        Connection connection = databaseAccessor.getConnection();
        Statement stmt = connection.createStatement();
        query = "SELECT * FROM " + tablename;
        stmt.executeQuery(query);
        ResultSet rs = stmt.getResultSet();
        ResultSetMetaData rsmd = rs.getMetaData();

        ObservableList col_names = FXCollections.observableArrayList();
        for (int i = 1; i <= rsmd.getColumnCount(); i++)
            col_names.add(rsmd.getColumnName(i));

        return col_names;

    }


    public DatabaseAccessor getDatabaseAccessor() {
        return databaseAccessor;
    }
}
