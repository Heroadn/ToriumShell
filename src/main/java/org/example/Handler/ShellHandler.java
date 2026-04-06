package org.example.Handler;

import org.example.Command.Command;
import org.example.ShellContext;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShellHandler {
    private final ShellContext context;
    private final Map<String, Handler> handlers = new LinkedHashMap<>();

    public ShellHandler(ShellContext context) {
        this.context = context;

        handlers.put("cd", new ChangeDirectoryHandler());
        handlers.put("ls", new ListHandler());
        handlers.put("rm", new RemoveHandler());
        handlers.put("mkdir", new MakeDirectoryHandler());
    }

    public void execute(Command command) throws Exception {
        String type = command.getType();
        Handler handler = handlers.get(type);

        if (handler == null) {
            throw new Exception("ERROR: UNKNOWN COMMAND " + type);
        }

        handler.execute(command, context);
    }
}
