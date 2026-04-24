package org.example.core.Shell;

import org.example.api.Command.ICommand;
import org.example.api.Parser.AbstractParser;
import org.example.core.CommandRegistry;
import org.example.core.Exception.UnknownCommandException;
import org.example.api.Parser.IParser;import org.example.api.Parser.Token;

import java.util.ArrayDeque;
import java.util.List;

public class ShellParser extends AbstractParser {
    private final CommandRegistry registry;

    public ShellParser(CommandRegistry registry)
    {
        this.registry = registry;
    }

    public ICommand parse(List<Token> tokens) throws Exception
    {
        this.tokens = new ArrayDeque<>(tokens);
        String first = peek().value.toLowerCase();

        if(registry.has(first))
            return dispatch(tokens, first);

        //checking for a subcommand
        if (!isEmpty()) {
            String combined = first + "-" + peek().value;
            if (registry.has(combined)) {
                consume();
                return dispatch(tokens, combined);
            }
        }

        throw new UnknownCommandException(first);
    }

    private ICommand dispatch(
            List<Token> tokens,
            String first) throws Exception
    {
        IParser parser = this.registry.getParser(first).get();
        if (parser == null) throw new UnknownCommandException(first);
        return parser.parse(tokens);
    }

    @Override
    protected ICommand parse() throws Exception {
        return null;
    }

}