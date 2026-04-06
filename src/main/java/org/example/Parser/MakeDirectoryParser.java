package org.example.Parser;

import org.example.Command.MakeDirectoryCommand;
import org.example.Command.RemoveCommand;
import org.example.Lexer.Token;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MakeDirectoryParser extends Parser {
    public MakeDirectoryParser(List<Token> tokens)
    {
        this.setTokens(tokens);
        allowedFlags.add("-p");
    }

    public MakeDirectoryCommand parse() throws Exception
    {
        MakeDirectoryCommand command = new MakeDirectoryCommand();

        if(!expect("mkdir"))
            throw new Exception("ERROR: mkdir EXPECTED");

        if(!peek(Token.TYPES.STRING))
            throw new Exception("ERROR: name of the file EXPECTED");
        command.setFileName(consume().value);

        //getting args and validation
        command.setArgs(consumeArgs());
        return command;
    }
}