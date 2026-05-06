package org.example.core.Plugin;

import org.example.api.Command.ICommandRegistry;
import org.example.api.Event.IEventBus;
import org.example.api.Plugin.IPluginContext;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;
import org.example.api.Runtime.InteractiveScreen;

public class PluginContext implements IPluginContext {

    private final ICommandRegistry commandRegistry;
    private final IContext context;
    private final IConsole console;
    private final IEventBus bus;
    private InteractiveScreen screen;

    public PluginContext(
            ICommandRegistry commandRegistry,
            IEventBus IEventBus,
            IConsole console,
            IContext context) {
        this.commandRegistry = commandRegistry;
        this.context = context;
        this.console = console;
        this.bus = IEventBus;
    }

    public ICommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    public void setInteractiveScreen(InteractiveScreen screen) {
        this.screen = screen;
    }

    public InteractiveScreen getInteractiveScreen() {
        return this.screen;
    }

    public IContext getContext() { return context; }

    public IConsole getConsole() { return console; }

    @Override
    public IEventBus getEventBus() {
        return this.bus;
    }
}