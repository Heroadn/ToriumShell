package org.plugins.Parser;

import org.example.api.Command.ICommand;
import org.example.api.Parser.*;
import org.plugins.Command.FindCommand;
import java.util.List;
import org.example.api.Parser.BuildParser;

public class FindParser implements IParser {
    private final IParser delegate
            = BuildParser
            .of(FindCommand::new)
            .valued("-t", "-d")
            .validateFlag(
                    "-t",
                    FindParser::isValidType,
                    "ERROR: file or dir EXPECTED after -t.")
            .minArgs(0, "")
            .build();

    private static boolean isValidType(String type) {
        return (type.equalsIgnoreCase("file")
                || type.equalsIgnoreCase("dir"));
    }

    @Override
    public ICommand parse(List<Token> tokens) throws Exception
    {
        return delegate.parse(tokens);
    }
}