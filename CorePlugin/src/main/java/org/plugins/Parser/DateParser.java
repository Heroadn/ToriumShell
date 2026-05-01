package org.plugins.Parser;

import org.example.api.Command.ICommand;
import org.example.api.Parser.BuildParser;
import org.example.api.Parser.IParser;
import org.example.api.Parser.Token;
import org.plugins.Command.DateCommand;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class DateParser implements IParser {
    private final IParser delegate
            = BuildParser.of(DateCommand::new)
            .valued("-f")
            .validateFlag(
                    "-f",
                    DateParser::isValidFormat, "ERROR: formato inválido")
            .build();

    private static boolean isValidFormat(String pattern) {
        try {
            DateTimeFormatter.ofPattern(pattern);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public ICommand parse(List<Token> tokens) throws Exception {
        return delegate.parse(tokens);
    }
}