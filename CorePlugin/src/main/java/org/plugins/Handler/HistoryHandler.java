package org.plugins.Handler;

import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;
import org.plugins.HistoryManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistoryHandler implements IHandler {
    private final HistoryManager historyManager;
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");

    public HistoryHandler(HistoryManager historyManager)
    {
        this.historyManager = historyManager;
    }

    @Override
    public int execute(ICommand command, IContext context, IConsole console) throws Exception {

        if(command.has("-n"))
        {
            var list = historyManager.getLast(
                    Integer.parseInt(command.getValue("-n")));

            printHistoryList(console, list);
            return 0;
        }

        printHistoryList(console, historyManager.getAll());
        return 0;
    }

    private void printHistoryList(IConsole console, List<HistoryManager.HistoryEntry> list) {
        for (HistoryManager.HistoryEntry entry : list)
        {
            String time = LocalDateTime
                    .ofInstant(entry.timestamp(), ZoneId.systemDefault())
                    .format(fmt);

            console.println(String.format("%3d  %-40s %s",
                    entry.index(),
                    entry.raw(),
                    time));
        }
    }
}
