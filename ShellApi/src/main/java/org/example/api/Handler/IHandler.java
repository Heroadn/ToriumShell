package org.example.api.Handler;
import org.example.api.Command.ICommand;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;

public interface IHandler {
    void execute(
            ICommand command,
            IContext context,
            IConsole console) throws Exception;
}
