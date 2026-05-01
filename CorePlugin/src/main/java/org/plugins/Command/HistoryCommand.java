package org.plugins.Command;
import org.example.api.Command.BaseCommand;
import org.example.api.Command.Command;
import org.plugins.Handler.CatHandler;
import org.plugins.Handler.HistoryHandler;
import org.plugins.Parser.CatParser;
import org.plugins.Parser.HistoryParser;

@Command(
        name = "history",
        parser  = HistoryParser.class,
        handler = HistoryHandler.class,
        description = "",
        usage = "history        → lista todos\n" +
                "history -n 10  → últimos 10",
        manual = true)
public class HistoryCommand extends BaseCommand {
}