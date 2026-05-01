package org.plugins;

import org.example.api.Event.CommandExecuted;
import org.example.api.Event.CommandReceived;
import org.example.api.Plugin.IPlugin;
import org.example.api.Plugin.IPluginContext;
import org.example.api.Plugin.Plugin;
import org.plugins.Command.HistoryCommand;
import org.plugins.Handler.HistoryHandler;
import org.plugins.Parser.HistoryParser;

@Plugin(name = "core", version = "1.0")
public class CorePlugin implements IPlugin {
    private final HistoryManager historyManager = new HistoryManager();

    @Override
    public String getName() {
        return "core";
    }

    @Override
    public void onStart(IPluginContext context)
    {
        context.getEventBus().subscribe(CommandReceived.class, historyManager::record);
        context.getCommandRegistry().register(
                HistoryCommand.class,
                HistoryParser::new,
                () -> new HistoryHandler(historyManager)
        );
        //TODO ("ADICIONAR LISTENER DE PLUGIN CARREGARDO AO REGISTRY");
    }

    @Override
    public void onStop(IPluginContext context)
    {
    }
}
