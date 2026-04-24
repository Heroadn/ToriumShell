package org.example.core.Shell;

import org.example.api.Command.ICommand;
import org.example.api.Lexer.Lexer;

public class ShellExecutor
{
    private final Lexer lexer;
    private final ShellParser parser;
    private final ShellHandler handler;

    public ShellExecutor(Lexer lexer, ShellParser parser, ShellHandler handler) {
        this.lexer = lexer;
        this.parser = parser;
        this.handler = handler;
    }

    public void execute(String input) throws Exception {
        lexer.setInput(input);
        ICommand command = parser.parse(lexer.tokenizer());
        handler.execute(command);
    }
}
