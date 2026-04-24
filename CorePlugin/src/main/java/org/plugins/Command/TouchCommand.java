package org.plugins.Command;
import org.example.api.Command.BaseCommand;
import org.example.api.Command.Command;
import org.plugins.Handler.CopyHandler;
import org.plugins.Handler.TouchHandler;
import org.plugins.Parser.CopyParser;
import org.plugins.Parser.TouchParser;

@Command(
        name = "touch",
        parser  = TouchParser.class,
        handler = TouchHandler.class,
        description = "create file or update TimeStamp",
        usage = "touch [filename1] [filename....]")
public class TouchCommand extends BaseCommand {
}