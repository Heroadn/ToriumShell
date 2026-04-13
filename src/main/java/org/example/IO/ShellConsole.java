package org.example.IO;

import java.io.PrintStream;

public class ShellConsole {
    private PrintStream out;

    public ShellConsole()                    { this.out = System.out; }
    public ShellConsole(PrintStream out)     { this.out = out; }

    public void print(String s)   { out.print(s); }
    public void println(String s) { out.println(s); }
}