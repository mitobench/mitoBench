package view.dialogues.settings;

import Logging.LogClass;
import org.xml.sax.SAXException;
import view.table.controller.TableControllerUserBench;
import view.tree.HaplotreeController;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by neukamm on 06.03.17.
 */
public class DataFilteringTreebasedDialogue extends APopupDialogue{
    private TableControllerUserBench tableController;
    private HaplotreeController treeController;

    public DataFilteringTreebasedDialogue(String title, LogClass logClass, TableControllerUserBench tableContr) throws IOException, SAXException, ParserConfigurationException {
        super(title, logClass);

        tableController = tableContr;
        addComponents();
        show(600,450);
    }
    private void addComponents() throws ParserConfigurationException, SAXException, IOException {

        treeController = new HaplotreeController(tableController, logClass);
        treeController.configureSearch();
        dialogGrid.add(treeController.getStackPaneSearchWithList(), 0,0);
    }

}
