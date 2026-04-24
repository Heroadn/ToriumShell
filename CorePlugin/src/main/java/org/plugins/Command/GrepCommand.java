package org.plugins.Command;
import org.example.api.Command.BaseCommand;
import org.example.api.Command.Command;
import org.plugins.Handler.GrepHandler;
import org.plugins.Handler.WcHandler;
import org.plugins.Parser.GrepParser;
import org.plugins.Parser.WcParser;

@Command(
        name = "grep",
        parser  = GrepParser.class,
        handler = GrepHandler.class,
        description = "search for a line with [string] in file",
        usage = "grep [-i/-n/-v/-c] [string] [filename]")
public class GrepCommand extends BaseCommand {
}