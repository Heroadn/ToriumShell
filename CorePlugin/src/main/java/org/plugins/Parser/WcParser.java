package org.plugins.Parser;

import org.example.api.Parser.AbstractParser;
import org.example.api.Parser.ParsedArgs;
import org.plugins.Command.TouchCommand;
import org.plugins.Command.WcCommand;

public class WcParser extends AbstractParser {

    public WcParser()
    {
        this.allowedFlags.add("-l");
        this.allowedFlags.add("-w");
        this.allowedFlags.add("-c");
    }

    @Override
    public WcCommand parse() throws Exception
    {
        WcCommand command = new WcCommand();
        consume();

        ParsedArgs parsed = consumeArgs();
        command.setFlags(parsed.flags());
        command.setArgs(parsed.args());

        if(command.getArgs().isEmpty())
            throw new Exception("ERROR: fileName EXPECTED");

        return command;
    }
}