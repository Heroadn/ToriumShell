package org.plugins.Parser;

import org.example.api.Command.ICommand;
import org.example.api.Parser.*;
import org.plugins.Command.PwdCommand;

import java.util.List;

public class PwdParser implements IParser {
    private final IParser delegate
            = BuildParser.of(PwdCommand::new).build();

    @Override
    public ICommand parse(List<Token> tokens) throws Exception
    {
        return delegate.parse(tokens);
    }
}