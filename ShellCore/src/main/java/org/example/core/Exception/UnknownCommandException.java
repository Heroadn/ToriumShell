package org.example.core.Exception;

public class UnknownCommandException extends RuntimeException {
    public UnknownCommandException(String command) {
        super("ERROR: UNKNOWN COMMAND " + command);
    }
}
