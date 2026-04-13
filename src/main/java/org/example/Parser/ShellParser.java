package org.example.Parser;

import org.example.Command.Command;

public class ShellParser extends Parser{

    public ShellParser() {}

    public Command parse() throws Exception
    {
        String first = peek().value.toLowerCase();
        switch(first)
        {
            case "mkdir" -> {
                return new MakeDirectoryParser(this.getTokens()).parse();
            }
            case "cd" ->
            {
                return new ChangeDirectoryParser(this.getTokens()).parse();
            }
            case "ls" ->
            {
                return new ListParser(this.getTokens()).parse();
            }
            case "rm" -> {
                return new RemoveParser(this.getTokens()).parse();
            }
            default -> throw new Exception("ERROR: UNKNOWN COMMAND " + first);
        }

    }

}