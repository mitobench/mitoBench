package io.Exceptions;

/**
 * Created by peltzer on 22/11/2016.
 */
public class ExcelFileNameException extends Exception {
    public ExcelFileNameException() {
        super();
    }

    public ExcelFileNameException(String message) {
        super(message);
    }

    public ExcelFileNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelFileNameException(Throwable cause) {
        super(cause);

    }
}
