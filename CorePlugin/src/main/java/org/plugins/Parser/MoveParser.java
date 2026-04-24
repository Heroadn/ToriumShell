package org.plugins.Parser;

import org.example.api.Parser.AbstractParser;
import org.example.api.Parser.ParsedArgs;
import org.plugins.Command.CatCommand;
import org.plugins.Command.CopyCommand;
import org.plugins.Command.MoveCommand;

public class MoveParser extends AbstractParser {

    public MoveParser()
    {
        this.allowedFlags.add("-r");
    }

    @Override
    public MoveCommand parse() throws Exception
    {
        MoveCommand command = new MoveCommand();
        consume();

        ParsedArgs parsed = consumeArgs();
        command.setFlags(parsed.flags());
        command.setArgs(parsed.args());

        if(command.getArgs().size() < 2)
            throw new Exception("ERROR: fileNames EXPECTED: " + command.getArgs().size());

        return command;
    }
}