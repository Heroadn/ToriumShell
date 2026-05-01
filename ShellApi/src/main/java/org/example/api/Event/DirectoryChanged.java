package org.example.api.Event;

import java.nio.file.Path;

public record DirectoryChanged(Path from, Path to) implements Event {
}
