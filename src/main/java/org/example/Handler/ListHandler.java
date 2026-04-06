package org.example.Handler;

import org.example.Command.Command;
import org.example.ShellContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class ListHandler implements Handler {

    @Override
    public void execute(Command command, ShellContext context) throws Exception {
        List<String> args = command.getArgs();
        Path dir = args.isEmpty()
                ? context.getCurrentDir()
                : context.getCurrentDir().resolve(args.get(0));

        //TODO:File formatter
        try (Stream<Path> stream = Files.list(dir)) {
            stream.forEach(path -> {
                if (Files.isDirectory(path)) {
                    System.out.println("\\" + path.getFileName());
                } else {
                    System.out.println(" " + path.getFileName());
                }
            });
        }
    }
}
