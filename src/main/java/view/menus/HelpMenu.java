package view.menus;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
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

        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();

        MenuItem helpItem = new MenuItem("Show help");
        helpItem.setOnAction(t -> {

        });




         /*
                        About starter.MitoBenchStarter

         */

        MenuItem aboutItem = new MenuItem("About MitoBench");
        aboutItem.setId("aboutMenuItem");
        aboutItem.setOnAction(t -> {
            AboutDialogue aboutDialogue = new AboutDialogue(
                    "About MitoBench",
                    "If you need some help, read the documentation first:",
                    "mitoBench and mitoDB",
                    "aboutDialogue");
        });


        //menuHelp.getItems().addAll(helpItem, aboutItem);
        menuHelp.getItems().addAll(aboutItem);

    }

    public Menu getMenuHelp() {
        return menuHelp;
    }


}
