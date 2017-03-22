package view.dialogues.settings;

import Logging.LogClass;
import org.xml.sax.SAXException;
import view.MitoBenchWindow;
import view.table.controller.TableControllerUserBench;
import controller.HaplotreeController;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by neukamm on 06.03.17.
 */
public class DataFilteringTreebasedDialogue extends APopupDialogue{

    public DataFilteringTreebasedDialogue(String title, LogClass logClass, MitoBenchWindow mito) throws IOException, SAXException, ParserConfigurationException {
        super(title, logClass);
        dialogGrid.setId("treeFilterDialogue");
        TableControllerUserBench tableController = mito.getTableControllerUserBench();
        addComponents(mito);
        show(600,450);
    }
    private void addComponents(MitoBenchWindow mito) throws ParserConfigurationException, SAXException, IOException {

        HaplotreeController treeController = mito.getTreeController();
        treeController.setKeyEvents(this);
        dialogGrid.add(treeController.getTreeView().getTreeSearchPane(), 0,0);
    }

}
