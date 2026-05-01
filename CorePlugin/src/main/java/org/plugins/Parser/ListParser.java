package org.plugins.Parser;

import org.example.api.Command.ICommand;
import org.example.api.Parser.IParser;
import org.example.api.Parser.Token;
import org.plugins.Command.ListCommand;
import org.example.api.Parser.BuildParser;

import java.util.List;

public class ListParser implements IParser {
    private final IParser delegate
            = BuildParser.of(ListCommand::new).build();

    @Override
    public ICommand parse(List<Token> tokens) throws Exception
    {
        return delegate.parse(tokens);
    }
}