package org.example.Handler;

import org.example.Command.ChangeDirectoryCommand;
import org.example.Command.Command;
import org.example.IO.ShellConsole;
import org.example.ShellContext;

import java.nio.file.Files;
import java.nio.file.Path;

public class ChangeDirectoryHandler implements Handler {

    @Override
    public void execute(Command command, ShellContext context, ShellConsole console) throws Exception {
        ChangeDirectoryCommand c = (ChangeDirectoryCommand) command;
        String fileName = c.getArgs().getFirst();
        String path = fileName.replace("~", context.getHome().toString());

        Path newPath = context.getCurrentDir()
                .resolve(path)
                .normalize();

        if (!Files.exists(newPath)) {
            throw new Exception("Diretório não existe");
        }

        if (!Files.isDirectory(newPath)) {
            throw new Exception("Não é um diretório");
        }

        context.setCurrentDir(newPath);
    }
}
