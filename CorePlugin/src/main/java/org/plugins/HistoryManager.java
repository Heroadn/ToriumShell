package org.plugins;

import org.example.api.Command.ICommand;
import org.example.api.Event.CommandExecuted;
import org.example.api.Event.CommandReceived;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class HistoryManager
{
    public record HistoryEntry(int index, String raw, Instant timestamp){};
    private final List<HistoryEntry> entries = new ArrayList<>();

    public void record(CommandReceived event)
    {
        String raw = event.raw();
        entries.add(new HistoryEntry(entries.size() + 1, raw, Instant.now()));
    }

    public List<HistoryEntry> getAll()
    {
        return new ArrayList<>(this.entries);
    }

    public List<HistoryEntry> getLast(int n)
    {
        List<HistoryEntry> result = new ArrayList<>();

        int size = this.entries.size();
        int num = Math.min(n, size);
        for (int i = (size - num); i < size; i++) result.add(this.entries.get(i));

        return result;
    }
}
