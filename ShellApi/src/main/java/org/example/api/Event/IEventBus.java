package org.example.api.Event;

import java.util.function.Consumer;

public interface IEventBus {
    <T extends IEvent> void subscribe(
            Class<T> type,
            Consumer<T> listener);

    @SuppressWarnings("unchecked")
    <T extends IEvent> void publish(T event);

    <T extends IEvent> void unsubscribe(
            Class<T> type,
            Consumer<T> listener);
}
