package org.plugins.Handler;

import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class HeadHandler implements IHandler {
    //TODO: -n -> number of lines
    @Override
    public void execute(ICommand command, IContext context, IConsole console) throws Exception {
        Path path = context.getCurrentDir().resolve(command.getArgs().getFirst());
        int max = 10;

        //head [-n 5] filename

        if(command.has("-n"))
        {
            String input = command.getArgs().getFirst();
            max  = Integer.parseInt(input);
            path = context.getCurrentDir().resolve(command.getArgs().get(1));
        }


        File file = new File(path.toString());
        if(!file.exists())
            throw new Exception("Arquivo nao encontrado");

        if(file.isDirectory())
            throw new Exception("Não é um arquivo");

        List<String> lines = Files.readAllLines(file.toPath());
        max = Math.min(max, lines.size());
        for (int i = 0; i < max; i++) {
            String line = lines.get(i);
            console.println(line);
        }
    }
}
