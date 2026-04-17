package org.plugins.Command;

import org.example.api.Command.BaseCommand;
import org.example.api.Command.Command;
import org.plugins.Handler.RemoveHandler;
import org.plugins.Parser.RemoveParser;

@Command(
        name = "mkdir",
        parser  = RemoveParser.class,
        handler = RemoveHandler.class,
        description = "make folders",
        usage = "mkdir [-p] [folder/file]")
public class MakeDirectoryCommand extends BaseCommand {
}