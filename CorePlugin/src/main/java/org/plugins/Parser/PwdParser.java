package org.plugins.Parser;

import org.example.api.Parser.AbstractParser;
import org.example.api.Parser.ParsedArgs;
import org.plugins.Command.EchoCommand;
import org.plugins.Command.PwdCommand;

public class PwdParser extends AbstractParser {

    @Override
    public PwdCommand parse() throws Exception
    {
        PwdCommand command = new PwdCommand();

        if(!expect("pwd"))
            throw new Exception("ERROR: echo EXPECTED");

        ParsedArgs parsed = consumeArgs();
        command.setFlags(parsed.flags());
        command.setArgs(parsed.args());

        return command;
    }
}