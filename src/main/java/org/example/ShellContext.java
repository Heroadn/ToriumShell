package org.example;

import java.nio.file.Path;

public class ShellContext   {
    private Path currentDir;
    private Path home;
    private Boolean running;
    //TODO: maybe variables defined like
    //a hashmap that any handler can access and share


    public ShellContext() {
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
