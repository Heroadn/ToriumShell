package org.plugins.Handler;

import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;

public class PwdHandler implements IHandler {

    @Override
    public void execute(ICommand command, IContext context, IConsole console) throws Exception {
        console.println(context.getCurrentDir().toString());
    }
}
