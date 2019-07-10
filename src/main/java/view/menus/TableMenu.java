package view.menus;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import org.apache.log4j.Logger;
import view.MitoBenchWindow;
import controller.GroupController;
import controller.TableControllerUserBench;
import view.dialogues.settings.ColumnOrderDialogue;


/**
 * Created by neukamm on 23.11.16.
 */
public class TableMenu {

    private final MitoBenchWindow mito;
    private Menu menuTable;
    private TableControllerUserBench tableController;
    private Logger LOG;

    public TableMenu(MitoBenchWindow mitoBenchWindow){
        menuTable = new Menu("Table");
        menuTable.setId("tableMenu");
        this.mito = mitoBenchWindow;
        this.tableController = mitoBenchWindow.getTableControllerUserBench();
        LOG = mitoBenchWindow.getLogClass().getLogger(this.getClass());
        addSubMenus();
    }


    private void addSubMenus(){



        CustomMenuItem defineColumnOrder = new CustomMenuItem(new Label("Define Column Order"));
        defineColumnOrder.setId("menuitem_column_order");
        defineColumnOrder.setOnAction(t -> {
            ColumnOrderDialogue columnOrderDialogue = new ColumnOrderDialogue("Custom Column Order",
                    mito.getLogClass(), mito);

            mito.getTabpane_statistics().getTabs().add(columnOrderDialogue.getTab());
            mito.getTabpane_statistics().getSelectionModel().select(columnOrderDialogue.getTab());

        });



        /*
                        Get selected rows

         */

        MenuItem getSelectedRows = new MenuItem("Get selected rows");
        getSelectedRows.setOnAction(t -> {
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
        });



        /*
                       Select all rows

         */

        MenuItem selectAllRows = new MenuItem("Select all rows");
        selectAllRows.setOnAction(t -> {
            try{
                LOG.info("Select all rows in user data table.");
                tableController.getTable().getSelectionModel().selectAll();
            } catch (Exception e){
                e.printStackTrace();
            }
        });




        /*
                        Clear table

         */

        MenuItem cleanTable = new MenuItem("Clear table");
        cleanTable.setOnAction(t -> {
            try{
                LOG.info("Remove all data from data table.");
                tableController.cleartable();
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        menuTable.getItems().addAll(defineColumnOrder, getSelectedRows, selectAllRows, cleanTable);
    }


    public Menu getMenuTable() {
        return menuTable;
    }


}
