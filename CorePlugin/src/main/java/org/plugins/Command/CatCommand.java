package org.plugins.Command;
import org.example.api.Command.BaseCommand;
import org.example.api.Command.Command;
import org.plugins.Handler.CatHandler;
import org.plugins.Handler.PwdHandler;
import org.plugins.Parser.CatParser;
import org.plugins.Parser.PwdParser;

@Command(
        name = "cat",
        parser  = CatParser.class,
        handler = CatHandler.class,
        description = "Print contents of a file",
        usage = "cat")
public class CatCommand extends BaseCommand {
}