package view.menus;

import javafx.scene.control.Menu;

/**
 * Created by neukamm on 16.11.16.
 */
public class ToolsMenu {

    private Menu menuTools;

    public ToolsMenu(){
        menuTools = new Menu("Statistics");
    }


    public Menu getMenuTools() {
        return menuTools;
    }
}
