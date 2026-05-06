package org.example.core.Parser;

import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;
import org.example.api.Event.IEventBus;

public class CommandNode implements ASTNode {
    private final ICommand command;
    private final IHandler handler;
    private final IEventBus bus;

    public CommandNode(
            ICommand command,
            IHandler handler,
            IEventBus bus)
    {
        this.command = command;
        this.handler = handler;
        this.bus = bus;
    }

    @Override
    public int execute(IContext context, IConsole console) {
        try {
            int exit = handler.execute(command, context, console);
            return exit;
        } catch (Exception e) {
            return 1;
        }
    }
}