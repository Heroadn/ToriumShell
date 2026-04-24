package org.plugins.Handler;

import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class CatHandler implements IHandler {
    //TODO: -n -> number of lines
    @Override
    public void execute(ICommand command, IContext context, IConsole console) throws Exception {
        Path path = context.getCurrentDir().resolve(command.getArgs().getFirst());
        File file = new File(path.toString());

        if(!file.exists())
            throw new Exception("Arquivo nao encontrado");

        if(file.isDirectory())
            throw new Exception("Não é um arquivo");

        if(command.has("-n"))
        {
            String num = String.valueOf(Files.lines(file.toPath()).count());
            console.println("Número de linhas: " + num);
            return;
        }

        console.println(Files.readString(file.toPath()));
    }
}
