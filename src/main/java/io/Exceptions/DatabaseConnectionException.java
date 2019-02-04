package io.Exceptions;

/**
 * Created by neukamm on 12.07.17.
 */
public class DatabaseConnectionException extends Exception implements IMitoException {
    @Override
    public String getType() {
        return "database";
    }
}
