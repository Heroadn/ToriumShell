package org.plugins.Handler;

import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GrepHandler implements IHandler {

    @Override
    public void execute(
            ICommand command,
            IContext context,
            IConsole console) throws Exception
    {
        String string = command.getArgs().getFirst();
        File source = new File(context.getCurrentDir()
                .resolve(command.getArgs().get(1))
                .toString());

        //
        List<Long> result;
        boolean isCaseSensitive = !command.has("-i");
        boolean isInverse   = command.has("-v");

        if(!source.exists())
            throw new Exception(source.getPath() + " don't exists");

        List<String> lines = Files.readAllLines(source.toPath(), StandardCharsets.UTF_8);
        result = search(lines, string, isCaseSensitive, isInverse);

        if(!command.has("-n") && !command.has("-c"))
        {
            for (Long line : result)
                console.println(lines.get(Math.toIntExact(line)));
        }

        if(command.has("-n"))
        {
            for (Long line : result)
                console.println((line + 1) + ": " + lines.get(Math.toIntExact(line)));
        }

        if(command.has("-c"))
        {
            console.println(String.valueOf(result.size()));
        }
    }

    public List<Long> search(
            List<String> lines,
            String string,
            boolean isCaseSensitive,
            boolean isInverse)
    {
        List<Long> result = new ArrayList<>();//stores number of the line

        for (int i = 0; i < lines.size(); i++)
        {
            String line = lines.get(i);
            boolean isFound = contains(line, string, isCaseSensitive);

            if(isInverse && !isFound)
            {
                result.add((long) i);
            }else if(!isInverse &&isFound)
                result.add((long) i);
        }

        return result;
    }

    private boolean contains(String line, String target, boolean isCaseSensitive)
    {
        if(isCaseSensitive)
            return line.contains(target);

        return line.toLowerCase().contains(target.toLowerCase(Locale.ROOT));
    }
}
