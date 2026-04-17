package org.plugins.Parser;

import org.example.api.Parser.AbstractParser;
import org.example.api.Parser.ParsedArgs;
import org.plugins.Command.MakeDirectoryCommand;

public class MakeDirectoryParser extends AbstractParser {
    public MakeDirectoryParser()
    {
        allowedFlags.add("-p");
    }

    @Override
    public MakeDirectoryCommand parse() throws Exception
    {
        MakeDirectoryCommand command = new MakeDirectoryCommand();

        if(!expect("mkdir"))
            throw new Exception("ERROR: mkdir EXPECTED");

        //
        ParsedArgs parsed = consumeArgs();
        command.setFlags(parsed.flags());
        command.setArgs(parsed.args());

        if(parsed.args().isEmpty())
            throw new Exception("ERROR: name of the file EXPECTED");

        return command;
    }
}