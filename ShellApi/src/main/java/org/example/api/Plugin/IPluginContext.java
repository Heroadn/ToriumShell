package org.example.api.Plugin;

import org.example.api.Command.ICommandRegistry;
import org.example.api.Runtime.InteractiveScreen;

public interface IPluginContext {
    ICommandRegistry getCommandRegistry();
    InteractiveScreen getInteractiveScreen();

    void setInteractiveScreen(InteractiveScreen screen);
}
