package org.example.api.Plugin;

public interface IPlugin {
    String getName();
    void onStart(IPluginContext context);
    void onStop(IPluginContext context);
}
