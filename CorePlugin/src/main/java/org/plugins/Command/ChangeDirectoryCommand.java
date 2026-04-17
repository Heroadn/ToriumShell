package org.plugins.Command;

import org.example.api.Command.BaseCommand;
import org.example.api.Command.Command;
import org.plugins.Handler.ChangeDirectoryHandler;
import org.plugins.Parser.ChangeDirectoryParser;

@Command(
        name = "cd",
        parser  = ChangeDirectoryParser.class,
        handler = ChangeDirectoryHandler.class,
        description = "Change current directory of shell",
        usage = "cd [path]")
public class ChangeDirectoryCommand extends BaseCommand {
}