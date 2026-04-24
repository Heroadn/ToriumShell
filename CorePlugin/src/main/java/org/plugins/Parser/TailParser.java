package org.plugins.Parser;

import org.example.api.Parser.AbstractParser;
import org.example.api.Parser.ParsedArgs;
import org.plugins.Command.TailCommand;

public class TailParser extends AbstractParser {

    public TailParser()
    {
        this.allowedFlags.add("-n");
    }

    @Override
    public TailCommand parse() throws Exception
    {
        TailCommand command = new TailCommand();
        consume(); //tail

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