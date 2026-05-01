package org.example.api.Event;

import org.example.api.Command.ICommand;

public record CommandExecuted(ICommand command, int exitCode) implements Event {
}
