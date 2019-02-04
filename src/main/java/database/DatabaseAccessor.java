package database;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import io.datastructure.Entry;
import io.datastructure.generic.GenericInputData;
import io.inputtypes.CategoricInputType;
import org.json.JSONObject;

import java.sql.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by neukamm on 02.02.17.
 */
public class DatabaseAccessor {

    private Connection connection ;
    private String rowID;

    public DatabaseAccessor() { }

    public boolean connectToDatabase(String driverClassName, String dbURL, String username_root, String password_root) throws ClassNotFoundException {


        try {

            //final HttpResponse<JsonNode> response = Unirest.get("http://ec2-54-173-159-49.compute-1.amazonaws.com:3000/meta").asJson();
            final HttpResponse<JsonNode> response = Unirest.get("http://mitodb.org/meta").asJson();
            for (int i = 0; i < response.getBody().getArray().length(); i++){
                JSONObject map = (JSONObject) response.getBody().getArray().get(i);
                Set<String> keys = map.keySet();
                System.out.println();
            }

            System.out.println();


        } catch (UnirestException e) {
            e.printStackTrace();
        }

        connection = null;
        String dbUsername, dbPassword;
        String query;
        boolean login = false;

        Class.forName(driverClassName);
        try{
            connection = DriverManager.getConnection(dbURL, username_root, password_root);
           // connection = DriverManager.getConnection(dbURL);
            //connection = DriverManager.getConnection(dbURL, "mitodbreader", "$MitoRead17");
//            Statement stmt = connection.createStatement();
//            query = "SELECT user_alias, passwd FROM mitodb_users;";
//            stmt.executeQuery(query);
//            ResultSet rs = stmt.getResultSet();
//
//            while(rs.next()){
//                dbUsername = rs.getString("user_alias");
//                dbPassword = rs.getString("passwd");
//
//                if(dbUsername.equals(username) && dbPassword.equals(password)){
//                    login = true;
//                }
//            }
//            return login;
            return true;


        } catch (Exception e){
            //DatabaseErrorDialogue databaseErrorDialogue = new DatabaseErrorDialogue();
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
        )
        {
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
            if(col.equals("accession_id")){
                rowID = rs.getString(i);
            } else {
              String data_string = rs.getString(i);
              e = new Entry(col, new CategoricInputType("String"), new GenericInputData(data_string));
              eList.add(e);
          }
        }

        return eList;
    }

}
