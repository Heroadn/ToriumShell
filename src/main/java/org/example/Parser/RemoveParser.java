package org.example.Parser;

import org.example.Command.RemoveCommand;
import org.example.Lexer.Token;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RemoveParser extends Parser {

    public RemoveParser(List<Token> tokens)
    {
        this.setTokens(tokens);
        allowedFlags.add("-r");
    }

    public RemoveCommand parse() throws Exception
    {
        RemoveCommand command = new RemoveCommand();

        if(!expect("rm"))
            throw new Exception("ERROR: rm EXPECTED");

        if(!peek(Token.TYPES.STRING))
            throw new Exception("ERROR: name of the file EXPECTED");
        command.setFileName(consume().value);

        //getting args and validation
        command.setArgs(consumeArgs());
        return command;
    }
}