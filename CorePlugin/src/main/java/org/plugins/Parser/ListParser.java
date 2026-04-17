package org.plugins.Parser;

import org.example.api.Parser.AbstractParser;
import org.example.api.Parser.ParsedArgs;
import org.plugins.Command.ListCommand;

public class ListParser extends AbstractParser {

    @Override
    public ListCommand parse() throws Exception
    {
        ListCommand command = new ListCommand();

        if(!expect("ls"))
            throw new Exception("ERROR: ls EXPECTED");

        ParsedArgs parsed = consumeArgs();
        command.setFlags(parsed.flags());
        command.setArgs(parsed.args());

        return command;
    }
}