package org.plugins.Parser;

import org.example.api.Command.ICommand;
import org.example.api.Parser.BuildParser;
import org.example.api.Parser.IParser;
import org.example.api.Parser.Token;
import org.example.api.Parser.Validators;
import org.plugins.Command.CatCommand;
import org.plugins.Command.HistoryCommand;

import java.util.List;

public class HistoryParser implements IParser
{
    private final IParser delegate
            = BuildParser
            .of(HistoryCommand::new)
            .valued("-n")
            .validateFlag(
                    "-n",
                    Validators::isInt,
                    "ERROR: valor nao é um inteiro")
            .build();

    @Override
    public ICommand parse(List<Token> tokens) throws Exception
    {
        return delegate.parse(tokens);
    }
}