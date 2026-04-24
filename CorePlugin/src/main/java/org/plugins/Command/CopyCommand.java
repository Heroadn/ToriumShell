package org.plugins.Command;
import org.example.api.Command.BaseCommand;
import org.example.api.Command.Command;
import org.plugins.Handler.CopyHandler;
import org.plugins.Handler.TailHandler;
import org.plugins.Parser.CopyParser;
import org.plugins.Parser.TailParser;

@Command(
        name = "cp",
        parser  = CopyParser.class,
        handler = CopyHandler.class,
        description = "copy files to target path",
        usage = "cp [source] [target]")
public class CopyCommand extends BaseCommand {
}