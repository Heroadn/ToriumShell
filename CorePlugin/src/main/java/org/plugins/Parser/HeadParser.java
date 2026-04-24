package org.plugins.Parser;

import org.example.api.Parser.AbstractParser;
import org.example.api.Parser.ParsedArgs;
import org.plugins.Command.HeadCommand;

public class HeadParser extends AbstractParser {

    public HeadParser()
    {
        this.allowedFlags.add("-n");
    }

    @Override
    public HeadCommand parse() throws Exception
    {
        HeadCommand command = new HeadCommand();

        if(!expect("head"))
            throw new Exception("ERROR: head EXPECTED");

        ParsedArgs parsed = consumeArgs();
        command.setFlags(parsed.flags());
        command.setArgs(parsed.args());

        if(command.getArgs().isEmpty())
            throw new Exception("ERROR: fileName / number of lines EXPECTED");

        if(command.has("-n"))
        {
            String input = command.getArgs().getFirst();

            try {
                Integer.parseInt(input);
            } catch (NumberFormatException e) {
                throw new Exception("ERROR: argument is not a number " + input);
            }
        }

        return command;
    }
}