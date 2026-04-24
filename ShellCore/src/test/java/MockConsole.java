import org.example.api.Runtime.IConsole;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class MockConsole implements IConsole {
    private final List<String> lines = new ArrayList<>();
    private final List<String> errors = new ArrayList<>();

    @Override public void print(String s)       { lines.add(s); }
    @Override public void println(String s)      { lines.add(s); }
    @Override public void error(Exception e)     { errors.add(e.getMessage()); }
    @Override public void clear()                { }
    @Override public int readKey(long timeout)   { return -1; }
    @Override public void enterRawMode()         { }
    @Override public void exitRawMode() throws IOException { }

    public String output()                       { return String.join(System.lineSeparator(), lines); }
    public String lastLine()                     { return lines.isEmpty() ? "" : lines.get(lines.size() - 1); }
    public boolean hasError()                    { return !errors.isEmpty(); }
    public String lastError()                    { return errors.isEmpty() ? "" : errors.get(errors.size() - 1); }
    public void reset()                          { lines.clear(); errors.clear(); }
}