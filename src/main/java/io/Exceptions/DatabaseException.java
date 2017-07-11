package io.Exceptions;

/**
 * Created by neukamm on 7/11/17.
 */
public class DatabaseException extends Exception implements IMitoException {

    public DatabaseException(){super();}

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseException(Throwable cause) {
        super(cause);
    }


    @Override
    public String getType() {
        return "Database";
    }
}
