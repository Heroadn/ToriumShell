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
import java.nio.file.attribute.FileTime;
import java.time.Instant;

public class TouchHandler implements IHandler {

    @Override
    public void execute(
            ICommand command,
            IContext context,
            IConsole console) throws Exception
    {

        for (String path : command.getArgs())
        {
            String resolvedPath = String.valueOf(context.getCurrentDir().resolve(path));
            File file = new File(resolvedPath);
            if(!file.exists())
            {
                createFile(resolvedPath);
                continue;
            }

            updateTimeStamp(resolvedPath);
        }
    }

    private void updateTimeStamp(String path)
    {
        FileTime newTime = FileTime.from(Instant.now());
        try {
            Files.setLastModifiedTime(Path.of(path), newTime);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createFile(String path)
    {
        File file = new File(path);

        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
