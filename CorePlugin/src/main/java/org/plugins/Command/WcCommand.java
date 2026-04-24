package org.plugins.Command;
import org.example.api.Command.BaseCommand;
import org.example.api.Command.Command;
import org.plugins.Handler.TouchHandler;
import org.plugins.Handler.WcHandler;
import org.plugins.Parser.TouchParser;
import org.plugins.Parser.WcParser;

@Command(
        name = "wc",
        parser  = WcParser.class,
        handler = WcHandler.class,
        description = "counts lines, words and chars",
        usage = "wc [-l/-w/-c] filename")
public class WcCommand extends BaseCommand {
}