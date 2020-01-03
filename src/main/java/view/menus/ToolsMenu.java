package view.menus;

import Logging.LogClass;
import controller.MenuController;
import javafx.scene.control.*;
import view.MitoBenchWindow;

public class ToolsMenu {

    private final Menu menuTools;
    private final LogClass logClass;
    private MenuController menuController;
    private CustomMenuItem dataUploaderMenuItem;
    private CustomMenuItem dataValidatorMenuItem;
    private CustomMenuItem dataCompleterMenuItem;
    private CustomMenuItem dataAlignerMenuItem;

    public ToolsMenu(MitoBenchWindow mitoBenchWindow, GroupMenu groupMenu, AnalysisMenu analysisMenu, StatisticsMenu statisticsMenu) {
        menuTools = new Menu("Tools");
        menuTools.setId("menuTools");
        logClass = mitoBenchWindow.getLogClass();
        menuController = mitoBenchWindow.getMenuController();
        addSubMenus(groupMenu, analysisMenu, statisticsMenu);
    }

    private void addSubMenus(GroupMenu groupMenu, AnalysisMenu analysisMenu, StatisticsMenu statisticsMenu) {


        // ------------------------------ Menu Item Validate data --------------------------------------------

        dataAlignerMenuItem = new CustomMenuItem(new Label("Align Sequences"));
        dataAlignerMenuItem.setId("dataAligner");


        // ------------------------------ Menu Item Validate data --------------------------------------------

        dataValidatorMenuItem = new CustomMenuItem(new Label("Validate data"));
        dataValidatorMenuItem.setId("dataValidator");

        // ------------------------------ Menu Item Complete data --------------------------------------------

        dataCompleterMenuItem = new CustomMenuItem(new Label("Complete data"));
        dataValidatorMenuItem.setId("dataCompleter");

        // ------------------------------ Menu Item Upload data ----------------------------------------------

        dataUploaderMenuItem = new CustomMenuItem(new Label("Upload data to database"));
        dataValidatorMenuItem.setId("dataUploaderMenuItem");


        // ------------------------------ Add tooltips --------------------------------------------------------

        Tooltip tooltip_validateData = new Tooltip("This will check if your data are in correct format.");
        Tooltip.install(dataValidatorMenuItem.getContent(), tooltip_validateData);


        Tooltip tooltip_completeData = new Tooltip("This will complete and calculate some meta information. " +
                "It also runs dataValidation.");
        Tooltip.install(dataCompleterMenuItem.getContent(), tooltip_completeData);


        Tooltip tooltip_uploadData = new Tooltip("This will upload your data to database and runs dataValidation and dataCompletion " +
                "before. You will be asked again before uploading.");
        Tooltip.install(dataUploaderMenuItem.getContent(), tooltip_uploadData);

        // ------------------------------ Add All Items---------- --------------------------------------------

        menuController.setToolsMenu(dataAlignerMenuItem, dataValidatorMenuItem, dataCompleterMenuItem, dataUploaderMenuItem);

        menuTools.getItems().addAll(groupMenu.getMenuGroup(), analysisMenu.getMenuAnalysis(), statisticsMenu.getMenuTools(),
                dataAlignerMenuItem, dataValidatorMenuItem, dataCompleterMenuItem, dataUploaderMenuItem);



    }

    public Menu getMenuTools() {
        return menuTools;
    }


    public LogClass getLogClass() {
        return logClass;
    }

    public MenuItem getDataUploaderMenuItem() {
        return dataUploaderMenuItem;
    }

    public void setDataUploaderMenuItem(CustomMenuItem dataUploaderMenuItem) {
        this.dataUploaderMenuItem = dataUploaderMenuItem;
    }

    public MenuItem getDataValidatorMenuItem() {
        return dataValidatorMenuItem;
    }

    public void setDataValidatorMenuItem(CustomMenuItem dataValidatorMenuItem) {
        this.dataValidatorMenuItem = dataValidatorMenuItem;
    }

    public MenuItem getDataCompleterMenuItem() {
        return dataCompleterMenuItem;
    }

    public void setDataCompleterMenuItem(CustomMenuItem dataCompleterMenuItem) {
        this.dataCompleterMenuItem = dataCompleterMenuItem;
    }
}
