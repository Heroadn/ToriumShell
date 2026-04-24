package org.plugins.Command;
import org.example.api.Command.BaseCommand;
import org.example.api.Command.Command;
import org.plugins.Handler.HeadHandler;
import org.plugins.Handler.TailHandler;
import org.plugins.Parser.HeadParser;
import org.plugins.Parser.TailParser;

@Command(
        name = "tail",
        parser  = TailParser.class,
        handler = TailHandler.class,
        description = "Prints the last 10 lines of each file",
        usage = "tail [-n] [fileName1] [fileName...]")
public class TailCommand extends BaseCommand {
}