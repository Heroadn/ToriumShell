package org.plugins.Command;
import org.example.api.Command.BaseCommand;
import org.example.api.Command.Command;
import org.plugins.Handler.HeadHandler;
import org.plugins.Handler.PwdHandler;
import org.plugins.Parser.HeadParser;
import org.plugins.Parser.PwdParser;

@Command(
        name = "head",
        parser  = HeadParser.class,
        handler = HeadHandler.class,
        description = "Prints the first 10 lines of each file",
        usage = "head [-n] [fileName1] [fileName...]")
public class HeadCommand extends BaseCommand {
}