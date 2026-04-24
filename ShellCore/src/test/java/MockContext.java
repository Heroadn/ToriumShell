import org.example.api.Runtime.IContext;
import org.example.api.Runtime.Mode;

import java.nio.file.Path;

class MockContext implements IContext {
    private Path currentDir;
    private Path home;
    private boolean running = true;
    private Mode mode = Mode.NORMAL;
    private String userName = "testuser";
    private String prompt = "{user}@shell:{dir} $ ";

    public MockContext(Path dir) { this.currentDir = dir; this.home = dir; }

    @Override public Path getCurrentDir()              { return currentDir; }
    @Override public void setCurrentDir(Path p)        { this.currentDir = p; }
    @Override public Path getHome()                    { return home; }
    @Override public void setHome(Path p)              { this.home = p; }
    @Override public Boolean isRunning()               { return running; }
    @Override public void setRunning(Boolean r)        { this.running = r; }
    @Override public Mode getMode()                    { return mode; }
    @Override public void setMode(Mode m)              { this.mode = m; }
    @Override public String getUserName()              { return userName; }
    @Override public void setUserName(String n)        { this.userName = n; }
    @Override public String getPrompt()                { return prompt; }
    @Override public void setPrompt(String p)          { this.prompt = p; }
}
