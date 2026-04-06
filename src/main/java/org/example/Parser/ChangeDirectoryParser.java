package org.example.Parser;

import org.example.Command.ChangeDirectoryCommand;
import org.example.Lexer.Token;

import java.nio.file.Path;
import java.util.List;

public class ChangeDirectoryParser extends Parser {

    public ChangeDirectoryParser(List<Token> tokens)
    {
        this.setTokens(tokens);
    }

    public ChangeDirectoryCommand parse() throws Exception
    {
        ChangeDirectoryCommand command = new ChangeDirectoryCommand();

        if(!expect("cd"))
            throw new Exception("ERROR: cd EXPECTED");

        if(!peek(Token.TYPES.STRING))
            throw new Exception("ERROR: directory EXPECTED: " + peek());

        //args
        command.setArgs(consumeArgs());
        return command;
    }
}