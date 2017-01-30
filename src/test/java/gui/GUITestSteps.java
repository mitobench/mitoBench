package gui;

import io.Exceptions.ProjectException;
import io.dialogues.Import.ImportDialogue;
import io.reader.ProjectReader;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.media.Track;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.api.FxRobot;
import view.table.TableController;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;

import static com.google.common.io.Resources.getResource;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;
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
            robot.clickOn("#graphicsMenu");
            verifyThat("#graphicsMenu", isVisible());
            robot.clickOn("#haplo_graphics");
            verifyThat("#haplo_graphics", isVisible());

            robot.clickOn("#barchart");
            verifyThat("#barchart", isVisible());
            //robot.clickOn("#plotHGfreq_item");
            //verifyThat("#plotHGfreq_item", isVisible());
            //robot.clickOn("#plotHGfreqGroup_item");
            //verifyThat("#plotHGfreqGroup_item", isVisible());

            //robot.clickOn("#sunburstChart_item");
            //verifyThat("#sunburstChart_item", isVisible());
            //robot.clickOn("#profilePlot_item");
            //verifyThat("#profilePlot_item", isVisible());

            robot.clickOn("#grouping_graphics");
            verifyThat("#grouping_graphics", isVisible());
            //robot.clickOn("#grouping_barchart_item");
            //verifyThat("#grouping_barchart_item", isVisible());

            //robot.clickOn("#clearPlotBox_item");
            //verifyThat("#clearPlotBox_item", isVisible());

        });
        step("Open Help Menu", () -> {
            robot.clickOn("#helpMenu");
            verifyThat("#helpMenu", isVisible());
        });
    }

    public void part3AboutDialogueTests() {
        step("Open About Dialogue", () -> {
            robot.clickOn("#helpMenu");
            robot.clickOn("#aboutMenuItem");
            verifyThat("#aboutDialogue", isVisible());
            robot.clickOn("#aboutDialogue" + "button");
        });
    }

    public void part4TreeViewTests() {
        step("Open TreeView", () -> {
            robot.clickOn("#treeViewOpenCloseLabel");
            //Now our dropdown menu should appear!
            verifyThat("#treeviewSearchPane", isVisible());
            robot.clickOn("#treeviewSearchField").write("U,V,X,W");
            robot.clickOn("#treeviewApplyButton");
            robot.clickOn("#treeViewOpenCloseLabel");
        });
    }

    public void part5TestImportDialogue() {
        step("Open Project file", () -> {
            robot.clickOn("#fileMenu");
            robot.clickOn("#fileMenu_importData");
        });

    }


    public void part6FillTable(TableController tableController){
        //ImportDialogue dialogue = mock(ImportDialogue.class);
        //when(dialogue.isFileSelected()).thenReturn(true);
        //when(dialogue.getSelectedFile()).thenReturn(path.toFile());

        String path = "./project.mitoproj";
        ProjectReader projectReader = new ProjectReader();
        System.out.println(getResource(path));
        try {
            projectReader.read(new File(getResource(path).getFile()));
            //projectReader.read(new File("/home/neukamm/GitWorkspace/MitoBench/test_files/project.mitoproj"));
            projectReader.loadData(tableController);
            System.out.println(projectReader.getDatatable().keySet());
            System.out.println("Read file to table");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ProjectException e) {
            e.printStackTrace();
        }

        robot.clickOn("#tableView");
        assertTrue(tableController.getData().size() > 0);


    }

    private void step(final String step, final Runnable runnable) {
        ++stepno;
        LOG.info("STEP {}: Begin - {}", stepno, step);
        runnable.run();
        LOG.info("STEP {}:   End - {}", stepno, step);
    }
}
