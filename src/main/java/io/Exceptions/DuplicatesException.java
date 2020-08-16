package io.Exceptions;

public class DuplicatesException extends Exception implements IMitoException {


    public DuplicatesException() {
        super();
    }

    public DuplicatesException(String message) {
        super(message);
    }

    public DuplicatesException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatesException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getType() {
        return "input";
    }
}
