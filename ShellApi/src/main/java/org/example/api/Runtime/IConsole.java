package org.example.api.Runtime;

import java.io.IOException;
import java.io.PrintStream;

public interface IConsole {
    public void print(String s);
    public void println(String s);
    public void error(Exception e);
    public void clear();

    public int readKey(long timeoutMillis);
    public void enterRawMode();
    public void exitRawMode() throws IOException;
}
