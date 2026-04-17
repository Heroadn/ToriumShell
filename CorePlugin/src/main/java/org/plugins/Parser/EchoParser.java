package org.plugins.Parser;

import org.example.api.Parser.AbstractParser;
import org.example.api.Parser.ParsedArgs;
import org.plugins.Command.EchoCommand;

public class EchoParser extends AbstractParser {

    @Override
    public EchoCommand parse() throws Exception
    {
        EchoCommand command = new EchoCommand();

        if(!expect("echo"))
            throw new Exception("ERROR: echo EXPECTED");

        ParsedArgs parsed = consumeArgs();
        command.setFlags(parsed.flags());
        command.setArgs(parsed.args());

        return command;
    }
}