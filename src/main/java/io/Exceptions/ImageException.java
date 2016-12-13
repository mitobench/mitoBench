package io.Exceptions;

/**
 * Created by neukamm on 13.12.16.
 */
public class ImageException extends Exception implements IMitoException  {

    public ImageException() {
        super();
    }

    public ImageException(String message) {
        super(message);
    }

    public ImageException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageException(Throwable cause) {
        super(cause);
    }



    @Override
    public String getType() {
        return null;
    }
}
