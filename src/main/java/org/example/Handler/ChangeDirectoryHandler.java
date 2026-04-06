package org.example.Handler;

import org.example.Command.ChangeDirectoryCommand;
import org.example.Command.Command;
import org.example.ShellContext;

import java.nio.file.Files;
import java.nio.file.Path;

public class ChangeDirectoryHandler implements Handler {

    @Override
    public void execute(Command command, ShellContext context) throws Exception {
        ChangeDirectoryCommand c = (ChangeDirectoryCommand) command;
        String path = c.getPath().replace("~", context.getHome().toString());

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
