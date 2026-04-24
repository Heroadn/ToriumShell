package org.plugins.Command;
import org.example.api.Command.BaseCommand;
import org.example.api.Command.Command;
import org.plugins.Handler.EchoHandler;
import org.plugins.Handler.PwdHandler;
import org.plugins.Parser.EchoParser;
import org.plugins.Parser.PwdParser;

@Command(
        name = "pwd",
        parser  = PwdParser.class,
        handler = PwdHandler.class,
        description = "Print current directory",
        usage = "pwd")
public class PwdCommand extends BaseCommand {
}