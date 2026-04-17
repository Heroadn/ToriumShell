package org.plugins.Parser;

import org.example.api.Parser.AbstractParser;
import org.example.api.Parser.ParsedArgs;
import org.plugins.Command.RemoveCommand;

public class RemoveParser extends AbstractParser {

    public RemoveParser()
    {
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