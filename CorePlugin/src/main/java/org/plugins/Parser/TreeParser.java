package org.plugins.Parser;

import org.example.api.Parser.AbstractParser;
import org.example.api.Parser.ParsedArgs;
import org.plugins.Command.TreeCommand;

public class TreeParser extends AbstractParser {
    //TODO: -n -> fix tree print
    @Override
    public TreeCommand parse() throws Exception
    {
        TreeCommand command = new TreeCommand();

        if(!expect("tree"))
            throw new Exception("ERROR: echo EXPECTED");

        ParsedArgs parsed = consumeArgs();
        command.setFlags(parsed.flags());
        command.setArgs(parsed.args());

        if(command.has("-n") && !command.getArgs().isEmpty())
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