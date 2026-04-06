package org.example.Parser;

import org.example.Command.ListCommand;
import org.example.Lexer.Token;

import java.util.List;

public class ListParser extends Parser {

    public ListParser(List<Token> tokens)
    {
        this.setTokens(tokens);
    }

    public ListCommand parse() throws Exception
    {
        ListCommand command = new ListCommand();

        if(!expect("ls"))
            throw new Exception("ERROR: ls EXPECTED");

        command.setArgs(consumeArgs());
        return command;
    }
}