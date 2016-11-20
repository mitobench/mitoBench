package view.table;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

import io.datastructure.Entry;
import io.reader.MultiFastAInput;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author Narayan
 */

public class DynamicTable extends Application{

    //TABLE VIEW AND DATA
    private ObservableList<ObservableList> data;
    private TableView tableview;

    //MAIN EXECUTOR
    public static void main(String[] args) {
        launch(args);
    }

    //CONNECTION DATABASE
    public void buildData() throws IOException{
        Connection c ;
        data = FXCollections.observableArrayList();



        TableColumn colID = new TableColumn("ID");
        colID.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().toString());
            }
        });

        tableview.getColumns().addAll(colID);




        MultiFastAInput multiFastAInput = new MultiFastAInput("C:\\Users\\neukamm\\Dropbox\\MitoBench\\src\\main\\resources\\multiFastaTest.fa");
        HashMap<String, List<Entry>> input_multifasta = multiFastAInput.getCorrespondingData();
        try{

            for(String s : input_multifasta.keySet()){


                // add col if not exiting
                //idExists(s, tableview);
                /*TableColumn colID = new TableColumn(s);
                colID.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().toString());
                    }
                });

                tableview.getColumns().addAll(colID);*/


                List<Entry> entryList = input_multifasta.get(s);
                /**********************************
                 * TABLE COLUMN ADDED DYNAMICALLY *
                 **********************************/
                for(int i=0 ; i < entryList.size(); i++){
                    //We are using non property style for making dynamic table
                    final int j = i;

                    if(i<2){
                        TableColumn col = new TableColumn(entryList.get(i).getIdentifier());
                        col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                                return new SimpleStringProperty(param.getValue().get(j).toString());
                            }
                        });

                        tableview.getColumns().addAll(col);

                    }

                    System.out.println("Column ["+i+"] ");
                }

                /********************************
                 * Data added to ObservableList *
                 ********************************/
                int counter = 0;
                while(counter < entryList.size()) {
                    //Iterate Row
                    ObservableList<String> row = FXCollections.observableArrayList();
                    row.add(s);
                    for (int i = 0; i < entryList.size(); i++) {
                        //Iterate Column
                        row.add((String) entryList.get(i).getData());
                    }
                    System.out.println("Row [1] added " + row);
                    data.add(row);
                    counter++;

                }

            }

            //FINALLY ADDED TO TableView
            tableview.setItems(data);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }


    @Override
    public void start(Stage stage) throws Exception {
        //TableView
        tableview = new TableView();
        buildData();

        //Main Scene
        Scene scene = new Scene(tableview);

        stage.setScene(scene);
        stage.show();
    }



}
