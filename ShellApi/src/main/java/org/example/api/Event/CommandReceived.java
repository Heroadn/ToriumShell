package org.example.api.Event;

import org.example.api.Command.ICommand;

public record CommandReceived(String raw) implements Event {
}
