package org.plugins.Command;
import org.example.api.Command.BaseCommand;
import org.example.api.Command.Command;
import org.plugins.Handler.CatHandler;
import org.plugins.Handler.ClearHandler;
import org.plugins.Parser.CatParser;
import org.plugins.Parser.ClearParser;

@Command(
        name = "clear",
        parser  = ClearParser.class,
        handler = ClearHandler.class,
        description = "clear the console",
        usage = "clear")
public class ClearCommand extends BaseCommand {
}