package org.plugins;

import org.example.api.Plugin.IPlugin;
import org.example.api.Plugin.IPluginContext;
import org.example.api.Plugin.Plugin;

@Plugin(name = "core", version = "1.0")
public class CorePlugin implements IPlugin {

    @Override
    public String getName() {
        return "core";
    }
}
