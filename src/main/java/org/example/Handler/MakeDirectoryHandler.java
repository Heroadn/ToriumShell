package org.example.Handler;

import org.example.Command.Command;
import org.example.IO.ShellConsole;
import org.example.ShellContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.util.Collections.reverseOrder;

public class MakeDirectoryHandler implements Handler {

    @Override
    public void execute(Command command, ShellContext context, ShellConsole console) throws Exception
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
