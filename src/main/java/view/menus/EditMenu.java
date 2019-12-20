package view.menus;

import Logging.LogClass;
import controller.HGListController;
import controller.MenuController;
import javafx.scene.control.*;
import view.MitoBenchWindow;
import view.dialogues.settings.DataFilteringHaplotypeBasedDialogue;
import view.dialogues.settings.DataFilteringTreebasedDialogue;
import view.dialogues.settings.HGListDialogue;

/**
 * Created by neukamm on 16.11.16.
 */
public class EditMenu {

    private final LogClass logClass;
    private final MitoBenchWindow mito;
    private Menu menuEdit;
    private MenuController menuController;


    public EditMenu(MitoBenchWindow mitoBenchWindow){
        menuEdit = new Menu("Edit");
        menuEdit.setId("menuEdit");
        mito = mitoBenchWindow;
        logClass = mitoBenchWindow.getLogClass();
        menuController = mitoBenchWindow.getMenuController();
        addSubMenus();

    }

    private void addSubMenus() {

        Menu filterData = new Menu("Filter data...");
        filterData.setId("filterItem");

        // --------------------- MenuItem Filter tree ------------------------------------------

        MenuItem filterTreeBased = new MenuItem("... based on PhyloTree");
        filterTreeBased.setId("filterWithTree");

        // --------------------- MenuItem Filter according mutation ------------------------------------------

        MenuItem filterWithMutation = new MenuItem("... haplotype filtering");
        filterWithMutation.setId("filterWithMutation");

        // --------------------- MenuItem Redo filtering ------------------------------------------

        MenuItem unfilterData = new MenuItem("Redo...");
        unfilterData.setId("unfilterItem");

        // --------------------- MenuItem Define Haplogroup list ------------------------------------------

        CustomMenuItem defineHGList = new CustomMenuItem(new Label("Define Haplogroup list"));
        defineHGList.setId("menuitem_hg_list");


        // --------------------- Tooltips------------------------ ------------------------------------------
        Tooltip tooltip_hglist = new Tooltip("This list will be used as default list for all analyses and " +
                "visualizations within this project.");
        Tooltip.install(defineHGList.getContent(), tooltip_hglist);







        // --------------------- Add items ------------------------------------------------------
        menuController.setEditMenu(filterTreeBased, filterWithMutation, unfilterData, defineHGList);

        filterData.getItems().addAll(filterTreeBased, filterWithMutation);
        menuEdit.getItems().addAll(filterData, defineHGList);

    }


    public Menu getMenuEdit() {
        return menuEdit;
    }
}
