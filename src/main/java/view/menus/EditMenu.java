package view.menus;

import Logging.LogClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import org.xml.sax.SAXException;
import view.MitoBenchWindow;
import view.dialogues.settings.DataFilteringHaplotypeBasedDialogue;
import view.dialogues.settings.DataFilteringTreebasedDialogue;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by neukamm on 16.11.16.
 */
public class EditMenu {

    private final LogClass logClass;
    private final MitoBenchWindow mito;
    private Menu menuEdit;


    public EditMenu(MitoBenchWindow mitoBenchWindow){
        menuEdit = new Menu("Edit");
        menuEdit.setId("menuEdit");
        mito = mitoBenchWindow;
        logClass = mitoBenchWindow.getLogClass();
        addSubMenus();

    }

    private void addSubMenus() {

        Menu filterData = new Menu("Filter data...");
        filterData.setId("filterItem");

        MenuItem filterTreeBased = new MenuItem("... based on PhyloTree");
        filterTreeBased.setId("filterWithTree");
        filterTreeBased.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {

                try {
                    DataFilteringTreebasedDialogue dataFilteringWithListDialogue =
                            new DataFilteringTreebasedDialogue("Tree based data filtering",
                            logClass, mito);
                    mito.getTabpane_statistics().getTabs().add(dataFilteringWithListDialogue.getTab());
                    mito.getTabpane_statistics().getSelectionModel().select(dataFilteringWithListDialogue.getTab());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }

            }
        });

        MenuItem filterWithMutation = new MenuItem("... haplotype filtering");
        filterWithMutation.setId("filterWithMutation");
        filterWithMutation.setOnAction(t -> {

            DataFilteringHaplotypeBasedDialogue dataFilteringMutationBasedDialogue =
                    new DataFilteringHaplotypeBasedDialogue("Haplotype based data filtering", logClass, mito);
            mito.getTabpane_statistics().getTabs().add(dataFilteringMutationBasedDialogue.getTab());
            mito.getTabpane_statistics().getSelectionModel().select(dataFilteringMutationBasedDialogue.getTab());
        });

        filterData.getItems().addAll(filterTreeBased, filterWithMutation);
        menuEdit.getItems().add(filterData);

    }


    public Menu getMenuEdit() {
        return menuEdit;
    }
}
