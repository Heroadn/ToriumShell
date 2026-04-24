package org.example.core.Shell;

import org.example.api.Command.ICommand;
import org.example.api.Lexer.Lexer;
import org.example.api.Runtime.IContext;
import org.example.api.Runtime.InteractiveLoop;
import org.example.api.Runtime.Mode;
import org.example.core.CommandRegistry;
import org.example.core.PromptPrinter;
import org.example.core.Runtime.Context;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;

import java.io.IOException;

public class ShellRunner {

    public void run(
            ShellEnvironment environment,
            ShellExecutor executor) throws IOException
    {
        var context = environment.context;
        var registry = environment.commandRegistry;
        var console = environment.console;

        LineReaderBuilder builder = LineReaderBuilder.builder()
                .terminal(environment.terminal)
                .parser(new DefaultParser())
                .completer(getCompleter(registry));

        LineReader reader = builder.build();
        reader.getKeyMaps().get(LineReader.MAIN)
                .bind(new Reference(LineReader.COMPLETE_WORD), "\t");

        while(context.isRunning())
        {
            String line = readLine(reader, context);

            if (line == null) break;
            if (line.isBlank()) continue;

            try {
                executor.execute(line);
            } catch (Exception e) {
                console.error(e);
            }

            if (isInteractiveMode(context)
                    && isInteractiveScreenNull(environment)) {
                try {
                    InteractiveLoop loop = new InteractiveLoop(
                            environment.pluginContext.getInteractiveScreen(),
                            console
                    );
                    loop.run();
                } finally {
                    context.setMode(Mode.NORMAL);
                    environment.pluginContext.setInteractiveScreen(null);
                }
            }
        }
    }

    private static boolean isInteractiveMode(Context context) {
        return context.getMode() == Mode.INTERACTIVE;
    }

    private static boolean isInteractiveScreenNull(ShellEnvironment environment) {
        return environment.pluginContext.getInteractiveScreen() != null;
    }

    private static Completer getCompleter(CommandRegistry registry) {
        return (reader, line, candidates) -> {
            if (line.wordIndex() == 0) {
                registry.getClasses().keySet()
                        .forEach(cmd -> candidates.add(new Candidate(cmd)));
            }
        };
    }

    private String readLine(LineReader reader, IContext context) {
        try {
            return reader.readLine(PromptPrinter.print(context));
        } catch (UserInterruptException e) {
            return "";
        } catch (EndOfFileException e) {
            return null;
        }
    }
}
