package org.plugins.Command;

import org.example.api.Command.Command;
import org.plugins.Handler.ExitHandler;
import org.plugins.Parser.ExitParser;

@Command(
        name = "exit",
        parser  = ExitParser.class,
        handler = ExitHandler.class,
        description = "closes the shell",
        usage = "exit")
public class ExitCommand extends org.example.api.Command.BaseCommand{
}