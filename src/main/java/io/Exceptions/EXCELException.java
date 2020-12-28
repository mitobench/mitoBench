package io.Exceptions;

/**
 * Created by neukamm on 22.03.17.
 */
public class EXCELException extends Exception implements IMitoException {

    public EXCELException() {
        super();
    }

    public EXCELException(String message) {
        super(message);
    }

    public EXCELException(String message, Throwable cause) {
        super(message, cause);
    }

    public EXCELException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getType() {
        return "XSLX";
    }
}