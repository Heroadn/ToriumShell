package org.example.Parser;

import org.example.Command.ChangeDirectoryCommand;
import org.example.Lexer.Token;

import java.util.Queue;

public class ChangeDirectoryParser extends Parser {

    public ChangeDirectoryParser(Queue<Token> tokens) {
        this.setTokens(tokens);
    }

    public ChangeDirectoryCommand parse() throws Exception
    {
        ChangeDirectoryCommand command = new ChangeDirectoryCommand();

        if(!expect("cd"))
            throw new Exception("ERROR: cd EXPECTED");

        //
        ParsedArgs parsed = consumeArgs();
        command.setFlags(parsed.flags());
        command.setArgs(parsed.args());

        if(parsed.args().isEmpty())
            throw new Exception("ERROR: directory EXPECTED: " + peek());

        return command;
    }
}