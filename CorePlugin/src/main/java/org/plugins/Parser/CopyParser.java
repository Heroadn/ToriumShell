package org.plugins.Parser;

import org.example.api.Parser.AbstractParser;
import org.example.api.Parser.ParsedArgs;
import org.plugins.Command.CopyCommand;
import org.plugins.Command.TailCommand;

public class CopyParser extends AbstractParser {

    public CopyParser()
    {
        this.allowedFlags.add("-r");
    }

    @Override
    public CopyCommand parse() throws Exception
    {
        CopyCommand command = new CopyCommand();
        consume();

        ParsedArgs parsed = consumeArgs();
        command.setFlags(parsed.flags());
        command.setArgs(parsed.args());

        if(command.getArgs().size() < 2)
            throw new Exception("ERROR: fileNames EXPECTED: " + command.getArgs().size());

        return command;
    }
}