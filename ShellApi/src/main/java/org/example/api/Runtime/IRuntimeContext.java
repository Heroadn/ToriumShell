package org.example.api.Runtime;

import java.nio.file.Path;
import java.time.LocalDateTime;

public interface IRuntimeContext {
    Path getCurrentDir();
    void setCurrentDir(Path currentDir);

    Boolean isRunning();
    void setRunning(Boolean running);

    Mode getMode();
    void setMode(Mode mode);

    int getLastExitCode();
    void setLastExitCode(int code);

    public LocalDateTime getLocalTime();

    void setVariable(String name, String value);
    String getVariable(String name);   // null se não existe
    boolean hasVariable(String name);
}