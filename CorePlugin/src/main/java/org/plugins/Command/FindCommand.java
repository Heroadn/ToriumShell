package org.plugins.Command;
import org.example.api.Command.BaseCommand;
import org.example.api.Command.Command;
import org.plugins.Handler.FindHandler;
import org.plugins.Handler.WcHandler;
import org.plugins.Parser.FindParser;
import org.plugins.Parser.WcParser;

@Command(
        name = "find",
        parser  = FindParser.class,
        handler = FindHandler.class,
        description = "find files and dirs",
        usage = "find [-t file|dir] [-d N]")
public class FindCommand extends BaseCommand {
}