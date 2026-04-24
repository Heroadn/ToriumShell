package org.plugins.Parser;

import org.example.api.Parser.AbstractParser;
import org.example.api.Parser.ParsedArgs;
import org.plugins.Command.GrepCommand;
import org.plugins.Command.WcCommand;

public class GrepParser extends AbstractParser {

    public GrepParser()
    {
        this.allowedFlags.add("-i");
        this.allowedFlags.add("-n");
        this.allowedFlags.add("-v");
        this.allowedFlags.add("-c");
    }

    @Override
    public GrepCommand parse() throws Exception
    {
        GrepCommand command = new GrepCommand();
        consume();

        ParsedArgs parsed = consumeArgs();
        command.setFlags(parsed.flags());
        command.setArgs(parsed.args());

        if(command.getArgs().size() < 2)
            throw new Exception("ERROR: at least two arguments EXPECTED");

        return command;
    }
}