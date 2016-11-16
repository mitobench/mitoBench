package view.menus;

import javafx.scene.control.Menu;

/**
 * Created by neukamm on 16.11.16.
 */
public class EditMenu {

    private Menu menuEdit;

    public EditMenu(){
        menuEdit = new Menu("Edit");
    }

    public Menu getMenuEdit() {
        return menuEdit;
    }
}
