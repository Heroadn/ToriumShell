package org.plugins.Handler;

import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Scanner;
import java.util.stream.Stream;

public class WcHandler implements IHandler {

    @Override
    public void execute(
            ICommand command,
            IContext context,
            IConsole console) throws Exception
    {
        Path sourcePath = context.getCurrentDir().resolve(command.getArgs().getFirst());
        File source = new File(sourcePath.toString());

        if(!source.exists())
            throw new Exception(source.getPath() + " don't exists");

        if(command.has("-l"))
        {
            countLines(source);
        }

        if(command.has("-w"))
        {
            countWords(source);
        }

        if(command.has("-c"))
        {
            countChars(source);
        }

        console.println(countLines(source)
                + " "
                + countWords(source)
                + " "
                + countChars(source)
                + " "
                + source.getName());
    }

    public long countLines(File file)
    {
        long lineCount;
        try (Stream<String> stream = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
            lineCount = stream.count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lineCount;
    }

    public long countWords(File file) throws FileNotFoundException {
        long count = 0;
        try(Scanner sc = new Scanner(new FileInputStream(file)))
        {
            while(sc.hasNext())
            {
                sc.next();
                count++;
            }
        }

        return count;
    }

    public long countChars(File file)
    {
        int count = 0;
        try (FileReader reader = new FileReader(file)) {
            while (reader.read() != -1)
                count++;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }
}
