package org.example.api.Runtime;

import java.nio.file.Path;

public interface IContext {
    public Path getCurrentDir();

    public void setCurrentDir(Path currentDir);

    public Path getHome();

    public void setHome(Path home);

    public Boolean isRunning();

    public void setRunning(Boolean running);
}
