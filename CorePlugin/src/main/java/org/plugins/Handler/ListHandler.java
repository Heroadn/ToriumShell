package org.plugins.Handler;

import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

//@Command(name = "ls", description = "List files", usage = "ls [-a] [path]")
public class ListHandler implements IHandler {

    @Override
    public void execute(ICommand command, IContext context, IConsole console) throws Exception {
        List<String> args = command.getFlags();
        Path dir = args.isEmpty()
                ? context.getCurrentDir()
                : context.getCurrentDir().resolve(args.getFirst());

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
