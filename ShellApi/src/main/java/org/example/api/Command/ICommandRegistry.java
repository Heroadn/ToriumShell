package org.example.api.Command;
import org.example.api.Handler.IHandler;
import org.example.api.Parser.IParser;

import java.util.function.Supplier;

public interface ICommandRegistry {
    public void register(
            Class<? extends ICommand> command,
            Supplier<IParser> parser,
            Supplier<IHandler> handler);
}
