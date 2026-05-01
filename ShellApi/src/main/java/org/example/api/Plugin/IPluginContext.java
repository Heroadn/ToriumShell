package org.example.api.Plugin;

import org.example.api.Command.ICommandRegistry;
import org.example.api.Event.EventBus;
import org.example.api.Runtime.InteractiveScreen;

public interface IPluginContext {
    ICommandRegistry getCommandRegistry();
    InteractiveScreen getInteractiveScreen();
    EventBus getEventBus();

    void setInteractiveScreen(InteractiveScreen screen);
}
