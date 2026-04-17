package org.plugins.Handler;

import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;
import org.plugins.Command.ChangeDirectoryCommand;

import java.nio.file.Files;
import java.nio.file.Path;

public class ChangeDirectoryHandler implements IHandler {

    @Override
    public void execute(ICommand command, IContext context, IConsole console) throws Exception {
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
