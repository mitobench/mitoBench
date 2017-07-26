package database;

import io.datastructure.Entry;
import io.datastructure.generic.GenericInputData;
import io.inputtypes.CategoricInputType;
import view.dialogues.error.DatabaseErrorDialogue;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 02.02.17.
 */
public class DatabaseAccessor {

    private Connection connection ;
    private String rowID;

    public DatabaseAccessor() { }

    public boolean connectToDatabase(String driverClassName, String dbURL, String username_root, String password_root,
                                     String username, String password) throws SQLException, ClassNotFoundException {

        connection = null;
        String dbUsername, dbPassword;
        String query;
        boolean login = false;

        Class.forName(driverClassName);
        try{
            connection = DriverManager.getConnection(dbURL, username_root, password_root);
            //connection = DriverManager.getConnection(dbURL, username, password);
            Statement stmt = connection.createStatement();
            query = "SELECT alias, passwd FROM mitodb_users;";
            stmt.executeQuery(query);
            ResultSet rs = stmt.getResultSet();

            while(rs.next()){
                dbUsername = rs.getString("alias");
                dbPassword = rs.getString("passwd");

                if(dbUsername.equals(username) && dbPassword.equals(password)){
                    login = true;
                }
            }
            return login;

        } catch (Exception e){
            DatabaseErrorDialogue databaseErrorDialogue = new DatabaseErrorDialogue();
            return false;
        }

    }


    public void shutdown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }


    public HashMap<String, List<Entry>> getEntries(String query) throws SQLException {
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery(query)
        ){
            HashMap<String, List<Entry>> data = new HashMap<>();
            while (rs.next()) {
                List entries = getTableData(rs);
                data.put(rowID, entries);
            }
            return data ;
        }
    }


    private List<Entry> getTableData(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        List<Entry> eList = new ArrayList<>();
        Entry e;
        for(int i = 1; i < rsmd.getColumnCount(); i++){
            String col = rsmd.getColumnName(i);
            if(col.equals("id")){
                rowID = rs.getString(i);
            } else if(!col.equals("mitodb_entry_identifier")) {
              String data_string = rs.getString(i);
              e = new Entry(col, new CategoricInputType("String"), new GenericInputData(data_string));
              eList.add(e);
          }
        }

        return eList;
    }

}
