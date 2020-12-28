package view.table;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class TableContextMenu {

    private ContextMenu menu;
    private MenuItem addNewGropuItem;
    private MenuItem addAllSelectedItem;
    private MenuItem deleteSelectedRows;
    private MenuItem deleteColumn;
    private MenuItem copyColumn;

    public TableContextMenu(){
        build();
    }

    private void build(){
        menu = new ContextMenu();
        addNewGropuItem = new MenuItem("Add new column");
        addAllSelectedItem = new MenuItem("Add/replace data");
        deleteSelectedRows = new MenuItem("Delete selected row(s)");
        deleteColumn = new MenuItem("Delete column");
        copyColumn = new MenuItem("Copy column");

        menu.getItems().addAll(addNewGropuItem, addAllSelectedItem, deleteSelectedRows, copyColumn,  deleteColumn);

    }

    public ContextMenu getMenu() {
        return menu;
    }

    public void setMenu(ContextMenu menu) {
        this.menu = menu;
    }

    public MenuItem getAddNewGropuItem() {
        return addNewGropuItem;
    }

    public void setAddNewGropuItem(MenuItem addNewGropuItem) {
        this.addNewGropuItem = addNewGropuItem;
    }

    public MenuItem getAddAllSelectedItem() {
        return addAllSelectedItem;
    }

    public void setAddAllSelectedItem(MenuItem addAllSelectedItem) {
        this.addAllSelectedItem = addAllSelectedItem;
    }

    public MenuItem getDeleteSelectedRows() {
        return deleteSelectedRows;
    }

    public void setDeleteSelectedRows(MenuItem deleteSelectedRows) {
        this.deleteSelectedRows = deleteSelectedRows;
    }

    public MenuItem getDeleteColumn() {
        return deleteColumn;
    }

    public void setDeleteColumn(MenuItem deleteColumn) {
        this.deleteColumn = deleteColumn;
    }

    public MenuItem getCopyColumn() {
        return copyColumn;
    }

    public void setCopyColumn(MenuItem copyColumn) {
        this.copyColumn = copyColumn;
    }
}
