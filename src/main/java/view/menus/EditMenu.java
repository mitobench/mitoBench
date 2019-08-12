package view.menus;

import Logging.LogClass;
import controller.HGListController;
import javafx.scene.control.*;
import org.xml.sax.SAXException;
import view.MitoBenchWindow;
import view.dialogues.settings.DataFilteringHaplotypeBasedDialogue;
import view.dialogues.settings.DataFilteringTreebasedDialogue;
import view.dialogues.settings.HGListDialogue;

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
        filterTreeBased.setOnAction(t -> {

            DataFilteringTreebasedDialogue dataFilteringWithListDialogue =
                    new DataFilteringTreebasedDialogue("Tree based data filtering",
                    logClass, mito);
            mito.getTabpane_statistics().getTabs().add(dataFilteringWithListDialogue.getTab());
            mito.getTabpane_statistics().getSelectionModel().select(dataFilteringWithListDialogue.getTab());

          });

        MenuItem filterWithMutation = new MenuItem("... haplotype filtering");
        filterWithMutation.setId("filterWithMutation");
        filterWithMutation.setOnAction(t -> {

            DataFilteringHaplotypeBasedDialogue dataFilteringMutationBasedDialogue =
                    new DataFilteringHaplotypeBasedDialogue("Haplotype based data filtering", logClass, mito);
            mito.getTabpane_statistics().getTabs().add(dataFilteringMutationBasedDialogue.getTab());
            mito.getTabpane_statistics().getSelectionModel().select(dataFilteringMutationBasedDialogue.getTab());
        });


        MenuItem unfilterData = new MenuItem("Redo...");
        unfilterData.setId("unfilterItem");
        unfilterData.setOnAction(t -> {
            mito.getTableControllerUserBench().resetToUnfilteredData();

        });

        CustomMenuItem defineHGList = new CustomMenuItem(new Label("Define Haplogroup list"));
        defineHGList.setId("menuitem_hg_list");
        defineHGList.setOnAction(t -> {

            HGListDialogue hgListDialogue = new HGListDialogue("Custom Haplogroup list", logClass);
            HGListController hgListController = new HGListController(hgListDialogue, mito.getChartController(), mito);
            mito.getTabpane_statistics().getTabs().add(hgListDialogue.getTab());
            mito.getTabpane_statistics().getSelectionModel().select(hgListDialogue.getTab());


        });

        Tooltip tooltip_hglist = new Tooltip("This list will be used as default list for all analyses and " +
                "visualizations within this project.");
        Tooltip.install(defineHGList.getContent(), tooltip_hglist);



        filterData.getItems().addAll(filterTreeBased, filterWithMutation);
        menuEdit.getItems().addAll(filterData, defineHGList);

    }


    public Menu getMenuEdit() {
        return menuEdit;
    }
}
