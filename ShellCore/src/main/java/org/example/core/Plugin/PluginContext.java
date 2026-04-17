package org.example.core.Plugin;

import org.example.api.Command.ICommandRegistry;
import org.example.api.Plugin.IPluginContext;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;

public class PluginContext implements IPluginContext {

    private final ICommandRegistry commandRegistry;
    private final IContext context;
    private final IConsole console;

    public PluginContext(
            ICommandRegistry commandRegistry,
            IConsole console,
            IContext context) {
        this.commandRegistry = commandRegistry;
        this.context = context;
        this.console = console;
    }

    public ICommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    public IContext getContext() { return context; }

    public IConsole getConsole() { return console; }
}