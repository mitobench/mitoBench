package view.dialogues.settings;

import Logging.LogClass;
import view.MitoBenchWindow;
import controller.HaplotreeController;


/**
 * Created by neukamm on 06.03.17.
 */
public class DataFilteringTreebasedDialogue extends ATabpaneDialogue{

    private HaplotreeController treeController;

    public DataFilteringTreebasedDialogue(String title, LogClass logClass, MitoBenchWindow mito){
        super(title, logClass);
        dialogGrid.setId("treeFilterDialogue");
        addComponents(mito);
    }
    private void addComponents(MitoBenchWindow mito) {

        treeController = mito.getTreeController();
        treeController.setKeyEvents(this);
        dialogGrid.add(treeController.getTreeView().getTreeSearchPane(), 0,0);
    }

}
