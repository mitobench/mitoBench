package gui;

import javafx.scene.input.KeyCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.api.FxRobot;

import java.io.File;

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

    public void part1BasicStuff() {

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
    }

    public void part2MenuInteraction() {
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

    public void part4TreeViewTests() {
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

    public void part5CreatePlots(){
        step("Open Project file", () -> {
            robot.clickOn("#graphicsMenu");
            verifyThat("#graphicsMenu", isVisible());
            robot.clickOn("#haplo_graphics");
            verifyThat("#haplo_graphics", isVisible());

            robot.clickOn("#barchart");
            verifyThat("#barchart", isVisible());
            robot.clickOn("#plotHGfreq_item");
            verifyThat("#tab_haplo_barchart", isVisible());

            robot.clickOn("#graphicsMenu").clickOn("#haplo_graphics").clickOn("#barchart").clickOn("#plotHGfreqGroup_item");
            verifyThat("#tab_stacked_bar_chart", isVisible());

            robot.clickOn("#graphicsMenu").clickOn("#haplo_graphics").clickOn("#sunburstChart_item");
            verifyThat("#tab_sunburst", isVisible());
            robot.moveTo("#borderpane_sunburst");

            robot.clickOn("#graphicsMenu").clickOn("#grouping_graphics");
            verifyThat("#grouping_graphics", isVisible());
            robot.clickOn("#grouping_barchart_item");
            verifyThat("#tab_group_barchart", isVisible());

            robot.clickOn("#graphicsMenu");
            robot.clickOn("#haplo_graphics");
            //robot.clickOn("#profilePlotItem");
            //verifyThat("#tab_profilePlot", isVisible());


            robot.clickOn("#graphicsMenu");
            //robot.clickOn("#clear_plots");
            //verifyThat("#clearPlotBox_item", isVisible());
        });
    }


    public void part6FillTable(File file){
        step("Open Project file", () -> {
            robot.clickOn("#fileMenu").clickOn("Import Data");
        });
    }

    public void part7ExportStatistics(){
        step("Export Statistics", () -> {
            robot.clickOn("#fileMenu");
            robot.clickOn("#exportCurrentStats");
        });
    }


    private void step(final String step, final Runnable runnable) {
        ++stepno;
        LOG.info("STEP {}: Begin - {}", stepno, step);
        runnable.run();
        LOG.info("STEP {}:   End - {}", stepno, step);
    }
}
