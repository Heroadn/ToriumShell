package org.example.Parser;

import org.example.Command.ListCommand;
import org.example.Lexer.Token;

import java.util.List;
import java.util.Queue;

public class ListParser extends Parser {

    public ListParser(Queue<Token> tokens) {
        this.setTokens(tokens);
    }

    public ListParser(List<Token> tokens) { this.add(tokens); }

    @Override
    public ListCommand parse() throws Exception
    {
        ListCommand command = new ListCommand();

        if(!expect("ls"))
            throw new Exception("ERROR: ls EXPECTED");

        //
        ParsedArgs parsed = consumeArgs();
        command.setFlags(parsed.flags());
        command.setArgs(parsed.args());

        return command;
    }
}