package org.plugins.Handler;

import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class MoveHandler implements IHandler {

    @Override
    public void execute(ICommand command, IContext context, IConsole console) throws Exception {
        Path sourcePath = context.getCurrentDir().resolve(command.getArgs().getFirst());
        Path targetPath = context.getCurrentDir().resolve(command.getArgs().get(1));

        File source = new File(sourcePath.toString());
        File target = new File(targetPath.toString());

        if(!source.exists())
            throw new Exception(source.getPath() + " don't exists");

        if(command.has("-r"))
        {
            try {
                moveDirectory(sourcePath, targetPath);
                return;
            } catch (IOException e) {
                console.println("Error during file move: " + e.getMessage());
            }
        }

        if(source.isDirectory())
            throw new Exception("[source] is not a file, for moving directories use -r.");

        if (target.isDirectory())
            targetPath = targetPath.resolve(sourcePath.getFileName());

        moveFile(sourcePath, targetPath);
    }

    private static void moveDirectory(Path source, Path target) throws IOException
    {
        Files.walkFileTree(source, new SimpleFileVisitor<>() {

            //Cria a pasta no destino
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetDirPath = target.resolve(source.relativize(dir));
                Files.createDirectories(targetDirPath);
                return FileVisitResult.CONTINUE;
            }

            //Move o arquivo
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path targetFile = target.resolve(source.relativize(file));
                Files.move(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }

            //Apaga a pasta de origem, depois de esvaziá-la
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void moveFile(Path source, Path target) throws IOException {
        Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
    }
}
