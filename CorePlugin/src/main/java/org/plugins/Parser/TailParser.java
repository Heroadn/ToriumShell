package org.plugins.Parser;

import org.example.api.Command.ICommand;
import org.example.api.Parser.*;
import org.plugins.Command.TailCommand;

import java.util.List;

public class TailParser implements IParser
{
    private final IParser delegate
            = BuildParser
            .of(TailCommand::new)
            .valued("-n")
            .validateFlag(
                    "-n",
                    Validators::isInt,
                    "ERROR: argument is not a number")
            .minArgs(1, "ERROR: fileName EXPECTED")
            .build();

    @Override
    public ICommand parse(List<Token> tokens) throws Exception
    {
        return delegate.parse(tokens);
    }
}