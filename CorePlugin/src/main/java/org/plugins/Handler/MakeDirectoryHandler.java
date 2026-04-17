package org.plugins.Handler;

import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MakeDirectoryHandler implements IHandler {

    @Override
    public void execute(ICommand command, IContext context, IConsole console) throws Exception
    {
        List<String> flags = command.getFlags();
        List<String> args  = command.getArgs();

        String fileName = args.getFirst();
        Path path = context.getCurrentDir().resolve(fileName);

        if(Files.exists(path))
            throw new Exception("Arquivo já existe: " + path);

        //is a folder but not -r
        if(isDirectoriesFlag(flags))
        {
            Files.createDirectories(path);
            console.println(directoryRecursiveSuccessMessage());
            return;
        }

        Files.createDirectory(path);
        console.println(directorySuccessMessage());
    }

    public String directorySuccessMessage()
    {
        return "Diretorio criado com sucesso";
    }

    public String directoryRecursiveSuccessMessage()
    {
        return "Diretorios criados com sucesso";
    }

    private static boolean isDirectoriesFlag(List<String> args) {
        return args.contains("-p");
    }

}
