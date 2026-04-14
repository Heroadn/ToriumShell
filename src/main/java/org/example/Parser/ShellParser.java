package org.example.Parser;

import org.example.Command.Command;
import org.example.Exception.UnknownCommandException;

import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.Map;

public class ShellParser extends Parser{
    private final Map<String, Parser> parsers = new LinkedHashMap<>();

    public ShellParser(Map<String, Parser> parsers)
    {
        this.parsers.putAll(parsers);
    }

    @Override
    public Command parse() throws Exception
    {
        String first = peek().value.toLowerCase();
        Parser parser = parsers.get(first);

        if (parser == null) {
            throw new UnknownCommandException(first);
        }

        parser.reset(this.getTokens());
        return parser.parse();
    }

}