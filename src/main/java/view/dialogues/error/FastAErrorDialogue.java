package view.dialogues.error;

import io.Exceptions.FastAException;

/**
 * Created by peltzer on 23/11/2016.
 */
public class FastAErrorDialogue extends AbstractFileExceptionDialogue {

    public FastAErrorDialogue(FastAException ex) {
        super(ex);
    }
}
