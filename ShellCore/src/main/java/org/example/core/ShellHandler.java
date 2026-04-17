package org.example.core;

import org.example.core.Runtime.Console;
import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;
import org.example.core.Runtime.Context;
import org.example.core.Exception.UnknownCommandException;

public class ShellHandler {
    private final Context context;
    private final Console console;
    private final CommandRegistry registry;

    public ShellHandler(Context context,
                        Console console,
                        CommandRegistry registry) {
        this.context = context;
        this.console = console;
        this.registry = registry;
    }

    public void execute(ICommand command) throws Exception {
        IHandler handler = registry.getHandler(command.getClass()).get();

        if (handler == null) {
            throw new UnknownCommandException(command.getClass().getName());
        }

        handler.execute(command, context, console);
    }
}
