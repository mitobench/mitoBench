package view.dialogues.settings;

import Logging.LogClass;
import org.xml.sax.SAXException;
import view.MitoBenchWindow;
import controller.HaplotreeController;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by neukamm on 06.03.17.
 */
public class DataFilteringTreebasedDialogue extends ATabpaneDialogue{

    private HaplotreeController treeController;

    public DataFilteringTreebasedDialogue(String title, LogClass logClass, MitoBenchWindow mito) throws IOException, SAXException, ParserConfigurationException {
        super(title, logClass);
        dialogGrid.setId("treeFilterDialogue");
        addComponents(mito);
    }
    private void addComponents(MitoBenchWindow mito) throws ParserConfigurationException, SAXException, IOException {

        treeController = mito.getTreeController();
        treeController.setKeyEvents(this);
        dialogGrid.add(treeController.getTreeView().getTreeSearchPane(), 0,0);
    }

}
