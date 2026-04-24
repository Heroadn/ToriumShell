package org.plugins.Parser;

import org.example.api.Parser.AbstractParser;
import org.example.api.Parser.ParsedArgs;
import org.plugins.Command.CatCommand;
import org.plugins.Command.PwdCommand;

public class CatParser extends AbstractParser {

    public CatParser()
    {
        this.allowedFlags.add("-n");
    }

    @Override
    public CatCommand parse() throws Exception
    {
        CatCommand command = new CatCommand();

        if(!expect("cat"))
            throw new Exception("ERROR: cat EXPECTED");

        ParsedArgs parsed = consumeArgs();
        command.setFlags(parsed.flags());
        command.setArgs(parsed.args());

        if(command.getArgs().isEmpty())
            throw new Exception("ERROR: fileName EXPECTED");

        return command;
    }
}