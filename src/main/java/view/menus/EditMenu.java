package view.menus;

import Logging.LogClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import org.xml.sax.SAXException;
import view.MitoBenchWindow;
import view.dialogues.settings.DataFilteringMutationBasedDialogue;
import view.dialogues.settings.DataFilteringTreebasedDialogue;
import view.table.controller.TableControllerUserBench;
import view.tree.HaplotreeController;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by neukamm on 16.11.16.
 */
public class EditMenu {

    private final TableControllerUserBench tableController;
    private final LogClass logClass;
    private final MitoBenchWindow mito;
    private HaplotreeController treeController;
    private Menu menuEdit;


    public EditMenu(MitoBenchWindow mitoBenchWindow){
        menuEdit = new Menu("Edit");
        mito = mitoBenchWindow;
        treeController = mitoBenchWindow.getTreeController();
        logClass = mitoBenchWindow.getLogClass();
        tableController = mitoBenchWindow.getTableControllerUserBench();
        addSubMenues();

    }

    private void addSubMenues() {

        Menu filterData = new Menu("Filter data...");
        filterData.setId("filterItem");

        MenuItem filterTreeBased = new MenuItem("... based on Haplotree");
        filterTreeBased.setId("filterWithList");
        filterTreeBased.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {

                try {
                    DataFilteringTreebasedDialogue dataFilteringWithListDialogue =
                            new DataFilteringTreebasedDialogue("Tree based data filtering",
                            logClass, tableController);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }

            }
        });

        MenuItem filterWithMutation = new MenuItem("... enter Mutation");
        filterWithMutation.setId("filterWithMutation");
        filterWithMutation.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {

                DataFilteringMutationBasedDialogue dataFilteringMutationBasedDialogue =
                        new DataFilteringMutationBasedDialogue("Mutation based data filtering", logClass, mito);
            }
        });



        filterData.getItems().addAll(filterTreeBased, filterWithMutation);
        menuEdit.getItems().add(filterData);

    }


    public Menu getMenuEdit() {
        return menuEdit;
    }
}
