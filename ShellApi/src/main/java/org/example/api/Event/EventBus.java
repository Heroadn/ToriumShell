package org.example.api.Event;

import java.util.function.Consumer;

public interface EventBus {
    <T extends Event> void subscribe(
            Class<T> type,
            Consumer<T> listener);

    @SuppressWarnings("unchecked")
    <T extends Event> void publish(T event);

    <T extends Event> void unsubscribe(
            Class<T> type,
            Consumer<T> listener);
}
