package io.Exceptions;

/**
 * Created by neukamm on 12.12.2016.
 */
public class ProjectException extends Exception implements IMitoException {

    public ProjectException(){
        super();
    }

    public ProjectException(String message) {
        super(message);
    }

    public ProjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProjectException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getType() {
        return "Project";
    }
}
