package view.dialogues.settings;

import Logging.LogClass;
import org.xml.sax.SAXException;
import view.MitoBenchWindow;
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

    public DataFilteringTreebasedDialogue(String title, LogClass logClass, MitoBenchWindow mito) throws IOException, SAXException, ParserConfigurationException {
        super(title, logClass);
        dialogGrid.setId("treeFilterDialogue");
        tableController = mito.getTableControllerUserBench();
        addComponents(mito);
        show(600,450);
    }
    private void addComponents(MitoBenchWindow mito) throws ParserConfigurationException, SAXException, IOException {

        treeController = mito.getTreeController();
        treeController.configureSearch(this);
        dialogGrid.add(treeController.getStackPaneSearchWithList(), 0,0);
    }

}
