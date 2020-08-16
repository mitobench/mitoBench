package controller;

import analysis.SequenceAligner;
import io.writer.MultiFastaWriter;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import view.MitoBenchWindow;


public class DialogueController {

    private final MitoBenchWindow mito;
    private Logger log;

    public DialogueController(MitoBenchWindow mito){

        this.mito = mito;
        this.log = mito.getLogClass().getLogger(this.getClass());
    }


    public void unalignedDialogue_setAlign_mafft_btn(Button align_mafft_btn, Stage dialog) {
        // todo: add different mafft algorithms
        align_mafft_btn.setOnAction(e -> {
            SequenceAligner sequenceAligner = new SequenceAligner(log, mito.getTableControllerUserBench());

            // get sequences to align
            MultiFastaWriter multiFastaWriter = new MultiFastaWriter(mito.getTableControllerUserBench().getDataTable().getMtStorage(),
                    mito.getTableControllerUserBench().getSelectedRows(),
                    true);

            try {
                try {
                    multiFastaWriter.writeData("sequences_to_align.fasta", mito.getTableControllerUserBench());

                    sequenceAligner.align("sequences_to_align.fasta");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            dialog.close();
        });
    }

    public void unalignedDialogue_setCancel_btn(Button cancel_btn, Stage dialog) {
        cancel_btn.setOnAction(e -> {
            dialog.close();
        });

    }
}

