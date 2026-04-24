package org.plugins.Command;
import org.example.api.Command.BaseCommand;
import org.example.api.Command.Command;
import org.plugins.Handler.CatHandler;
import org.plugins.Handler.MoveHandler;
import org.plugins.Parser.CatParser;
import org.plugins.Parser.MoveParser;

@Command(
        name = "mv",
        parser  = MoveParser.class,
        handler = MoveHandler.class,
        description = "move files from [source] to [target]",
        usage = "mv [-r] [folder/file] [folder/file]")
public class MoveCommand extends BaseCommand {
}