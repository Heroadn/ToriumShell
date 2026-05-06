package org.plugins.Handler;

import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FindHandler implements IHandler {

    @Override
    public int execute(
            ICommand command,
            IContext context,
            IConsole console) throws Exception
    {
        Path path = context.getRuntime().getCurrentDir();
        List<String> result = new ArrayList<>();

        String type   = command.has("-t") ? command.getValue("-t") : null;
        int maxDepth  = command.has("-d") ? Integer.parseInt(command.getValue("-d")) : Integer.MAX_VALUE;
        String target = command.getArgs().isEmpty() ? null : command.getArgs().getFirst();
        result = search(path, target, type, maxDepth);

        for (String line : result)
            console.println(line);

        return 0;
    }

    private List<String> search(Path source, String target, String type, int maxDepth) throws IOException {
        List<String> result = new ArrayList<>();

        Files.walkFileTree(source, new SimpleFileVisitor<>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                if (dir.equals(source)) return FileVisitResult.CONTINUE;
                int currentDepth = source.relativize(dir).getNameCount();
                if (currentDepth > maxDepth) return FileVisitResult.SKIP_SUBTREE;

                if (type == null || type.equals("dir"))
                    if (target == null || dir.getFileName().toString().equals(target))
                        result.add(dir.toString());

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                int currentDepth = source.relativize(file).getNameCount();
                if (currentDepth > maxDepth) return FileVisitResult.CONTINUE;

                if (type == null || type.equals("file"))
                    if (target == null || file.getFileName().toString().equals(target))
                        result.add(file.toString());

                return FileVisitResult.CONTINUE;
            }
        });

        return result;
    }
}
