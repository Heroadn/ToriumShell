package org.example.core.Runtime;

import org.example.api.Runtime.IContext;
import org.example.api.Runtime.Mode;

import java.nio.file.Path;

public class Context implements IContext{
    private Path currentDir;
    private Path home;
    private Boolean running;
    private Mode mode;
    private String userName;
    private String prompt;

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

    public void setMode(Mode mode) { this.mode = mode; }

    public Mode getMode() { return this.mode; }

    public String getUserName() { return this.userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public String getPrompt()
    {
        return this.prompt;
    }

    public void setPrompt(String prompt)
    {
        this.prompt = prompt;
    }
}
