package org.example.api.Runtime;

public interface InteractiveScreen {
    void render(IConsole console);
    void onKey(char ch);
    void onTick(int tickCount);
    boolean isRunning();
}
