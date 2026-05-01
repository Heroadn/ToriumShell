package org.example.core.Runtime;

import org.example.api.Runtime.IRuntimeContext;
import org.example.api.Runtime.Mode;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class RuntimeContext implements IRuntimeContext
{
    private Path currentDir;
    private Boolean running;
    private Mode mode;
    private int lastExitCode;
    private Map<String, String> variables;

    public RuntimeContext(
            Path currentDir,
            Boolean running,
            Mode mode,
            int lastExitCode) {
        this.currentDir = currentDir;
        this.running = running;
        this.mode = mode;
        this.lastExitCode = lastExitCode;
        this.variables = new HashMap<>();
    }

    @Override
    public Path getCurrentDir() {
        return this.currentDir;
    }

    @Override
    public void setCurrentDir(Path currentDir) {
        this.currentDir = currentDir;
    }

    @Override
    public Boolean isRunning() {
        return this.running;
    }

    @Override
    public void setRunning(Boolean running) {
        this.running = running;
    }

    @Override
    public Mode getMode() {
        return this.mode;
    }

    @Override
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    public int getLastExitCode() {
        return lastExitCode;
    }

    @Override
    public void setLastExitCode(int code) {
        this.lastExitCode = code;
    }

    @Override
    public LocalDateTime getLocalTime() {
        return LocalDateTime.now();
    }

    @Override
    public void setVariable(String name, String value)
    {
        variables.put(name, value);
    }

    @Override
    public String getVariable(String name) {
        return variables.get(name);
    }

    @Override
    public boolean hasVariable(String name) {
        return variables.containsKey(name);
    }
}
