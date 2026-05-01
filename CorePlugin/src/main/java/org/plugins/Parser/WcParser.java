package org.plugins.Parser;

import org.example.api.Parser.AbstractParser;
import org.example.api.Parser.ParsedArgs;
import org.plugins.Command.TouchCommand;
import org.plugins.Command.WcCommand;

public class WcParser extends AbstractParser {

    public WcParser()
    {
        setAllowed("-l", "-w", "-c");
    }

    @Override
    public WcCommand parse() throws Exception
    {
        var command = new WcCommand();
        var parsed = consumeArgs(command);
        command.setArgs(parsed.args());

        if(command.getArgs().isEmpty())
            throw new Exception("ERROR: fileName EXPECTED");

        return command;
    }
}