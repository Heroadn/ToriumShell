package org.example.api.Event;

import org.example.api.Command.ICommand;

public record CommandFailed(ICommand command, Exception cause) implements Event {
}
