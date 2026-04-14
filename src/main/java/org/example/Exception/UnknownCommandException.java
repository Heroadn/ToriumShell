package org.example.Exception;

public class UnknownCommandException extends RuntimeException {
    public UnknownCommandException(String command) {
        super("ERROR: UNKNOWN COMMAND " + command);
    }
}
