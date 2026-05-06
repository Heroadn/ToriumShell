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
    public int execute(ICommand command, IContext context, IConsole console) throws Exception
    {
        List<String> args  = command.getArgs();

        String fileName = args.getFirst();
        Path path = context.getRuntime().getCurrentDir().resolve(fileName);

        if(Files.exists(path))
            throw new Exception("Arquivo já existe: " + path);

        //is a folder but not -r
        if(command.has("-p"))
        {
            Files.createDirectories(path);
            console.println(directoryRecursiveSuccessMessage());
            return 0;
        }

        Files.createDirectory(path);
        console.println(directorySuccessMessage());
        return 0;
    }

    public String directorySuccessMessage()
    {
        return "Diretorio criado com sucesso";
    }

    public String directoryRecursiveSuccessMessage()
    {
        return "Diretorios criados com sucesso";
    }

}
