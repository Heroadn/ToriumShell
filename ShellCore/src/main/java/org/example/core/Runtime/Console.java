package org.example.core.Runtime;

import org.example.api.Runtime.IConsole;
import org.jline.terminal.Terminal;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

public class Console implements IConsole {
    private final Terminal terminal;
    private final PrintWriter out;

    public Console(Terminal terminal) {
        this.terminal = terminal;
        this.out = new PrintWriter(terminal.writer(), true);
    }

    @Override
    public void print(String s)    { out.print(s);}

    @Override
    public void println(String s)  { out.println(s); }

    @Override
    public void error(Exception e) { out.println("ERRO: " + e.getMessage()); }

    @Override
    public void clear() {
        out.print("\033[H\033[2J");
    }

    @Override
    public int readKey(long timeoutMillis) {
        try {
            return terminal.reader().read(timeoutMillis);
        } catch (IOException e) {
            return -1;
        }
    }

    @Override
    public void enterRawMode() {
        terminal.enterRawMode();
    }

    @Override
    public void exitRawMode() throws IOException {
        terminal.close();
    }
}