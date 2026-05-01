package org.plugins.Parser;

import org.example.api.Parser.AbstractParser;
import org.example.api.Parser.ParsedArgs;
import org.plugins.Command.TreeCommand;

public class TreeParser extends AbstractParser {

    public TreeParser()
    {
        setAllowed("-d");
    }

    @Override
    public TreeCommand parse() throws Exception
    {
        var command = new TreeCommand();
        var parsed = consumeArgs(command);
        command.setArgs(parsed.args());


        return command;
    }
}