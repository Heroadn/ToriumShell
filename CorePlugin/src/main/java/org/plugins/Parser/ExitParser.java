package org.plugins.Parser;

import org.example.api.Parser.AbstractParser;
import org.plugins.Command.ExitCommand;

public class ExitParser extends AbstractParser {

    @Override
    public ExitCommand parse() throws Exception
    {
        ExitCommand command = new ExitCommand();

        if(!expect("exit"))
            throw new Exception("ERROR: exit EXPECTED");

        return command;
    }
}