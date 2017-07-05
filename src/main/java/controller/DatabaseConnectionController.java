package controller;

import view.table.controller.ATableController;

/**
 * Created by neukamm on 05.07.17.
 */
public class DatabaseConnectionController {

    private Boolean loggedIn;
    private String userName;
    private String password;
    private ATableController table;

    public DatabaseConnectionController(){}

    public Boolean getLoggedIn() {
        return loggedIn;
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

    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
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
}
