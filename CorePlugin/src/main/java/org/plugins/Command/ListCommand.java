package org.plugins.Command;
import org.example.api.Command.BaseCommand;
import org.example.api.Command.Command;
import org.plugins.Handler.ListHandler;
import org.plugins.Parser.ListParser;

@Command(
        name = "ls",
        parser  = ListParser.class,
        handler = ListHandler.class,
        description = "List files",
        usage = "ls [-a] [path]")
public class ListCommand extends BaseCommand {
}