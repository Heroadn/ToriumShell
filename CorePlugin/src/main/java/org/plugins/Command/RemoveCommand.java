package org.plugins.Command;

import org.example.api.Command.BaseCommand;
import org.example.api.Command.Command;
import org.plugins.Handler.RemoveHandler;
import org.plugins.Parser.RemoveParser;

@Command(
        name = "rm",
        parser  = RemoveParser.class,
        handler = RemoveHandler.class,
        description = "remove folder",
        usage = "rm [-p] [folder/file]")
public class RemoveCommand extends BaseCommand {
}