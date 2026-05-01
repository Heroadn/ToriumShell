package org.example.core.Plugin;

import org.example.api.Command.ICommandRegistry;
import org.example.api.Event.EventBus;
import org.example.api.Plugin.IPluginContext;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;
import org.example.api.Runtime.InteractiveScreen;

public class PluginContext implements IPluginContext {

    private final ICommandRegistry commandRegistry;
    private final IContext context;
    private final IConsole console;
    private final EventBus bus;
    private InteractiveScreen screen;

    public PluginContext(
            ICommandRegistry commandRegistry,
            EventBus eventBus,
            IConsole console,
            IContext context) {
        this.commandRegistry = commandRegistry;
        this.context = context;
        this.console = console;
        this.bus = eventBus;
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
    public EventBus getEventBus() {
        return this.bus;
    }
}