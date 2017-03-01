package gui;

import javafx.scene.input.KeyCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.api.FxRobot;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

/**
 * Created by peltzer on 21/12/2016.
 */
public class GUITestSteps {

    private static final Logger LOG = LoggerFactory.getLogger(GUITestSteps.class);
    private final FxRobot robot;
    private int stepno;

    public GUITestSteps(final FxRobot robot) {
        this.robot = robot;
    }

    public void part4BasicStuff() {

        step("Open application", () -> {
            verifyThat("#mainBorderPane", isVisible());
        });
        step("Test Table is visible", () -> {
            verifyThat("#mainEntryTable", isVisible());
        });
        step("TreeView Button exists", () -> {
            verifyThat("#treeViewOpenCloseLabel", isVisible());
        });
        step("MenuBar exists", () -> {
            verifyThat("#menuBar", isVisible());
        });
        step("Visualization pane exists", () -> {
            verifyThat("#mainVizTabpane", isVisible());
        });
        step("Statistics pane exists", () -> {
            verifyThat("#mainWindowLeftpart", isVisible());
        });
    }

    public void part8MenuInteraction() {
        step("Open File Menu", () -> {
            robot.clickOn("#fileMenu");
            verifyThat("#fileMenu", isVisible());
        });
        step("Open Table Menu", () -> {
            robot.clickOn("#tableMenu");
            verifyThat("#tableMenu", isVisible());
        });
        step("Open Graphics Menu", () -> {
            step("Open application", () -> {
                verifyThat("#mainBorderPane", isVisible());
            });
            step("Test Table is visible", () -> {
                verifyThat("#mainEntryTable", isVisible());
            });
            step("TreeView Button exists", () -> {
                verifyThat("#treeViewOpenCloseLabel", isVisible());
            });
            step("MenuBar exists", () -> {
                verifyThat("#menuBar", isVisible());
            });
        });

        step("Open Help Menu", () -> {
            robot.clickOn("#helpMenu");
            verifyThat("#helpMenu", isVisible());
        });
    }

    public void part3AboutDialogueTests() {
        step("Open About Dialogue", () -> {
            robot.clickOn("#helpMenu").clickOn("#aboutMenuItem");
            verifyThat("#aboutDialogue", isVisible());
            robot.clickOn("#aboutDialogue" + "button");
        });
    }

    public void part1TreeViewTests() {
        step("Open TreeView", () -> {
            robot.clickOn("#treeViewOpenCloseLabel");
            //Now our dropdown menu should appear!
            verifyThat("#treeviewSearchPane", isVisible());
            robot.clickOn("#treeView-inner-tree");
            robot.clickOn("#treeviewApplyButton");
            robot.clickOn("#treeViewOpenCloseLabel");


            robot.clickOn("#treeViewOpenCloseLabel");
            //Now our dropdown menu should appear!
            verifyThat("#treeviewSearchPane", isVisible());
            robot.clickOn("#treeviewSearchField").write("U,V,X,W");
            robot.push(KeyCode.ENTER);
            robot.clickOn("#treeViewOpenCloseLabel");

            robot.clickOn("#treeViewOpenCloseLabel");
            //Now our dropdown menu should appear!
            verifyThat("#treeviewSearchPane", isVisible());
            robot.clickOn("#treeviewApplyButton");
            robot.clickOn("#treeViewOpenCloseLabel");

        });
    }

    public void part7CreatePlots(){
        step("Plot HG frequency", () -> {
            robot.clickOn("#graphicsMenu");
            verifyThat("#graphicsMenu", isVisible());
            robot.clickOn("#haplo_graphics");
            verifyThat("#haplo_graphics", isVisible());

            robot.clickOn("#barchart");
            verifyThat("#barchart", isVisible());
            robot.clickOn("#plotHGfreq_item");
            verifyThat("#tab_haplo_barchart", isVisible());
        });


        step("Plot Stacked bar chart", () -> {

            robot.clickOn("#graphicsMenu").clickOn("#haplo_graphics").clickOn("#barchart").clickOn("#plotHGfreqGroup_item");
            verifyThat("#stackedBarChartDialogue", isVisible());
            robot.clickOn("#stackedBarApplyBtn");
            verifyThat("#tab_stacked_bar_chart", isVisible());
        });
        step("Plot Sunburstchart", () -> {

            robot.clickOn("#graphicsMenu").clickOn("#haplo_graphics").clickOn("#sunburstChart_item");
            verifyThat("#tab_sunburst", isVisible());
            robot.moveTo("#borderpane_sunburst");
        });

        step("Plot profile plot", () -> {

            robot.clickOn("#graphicsMenu").clickOn("#haplo_graphics").moveTo("#sunburstChart_item").clickOn("#profilePlot");
            verifyThat("#tab_profilePlot", isVisible());

        });


        step("Plot Grouped barchart", () -> {

            robot.clickOn("#graphicsMenu").clickOn("#grouping_graphics");
            verifyThat("#grouping_graphics", isVisible());
            robot.clickOn("#grouping_barchart_item");
            verifyThat("#tab_group_barchart", isVisible());

        });

        step("Clear plots", () -> {

            robot.clickOn("#graphicsMenu");
            robot.clickOn("#clear_plots");
            //verifyThat("#clearPlotBox_item", isVisible());
        });


    }


    public void part2FillTable(String file){
        step("Open Project file", () -> {
            robot.clickOn("#fileMenu").clickOn("Import");
            verifyThat("#import_dialogue_alt", isVisible());
            robot.clickOn("#textfield_path").write(file);
            robot.clickOn("#btn_open");

        });
    }

    public void part6Statistics() {

        step("Calculate Statistics", () -> {
            robot.clickOn("#menu_statistics");
            robot.clickOn("#toolsMenu_stats_hg");
            verifyThat("#statistics_popup", isVisible());

            // click on default HG selection
            robot.clickOn("#checkbox_hg_default_selection");
            robot.clickOn("#button_ok_statistics");
            verifyThat("#tab_statistics", isVisible());

        });
    }


    public void part5DBTest(){
        step("Calculate Statistics", () -> {
            robot.clickOn("#fileMenu").clickOn("#importFromDB");
            verifyThat("#connect_to_database", isVisible());
            verifyThat("#title_label", isVisible());
            verifyThat("#username_label", isVisible());
            verifyThat("#password_label", isVisible());
            verifyThat("#usernamme_field", isVisible());
            verifyThat("#password_field", isVisible());
            robot.clickOn("#loginButton");

            verifyThat("#set_database_query", isVisible());
            robot.clickOn("#textfield_selection_table").write("*");
            robot.clickOn("#db_sendBtn");
            verifyThat("#dbtable" , isVisible());

            robot.clickOn("#dbtable").drag("#dbtable").dropTo("#mainEntryTable");

        });

    }

    public void part0SetLogDir() {
        step("Set Log Directory", () -> {
            verifyThat("#logDialogue", isVisible());
            robot.clickOn("#btn_applyLogDir");
        });
    }


    public void partTestGrouping(){
        step("Calculate Statistics", () -> {
            robot.clickOn("#menu_grouping").clickOn("#create_groups").clickOn("#group_by_column");
        });
    }


    private void step(final String step, final Runnable runnable) {
        ++stepno;
        LOG.info("STEP {}: Begin - {}", stepno, step);
        runnable.run();
        LOG.info("STEP {}:   End - {}", stepno, step);
    }


}
