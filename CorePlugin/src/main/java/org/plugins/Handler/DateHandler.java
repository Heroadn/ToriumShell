package org.plugins.Handler;

import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;
import org.example.api.Runtime.IConsole;
import org.example.api.Runtime.IContext;

import javax.swing.text.DateFormatter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class DateHandler implements IHandler {

    @Override
    public int execute(ICommand command, IContext context, IConsole console) throws Exception
    {
        String pattern = command.has("-f")
                ? command.getValue("-f")
                : "dd-MM-yyyy HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        LocalDateTime time = context.getRuntime().getLocalTime();
        console.println(time.format(formatter));
        return 0;
    }
}
