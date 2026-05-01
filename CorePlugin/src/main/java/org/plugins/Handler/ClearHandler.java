package org.plugins.Handler;

import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class ClearHandler implements IHandler {

    @Override
    public int execute(ICommand command, IContext context, IConsole console) throws Exception {
        console.clear();
        return 0;
    }
}
