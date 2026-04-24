package org.plugins.Handler;

import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class CopyHandler implements IHandler {

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
                copyDirectory(sourcePath, targetPath);
                return;
            } catch (IOException e) {
                console.println("Error during file copy: " + e.getMessage());
            }
        }

        if(source.isDirectory())
            throw new Exception("[source] is not a file, for copying directories use -r.");

        copyFile(source, target);
    }

    private static void copyDirectory(Path source, Path target) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(
                    Path file,
                    BasicFileAttributes attrs) throws IOException
            {
                //diff between two files
                Path targetFile = target.resolve(source.relativize(file));
                Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(
                    Path dir,
                    BasicFileAttributes attrs) throws IOException
            {
                Path targetDirPath = target.resolve(source.relativize(dir));
                Files.createDirectories(targetDirPath);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static long copyFile(File source, File target) {
        try (FileChannel sourceChannel = new FileInputStream(source).getChannel();
             FileChannel targetChannel = new FileOutputStream(target).getChannel()) {

            return sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);
            //System.out.println("Copied " + transferred + " bytes successfully.");
        } catch (IOException e) {
            //System.err.println("Error during large file copy: " + e.getMessage());
            return 0;
        }
    }

    private static boolean isInCurrentDir(IContext context, Path dir) {
        return dir.startsWith(context.getCurrentDir());
    }
}
