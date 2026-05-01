package org.plugins.Command;
import org.example.api.Command.BaseCommand;
import org.example.api.Command.Command;
import org.plugins.Handler.ClearHandler;
import org.plugins.Handler.DateHandler;
import org.plugins.Parser.ClearParser;
import org.plugins.Parser.DateParser;

@Command(
        name = "date",
        parser  = DateParser.class,
        handler = DateHandler.class,
        description = "",
        usage = "date              → 2026-04-30 14:35:22\n" +
                "date -f HH:mm     → 14:35\n" +
                "date -f dd/MM/yyyy → 30/04/2026")
public class DateCommand extends BaseCommand
{

}