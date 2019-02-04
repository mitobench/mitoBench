package io.Exceptions;

public class HaplogroupException extends Exception implements IMitoException {


    public HaplogroupException() {
        super();
    }

    public HaplogroupException(String message) {
        super(message);
    }

    public HaplogroupException(String message, Throwable cause) {
        super(message, cause);
    }

    public HaplogroupException(Throwable cause) {
        super(cause);
    }


    @Override
    public String getType() {
        return null;
    }
}
