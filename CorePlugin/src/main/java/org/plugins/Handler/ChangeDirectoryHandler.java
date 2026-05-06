package org.plugins.Handler;

import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;
import org.plugins.Command.ChangeDirectoryCommand;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ChangeDirectoryHandler implements IHandler {

    @Override
    public int execute(ICommand command, IContext context, IConsole console) throws Exception {
        ChangeDirectoryCommand c = (ChangeDirectoryCommand) command;

        //cd command without a path should go to home
        if(c.getArgs().isEmpty())
        {
            c.setArgs(List.of(
                    String.valueOf(context.getSession().getHome()))
            );
        }

        String fileName = c.getArgs().getFirst();
        String path = fileName.replace("~", context.getSession().getHome().toString());

        Path newPath = context.getRuntime().getCurrentDir()
                .resolve(path)
                .normalize();

        if (!Files.exists(newPath)) {
            throw new Exception("Diretório não existe");
        }

        if (!Files.isDirectory(newPath)) {
            throw new Exception("Não é um diretório");
        }

        context.getRuntime().setCurrentDir(newPath);
        return 0;
    }
}
