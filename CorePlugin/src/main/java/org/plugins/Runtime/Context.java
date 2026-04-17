package org.plugins.Runtime;

import org.example.api.Runtime.IContext;

import java.nio.file.Path;

public class Context implements IContext{
    private Path currentDir;
    private Path home;
    private Boolean running;

    public Context() {
        this.currentDir = null;
        this.home = null;
        this.running = true;
    }

    public Path getCurrentDir() {
        return currentDir;
    }

    public void setCurrentDir(Path currentDir) {
        this.currentDir = currentDir;
    }

    public Path getHome() {
        return home;
    }

    public void setHome(Path home) {
        this.home = home;
    }

    public Boolean isRunning() {
        return running;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }
}
