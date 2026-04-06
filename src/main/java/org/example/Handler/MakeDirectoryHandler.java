package org.example.Handler;

import org.example.Command.Command;
import org.example.Command.RemoveCommand;
import org.example.ShellContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.reverseOrder;

public class MakeDirectoryHandler implements Handler {

    @Override
    public void execute(Command command, ShellContext context) throws Exception
    {
        List<String> args = command.getArgs();
        String fileName = ((RemoveCommand)command).getFileName();
        Path path = context.getCurrentDir().resolve(fileName);

        if(!Files.exists(path))
            throw new Exception("Arquivo não existe: " + path);

        //is a folder but not -r
        if(isDirectoriesArg(args))
        {
            Files.createDirectories(path);
            IO.println("Diretorios criado com sucessor");
            return;
        }

        Files.createDirectory(path);
        IO.println("Diretorio criado com sucessor");
    }

    private static boolean isDirectoriesArg(List<String> args) {
        return args.contains("-p");
    }
}
