package org.plugins.Parser;

import org.example.api.Command.ICommand;
import org.example.api.Parser.IParser;
import org.example.api.Parser.Token;
import org.plugins.Command.ClearCommand;
import org.example.api.Parser.BuildParser;

import java.util.List;

public class ClearParser implements IParser {
    private final IParser delegate
            = BuildParser.of(ClearCommand::new).build();

    @Override
    public ICommand parse(List<Token> tokens) throws Exception {
        return delegate.parse(tokens);
    }
}