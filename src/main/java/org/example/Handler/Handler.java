package org.example.Handler;

import org.example.Command.Command;
import org.example.ShellContext;

public interface Handler {
    void execute(Command command, ShellContext context) throws Exception;
}
