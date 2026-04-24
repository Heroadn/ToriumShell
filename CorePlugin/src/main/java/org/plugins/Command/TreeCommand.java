package org.plugins.Command;
import org.example.api.Command.BaseCommand;
import org.example.api.Command.Command;
import org.plugins.Handler.TreeHandler;
import org.plugins.Parser.TreeParser;

@Command(
        name = "tree",
        parser  = TreeParser.class,
        handler = TreeHandler.class,
        description = "List directory structure as a tree",
        usage = "tree [path] [-L depth]")
public class TreeCommand extends BaseCommand {
}