package io.Exceptions;

import java.io.PrintWriter;

/**
 * Created by peltzer on 23/11/2016.
 * This is an interface for all of our Exceptions that we can generate in the Mitobench tool in the end.
 *
 */
public interface IMitoException {
    public void printStackTrace(PrintWriter pw);
    public String getType();




}
