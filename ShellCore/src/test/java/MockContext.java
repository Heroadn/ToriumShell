import org.example.api.Runtime.IContext;
import org.example.api.Runtime.IRuntimeContext;
import org.example.api.Runtime.ISessionContext;
import org.example.api.Runtime.Mode;
import org.example.core.Runtime.SessionContext;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

class MockContext implements IContext {

    private final ISessionContext session;
    private final IRuntimeContext runtime;

    public MockContext(Path dir, ISessionContext context) {
        this.session = context;
        this.runtime = new MockRuntimeContext(dir);
    }

    @Override public ISessionContext getSession() { return session; }
    @Override public IRuntimeContext getRuntime() { return runtime; }

    // --- SessionContext ---
    static class MockSessionContext implements ISessionContext {
        private final Path home;
        private String userName = "testuser";
        private String prompt = "{user}@shell:{dir} $ ";

        MockSessionContext(Path home, String userName) {
            this.home = home;
            this.userName = userName;
        }

        @Override public Path getHome()                  { return home; }
        @Override public String getUserName()            { return userName; }
        @Override public String getPrompt()              { return prompt; }
        @Override public void setPrompt(String p)        { this.prompt = p; }
    }

    // --- RuntimeContext ---
    static class MockRuntimeContext implements IRuntimeContext {
        private Path currentDir;
        private boolean running = true;
        private Mode mode = Mode.NORMAL;
        private int lastExitCode = 0;
        private final Map<String, String> variables = new HashMap<>();

        MockRuntimeContext(Path dir) { this.currentDir = dir; }

        @Override public Path getCurrentDir()            { return currentDir; }
        @Override public void setCurrentDir(Path p)      { this.currentDir = p; }
        @Override public Boolean isRunning()             { return running; }
        @Override public void setRunning(Boolean r)      { this.running = r; }
        @Override public Mode getMode()                  { return mode; }
        @Override public void setMode(Mode m)            { this.mode = m; }
        @Override public int getLastExitCode()           { return lastExitCode; }
        @Override public void setLastExitCode(int c)     { this.lastExitCode = c; }

        @Override
        public LocalDateTime getLocalTime() {
            return LocalDateTime.now();
        }

        @Override public void setVariable(String name, String value) { variables.put(name, value); }
        @Override public String getVariable(String name)             { return variables.getOrDefault(name, ""); }
        @Override public boolean hasVariable(String name)            { return variables.containsKey(name); }
    }
}