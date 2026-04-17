package org.example.api.Parser;

import java.util.List;

public record ParsedArgs(List<String> flags, List<String> args)
{
    public boolean hasFlag(String flag) {
        return flags.contains(flag);
    }
}
