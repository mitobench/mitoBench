package gui;

import javafx.scene.Node;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.testfx.api.FxRobot;
import view.MitoBenchWindow;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.api.FxToolkit.registerPrimaryStage;
import static org.testfx.api.FxToolkit.setupApplication;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

/**
 * Created by neukamm on 06.02.17.
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class GUIDatabaseTest extends FxRobot {


    @BeforeClass
    public static void setupSpec() throws Exception {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
        }
        System.setProperty("javafx.running", "true");
        registerPrimaryStage();
    }

    @Before
    public void setUp() throws Exception {

        setupApplication(MitoBenchWindow.class);

    }

    @Test
    public void loadData() throws Exception {


        this.clickOn("#fileMenu").clickOn("#importFromDB");
        verifyThat("#connect_to_database", isVisible());
        verifyThat("#title_label", isVisible());
        verifyThat("#username_label", isVisible());
        verifyThat("#password_label", isVisible());
        verifyThat("#usernamme_field", isVisible());
        verifyThat("#password_field", isVisible());
        this.clickOn("#loginButton");
        verifyThat("#dbtable" , isVisible());

        this.clickOn("#dbtable").drag("#dbtable").dropTo("#mainEntryTable");




    }


}
