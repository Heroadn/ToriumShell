package org.example.api.Parser;
import org.example.api.Command.ICommand;

import java.util.*;

public abstract class AbstractParser extends BaseParser implements IParser{
    private final Set<String> allowedFlags = new HashSet<>();
    private final Set<String> valuedFlags  = new HashSet<>();

    public ICommand parse(List<Token> tokens) throws Exception {
        setTokens(tokens);
        return parse();
    }

    protected abstract ICommand parse() throws Exception;

    public ParsedArgs consumeArgs(ICommand cmd) throws Exception {
        List<String> args = new ArrayList<>();

        while (!isEmpty()) {
            Token t = peek();
            if (isEndOfFileToken(t)) break;
            String value = consume().value();

            //flags and args
            if (!isFlagPrefix(value)) {
                args.add(value);
                continue;
            }

            if (!isFlagAllowed(value))
                throw new Exception("ERROR: flag not allowed: " + value);

            boolean hasNextValue = !isEmpty() && !isEndOfFileToken(peek());
            boolean expectsValue = valuedFlags.contains(value);

            if (expectsValue && hasNextValue)
                cmd.put(value, consume().value());
            else
                cmd.put(value, "");
        }

        return new ParsedArgs(args);
    }

    private boolean isEndOfFileToken(Token t) {
        return t.key() == TokenType.EOF;
    }

    private boolean isFlagPrefix(String value) {
        return value.startsWith("-");
    }

    public boolean isFlagAllowed(String flag)
    {
        return allowedFlags.contains(flag);
    }

    public int size()
    {
        return this.tokens.size();
    }
    
    public void setAllowed(String... flags) {
        allowedFlags.addAll(List.of(flags));
    }

    public void setValued(String... flags) {
        valuedFlags.addAll(List.of(flags));
        allowedFlags.addAll(List.of(flags)); // valued já implica allowed
    }
}
