package org.plugins.Parser;

import org.example.api.Command.ICommand;
import org.example.api.Parser.BuildParser;
import org.example.api.Parser.IParser;
import org.example.api.Parser.Token;
import org.plugins.Command.RemoveCommand;
import java.util.List;

public class RemoveParser implements IParser
{
    private final IParser delegate
            = BuildParser
            .of(RemoveCommand::new)
            .allowed("-r")
            .minArgs(1, "ERROR: fileName EXPECTED")
            .build();

    @Override
    public ICommand parse(List<Token> tokens) throws Exception
    {
        return delegate.parse(tokens);
    }
}