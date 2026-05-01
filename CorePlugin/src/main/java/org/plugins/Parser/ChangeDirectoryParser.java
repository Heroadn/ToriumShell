package org.plugins.Parser;

import org.example.api.Parser.AbstractParser;
import org.example.api.Parser.ParsedArgs;
import org.plugins.Command.ChangeDirectoryCommand;

import java.util.List;

public class ChangeDirectoryParser extends AbstractParser {

    public ChangeDirectoryCommand parse() throws Exception
    {
        var command = new ChangeDirectoryCommand();
        var parsed = consumeArgs(command);
        command.setArgs(parsed.args());

        return command;
    }
}