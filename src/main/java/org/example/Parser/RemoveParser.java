package org.example.Parser;

import org.example.Command.RemoveCommand;
import org.example.Lexer.Token;

import java.util.List;
import java.util.Queue;

public class RemoveParser extends Parser {

    public RemoveParser(Queue<Token> tokens)
    {
        this.setTokens(tokens);
        allowedFlags.add("-r");
    }

    public RemoveParser(List<Token> tokens)
    {
        this.add(tokens);
        allowedFlags.add("-r");
    }

    @Override
    public RemoveCommand parse() throws Exception
    {
        RemoveCommand command = new RemoveCommand();

        if(!expect("rm"))
            throw new Exception("ERROR: rm EXPECTED");

        //
        ParsedArgs parsed = consumeArgs();
        command.setFlags(parsed.flags());
        command.setArgs(parsed.args());

        if(parsed.args().isEmpty())
            throw new Exception("ERROR: name of the file EXPECTED");

        return command;
    }
}