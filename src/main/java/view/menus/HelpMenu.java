package view.menus;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;



/**
 * Created by neukamm on 16.11.16.
 */
public class HelpMenu {

    private Menu menuHelp;

    public HelpMenu(){

        menuHelp = new Menu("Help");
        add();

    }

    private void add(){

          /*
                        Help page

         */

        MenuItem helpItem = new MenuItem("Show help");
        helpItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {


           }
        });


         /*
                        About MitoBench

         */

        MenuItem aboutItem = new MenuItem("About MitoBench");
        helpItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {


            }
        });


        menuHelp.getItems().addAll(helpItem, aboutItem);

    }

    public Menu getMenuHelp() {
        return menuHelp;
    }
}
