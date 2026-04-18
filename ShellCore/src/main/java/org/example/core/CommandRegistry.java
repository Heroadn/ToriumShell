package org.example.core;
import org.example.api.Command.Command;
import org.example.api.Command.ICommand;
import org.example.api.Command.ICommandRegistry;
import org.example.api.Handler.IHandler;
import org.example.api.Parser.IParser;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CommandRegistry implements ICommandRegistry
{
    private final Map<Class<? extends ICommand>, Supplier<IParser>> parsers = new HashMap<>();
    private final Map<Class<? extends ICommand>, Supplier<IHandler>> handlers = new HashMap<>();
    private final Map<String, Class<?>> classes = new HashMap<>(); //ListCommand -> ls

    @Override
    public void register(
            Class<? extends ICommand> command,
            Supplier<IParser> parser,
            Supplier<IHandler> handler)
    {
        Command annotation = command.getAnnotation(Command.class);

        if (annotation == null)
            throw new RuntimeException("Sem @Command: " + command.getName());
        if (classes.containsKey(annotation.name()))
            throw new RuntimeException("Comando já registrado: " + annotation.name());

        parsers.put(command, parser);
        handlers.put(command, handler);
        classes.put(annotation.name(), command);
    }

    public Supplier<IParser> getParser(String name) {
        return parsers.get(classes.get(name));
    }

    public Supplier<IParser> getParser(Class<? extends ICommand>  command) {
        return parsers.get(command);
    }

    public Supplier<IHandler> getHandler(Class<? extends ICommand>command)
    {
        return handlers.get(command);
    }

    public boolean has(String name)
    {
        return classes.containsKey(name);
    }
}
