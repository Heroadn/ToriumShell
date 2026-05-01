package org.plugins.Parser;

import org.example.api.Command.ICommand;
import org.example.api.Parser.*;
import org.plugins.Command.CopyCommand;

import java.util.List;

public class CopyParser implements IParser
{
    private final IParser delegate
            = BuildParser
            .of(CopyCommand::new)
            .allowed("-r")
            .minArgs(2, "ERROR: fileNames EXPECTED")
            .build();

    @Override
    public ICommand parse(List<Token> tokens) throws Exception
    {
        return delegate.parse(tokens);
    }
}