package view.menus;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import view.table.TableController;


/**
 * Created by neukamm on 23.11.16.
 */
public class TableMenu {

    private Menu menuTable;
    private TableController tableController;

    public TableMenu(TableController tableController){
        menuTable = new Menu("Table");
        menuTable.setId("tableMenu");
        this.tableController = tableController;
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
                    tableController.updateView(tableController.getTable().getSelectionModel().getSelectedItems());
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
        resetTable.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try{
                    tableController.resetTable();
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

                    // clean view.data model
                    tableController.getData().removeAll(tableController.getData());
                    // clean table view
                    tableController.getTable().getItems().removeAll(tableController.getTable().getItems());
                    tableController.getDataTable().getMtStorage().getData().clear();
                    tableController.getDataTable().getDataTable().clear();
                    tableController.getTable().getColumns().removeAll(tableController.getTable().getColumns());
                    tableController.getGroupController().clear();
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
