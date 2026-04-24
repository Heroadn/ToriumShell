package org.plugins.Parser;

import org.example.api.Parser.AbstractParser;
import org.example.api.Parser.ParsedArgs;
import org.plugins.Command.ChangeDirectoryCommand;

import java.util.List;

public class ChangeDirectoryParser extends AbstractParser {

    public ChangeDirectoryCommand parse() throws Exception
    {
        ChangeDirectoryCommand command = new ChangeDirectoryCommand();

        if(!expect("cd"))
            throw new Exception("ERROR: cd EXPECTED");

        //
        ParsedArgs parsed = consumeArgs();
        command.setFlags(parsed.flags());
        command.setArgs(parsed.args());

        return command;
    }
}