package org.example.api.Runtime;

import java.io.PrintStream;

public interface IConsole {
    public void print(String s);
    public void println(String s);
    public void error(Exception e);
}
