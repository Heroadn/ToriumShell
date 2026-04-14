package org.example.Handler;

import org.example.Command.Command;
import org.example.IO.ShellConsole;
import org.example.ShellContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class ExitHandler implements Handler {

    @Override
    public void execute(Command command, ShellContext context, ShellConsole console) throws Exception {
        context.setRunning(false);
    }
}
