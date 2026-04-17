package org.plugins.Command;
import org.example.api.Command.BaseCommand;
import org.example.api.Command.Command;
import org.plugins.Handler.EchoHandler;
import org.plugins.Parser.EchoParser;

@Command(
        name = "echo",
        parser  = EchoParser.class,
        handler = EchoHandler.class,
        description = "Echo arguments",
        usage = "echo [arg1] [arg...]")
public class EchoCommand extends BaseCommand {
}