package view.menus;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import view.dialogues.information.AboutDialogue;




/**
 * Created by neukamm on 16.11.16.
 */
public class HelpMenu {

    private Menu menuHelp;

    public HelpMenu(){

        menuHelp = new Menu("Help");
        menuHelp.setId("helpMenu");
        add();

    }

    private void add(){

          /*
                        Help page

         */

        MenuItem helpItem = new MenuItem("Show help");
        helpItem.setOnAction(t -> {

        });




         /*
                        About mitoBench

         */

        MenuItem aboutItem = new MenuItem("About MitoBench");
        aboutItem.setId("aboutMenuItem");
        aboutItem.setOnAction(t -> {
            AboutDialogue aboutDialogue = new AboutDialogue(
                    "About MitoBench",
                    "If you need some help, read the documentation first:");
        });

        menuHelp.getItems().addAll(aboutItem);

    }

    public Menu getMenuHelp() {
        return menuHelp;
    }


}
