package io.Exceptions;

/**
 * Created by peltzer on 26/11/2016.
 */
public class ARPException  extends Exception implements IMitoException {


    public ARPException() {
        super();
    }

    public ARPException(String message) {
        super(message);
    }

    public ARPException(String message, Throwable cause) {
        super(message, cause);
    }

    public ARPException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getType() {
        return "ARP";
    }
}
