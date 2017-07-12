package view.menus;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import org.apache.log4j.Logger;
import view.MitoBenchWindow;
import controller.GroupController;
import view.table.controller.TableControllerUserBench;


/**
 * Created by neukamm on 23.11.16.
 */
public class TableMenu {

    private Menu menuTable;
    private TableControllerUserBench tableController;
    private GroupController groupController;
    private Logger LOG;

    public TableMenu(MitoBenchWindow mitoBenchWindow){
        this.groupController = mitoBenchWindow.getGroupController();
        menuTable = new Menu("Table");
        menuTable.setId("tableMenu");
        this.tableController = mitoBenchWindow.getTableControllerUserBench();
        LOG = mitoBenchWindow.getLogClass().getLogger(this.getClass());
        addSubMenus();
    }


    private void addSubMenus(){



        /*
                        Get selected rows

         */

        MenuItem getSelectedRows = new MenuItem("Get selected rows");
        getSelectedRows.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try{

                    ObservableList<ObservableList> selection = tableController.getTable().getSelectionModel().getSelectedItems();
                    int index_ID = tableController.getColIndex("ID");
                    String log = "Get only selected rows: ";

                    for(int i = 0; i < selection.size(); i++){
                        log += selection.get(i).get(index_ID) + " ";
                    }
                    LOG.info(log);
                    tableController.updateView(selection);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });



        /*
                       Select all rows

         */

        MenuItem selectAllRows = new MenuItem("Select all rows");
        selectAllRows.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try{
                    LOG.info("Select all rows in user data table.");
                    tableController.getTable().getSelectionModel().selectAll();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });



        /*
                        Reset table

         */

        MenuItem resetTable = new MenuItem("Reset table");
        resetTable.setId("reset_item");
        resetTable.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try{
                    tableController.resetTable();
                    LOG.info("Reset user data table.");
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });



        /*
                        Clear table

         */

        MenuItem cleanTable = new MenuItem("Clear table");
        cleanTable.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try{
                    LOG.info("Remove all data from data table.");
                    tableController.cleartable();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        menuTable.getItems().addAll(getSelectedRows, selectAllRows, resetTable, cleanTable);
    }


    public Menu getMenuTable() {
        return menuTable;
    }


}
