package org.example.Parser;

import org.example.Command.ExitCommand;
import org.example.Command.RemoveCommand;
import org.example.Lexer.Token;

import java.util.List;
import java.util.Queue;

public class ExitParser extends Parser {

    public ExitParser(Queue<Token> tokens)
    {
        this.setTokens(tokens);
    }

    public ExitParser(List<Token> tokens)
    {
        this.add(tokens);
        allowedFlags.add("-r");
    }

    @Override
    public ExitCommand parse() throws Exception
    {
        ExitCommand command = new ExitCommand();

        if(!expect("exit"))
            throw new Exception("ERROR: exit EXPECTED");

        return command;
    }
}