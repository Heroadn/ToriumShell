package org.plugins.Runtime;

import org.example.api.Runtime.IConsole;

import java.io.PrintStream;

public class Console implements IConsole {
    private PrintStream out;

    public Console()                    { this.out = System.out; }
    public Console(PrintStream out)     { this.out = out; }

    public void print(String s)   { out.print(s); }
    public void println(String s) { out.println(s); }
    public void error(Exception e) { out.println("ERRO: " + e.getMessage()); }
}