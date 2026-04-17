package org.plugins.Handler;

import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;

//@Command(name = "echo", description = "Echo [arg1] [arg...]")
public class EchoHandler implements IHandler {

    @Override
    public void execute(ICommand command, IContext context, IConsole console) throws Exception {
        console.println(String.join(" ", command.getArgs()));
    }
}
