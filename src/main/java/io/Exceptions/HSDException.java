package io.Exceptions;

/**
 * Created by peltzer on 23/11/2016.
 */
public class HSDException extends Exception implements IMitoException {

    public HSDException() {
        super();
    }

    public HSDException(String message) {
        super(message);
    }

    public HSDException(String message, Throwable cause) {
        super(message, cause);
    }

    public HSDException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getType() {
        return "HSD";
    }
}

