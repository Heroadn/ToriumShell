package org.example.Parser;

import org.example.Command.MakeDirectoryCommand;
import org.example.Lexer.Token;

import java.util.*;

public class MakeDirectoryParser extends Parser {
    public MakeDirectoryParser(Queue<Token> tokens)
    {
        this.setTokens(tokens);
        allowedFlags.add("-p");
    }

    public MakeDirectoryParser(List<Token> tokens)
    {
        this.add(tokens);
        allowedFlags.add("-p");
    }

    public MakeDirectoryCommand parse() throws Exception
    {
        MakeDirectoryCommand command = new MakeDirectoryCommand();

        if(!expect("mkdir"))
            throw new Exception("ERROR: mkdir EXPECTED");

        //
        ParsedArgs parsed = consumeArgs();
        command.setFlags(parsed.flags());
        command.setArgs(parsed.args());

        if(parsed.args().isEmpty())
            throw new Exception("ERROR: name of the file EXPECTED");

        return command;
    }
}