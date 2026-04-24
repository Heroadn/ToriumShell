package org.plugins.Handler;

import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TreeHandler implements IHandler {

    @Override
    public void execute(ICommand command, IContext context, IConsole console) throws Exception {
        List<String> args = command.getFlags();
        Path dir = args.isEmpty()
                ? context.getCurrentDir()
                : context.getCurrentDir().resolve(args.getFirst());

        int maxDepth = 4;

        if(command.has("-n"))
            maxDepth = Integer.parseInt(command.getArgs().getFirst());

        console.println(dir.getFileName().toString());
        printDirs(console, dir, "", 0, maxDepth);
    }

    private static void printDirs(
            IConsole console,
            Path dir,
            String prefix,
            int depth,
            int MAX_DEPTH) throws IOException
    {
        File[] files = new File(dir.toString()).listFiles();
        for (int i = 0; i < files.length; i++) {
            Path path = files[i].toPath();
            boolean isLast = (i == files.length - 1);
            String connector = isLast ? "└── " : "├── ";

            console.println(prefix + connector + path.getFileName());

            if (Files.isDirectory(path) && depth <= MAX_DEPTH) {
                String newPrefix = prefix + (isLast ? "    " : "│   ");
                printDirs(console, path, newPrefix, depth + 1, MAX_DEPTH);
            }
        }
    }

    private static List<String> listFiles(String dir) {
        return Stream.of(Objects.requireNonNull(new File(dir).listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toList());
    }
}

