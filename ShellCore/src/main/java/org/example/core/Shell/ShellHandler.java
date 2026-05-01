package org.example.core.Shell;

import org.example.core.Parser.ASTNode;
import org.example.core.CommandRegistry;
import org.example.core.Runtime.Console;
import org.example.core.Runtime.Context;

public class ShellHandler {
    private final Context context;
    private final Console console;
    private final CommandRegistry registry;


    public ShellHandler(Context context,
                        Console console,
                        CommandRegistry registry) {
        this.context = context;
        this.console = console;
        this.registry = registry;
    }

    public void execute(ASTNode node) throws Exception {
        int exit = node.execute(context, console);
    }
}
