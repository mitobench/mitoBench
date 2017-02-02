package database;

import io.datastructure.Entry;
import io.datastructure.generic.GenericInputData;
import io.inputtypes.CategoricInputType;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 02.02.17.
 */
public class DataAccessor {

    // in real life, use a connection pool....
    private Connection connection ;
    private String rowID;

    public DataAccessor() { }

    public void connectToDatabase(String driverClassName, String dbURL, String user, String password)throws SQLException, ClassNotFoundException {

        Class.forName(driverClassName);
        connection = DriverManager.getConnection(dbURL, user, password);

    }


    public void shutdown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }


    public HashMap<String, List<Entry>>  getEntries(String query) throws SQLException {
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
