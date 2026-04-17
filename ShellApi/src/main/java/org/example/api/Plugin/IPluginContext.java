package org.example.api.Plugin;

import org.example.api.Command.ICommandRegistry;

public interface IPluginContext {
    ICommandRegistry getCommandRegistry();
}
