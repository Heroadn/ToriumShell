package org.plugins.Parser;

import org.example.api.Parser.AbstractParser;
import org.example.api.Parser.ParsedArgs;
import org.plugins.Command.CopyCommand;
import org.plugins.Command.TouchCommand;

public class TouchParser extends AbstractParser {

    public TouchParser()
    {
        setAllowed("-r");
    }

    @Override
    public TouchCommand parse() throws Exception
    {
        var command = new TouchCommand();
        var parsed = consumeArgs(command);
        command.setArgs(parsed.args());

        if(command.getArgs().isEmpty())
            throw new Exception("ERROR: fileNames EXPECTED");

        return command;
    }
}