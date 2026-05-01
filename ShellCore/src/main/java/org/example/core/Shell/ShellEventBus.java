package org.example.core.Shell;

import org.example.api.Event.Event;
import org.example.api.Event.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ShellEventBus implements EventBus {
    private final Map<Class<?>, List<Consumer<?>>> listeners = new HashMap<>();

    @Override
    public <T extends Event> void subscribe(
            Class<T> type,
            Consumer<T> listener)
    {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Event> void publish(T event) {
        List<Consumer<?>> list = listeners.get(event.getClass());
        if (list == null) return;
        for (Consumer<?> c : list) {
            ((Consumer<T>) c).accept(event);  // cast seguro — subscribe garante o tipo
        }
    }

    @Override
    public <T extends Event> void unsubscribe(
            Class<T> type,
            Consumer<T> listener)
    {
        List<Consumer<?>> list = listeners.get(type);
        if (list != null) list.remove(listener);
    }
}
