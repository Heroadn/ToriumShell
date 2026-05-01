package org.plugins.Parser;

import org.example.api.Command.ICommand;
import org.example.api.Parser.*;
import org.plugins.Command.CatCommand;

import java.util.List;

public class CatParser implements IParser
{
    private final IParser delegate
            = BuildParser
            .of(CatCommand::new)
            .allowed("-n")
            .minArgs(1, "ERROR: fileName EXPECTED")
            .build();

    @Override
    public ICommand parse(List<Token> tokens) throws Exception
    {
        return delegate.parse(tokens);
    }
}