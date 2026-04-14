package org.example.Handler;

import org.example.Command.Command;
import org.example.IO.ShellConsole;
import org.example.ShellContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class ListHandler implements Handler {

    @Override
    public void execute(Command command, ShellContext context, ShellConsole console) throws Exception {
        List<String> args = command.getFlags();
        Path dir = args.isEmpty()
                ? context.getCurrentDir()
                : context.getCurrentDir().resolve(args.get(0));

        //TODO:File formatter
        //TODO:File service class
        try (Stream<Path> stream = Files.list(dir)) {
            stream.forEach(path -> {
                if (Files.isDirectory(path)) {
                    console.println("\\" + path.getFileName());
                } else {
                    console.println(" " + path.getFileName());
                }
            });
        }
    }
}
