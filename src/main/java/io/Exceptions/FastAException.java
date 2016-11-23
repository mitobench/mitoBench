package io.Exceptions;

/**
 * Created by peltzer on 23/11/2016.
 */

/**
 * Created by peltzer on 22/11/2016.
 */
public class FastAException extends Exception implements IMitoException {
    public FastAException() {
        super();
    }

    public FastAException(String message) {
        super(message);
    }

    public FastAException(String message, Throwable cause) {
        super(message, cause);
    }

    public FastAException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getType() {
        return "FastA";
    }
}
