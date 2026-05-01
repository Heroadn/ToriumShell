package org.plugins.Parser;

import org.example.api.Command.ICommand;
import org.example.api.Parser.IParser;
import org.example.api.Parser.Token;
import org.plugins.Command.MakeDirectoryCommand;
import org.example.api.Parser.BuildParser;

import java.util.List;

public class MakeDirectoryParser implements IParser
{
    private final IParser delegate
            = BuildParser
            .of(MakeDirectoryCommand::new)
            .allowed("-p")
            .minArgs(1, "ERROR: fileName EXPECTED")
            .build();

    @Override
    public ICommand parse(List<Token> tokens) throws Exception
    {
        return delegate.parse(tokens);
    }
}