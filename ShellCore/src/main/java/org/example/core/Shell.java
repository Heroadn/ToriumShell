package org.example.core;

import org.example.api.Command.ICommand;
import org.example.api.Lexer.Lexer;
import org.example.core.Plugin.PluginContext;
import org.example.core.Plugin.PluginLoader;
import org.example.core.Runtime.Console;
import org.example.core.Runtime.Context;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Shell
{
    private final Scanner scanner;
    private final Lexer lexer;
    private final StringBuilder buffer;

    private final Context context;
    private final Console console;

    private final CommandRegistry commandRegistry;
    private final ShellHandler handler;
    private final ShellParser  parser;

    private final PluginContext pluginContext;
    private final PluginLoader loader;

    public Shell() throws IOException, URISyntaxException {
        scanner = new Scanner(System.in);
        lexer     = new Lexer();
        buffer = new StringBuilder();

        context = new Context();
        console = new Console();

        commandRegistry = new CommandRegistry();
        handler = new ShellHandler(context, console, commandRegistry);
        parser  = new ShellParser(commandRegistry);
        context.setCurrentDir(Path.of(System.getProperty("user.home")));
        context.setHome(Path.of(System.getProperty("user.home")));

        Path jarDir = Path.of(Main.class
                        .getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .toURI())
                .getParent();

        Path pluginsDir = jarDir.resolve("plugins");

        pluginContext = new PluginContext(
                commandRegistry,
                console,
                context);

        loader = new PluginLoader(
                pluginContext,
                pluginsDir);
        System.out.println(pluginsDir);
        loader.loadAll();
    }

    public void start() throws Exception
    {
        Terminal terminal = TerminalBuilder.builder().system(true).build();

        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .build();

        while(this.context.isRunning())
        {
            String line;

            try {
                line = reader.readLine(context.getCurrentDir() + " $ ");
            } catch (UserInterruptException e) {
                continue;
            } catch (EndOfFileException e) {
                break;
            }

            if (line == null || line.trim().isEmpty()) {
                continue;
            }

            try {
                appendLine(buffer, line);
                execute(buffer, lexer, handler, parser);
            } catch (Exception e) {
                console.error(e);
            } finally {
                buffer.setLength(0);
            }
        }
        /*
        while(context.isRunning()){
            console.print(context.getCurrentDir() + " $ ");
            String line = scanner.nextLine();
            appendLine(buffer, line);

            try {
                execute(buffer, lexer, handler, parser);
            } catch (Exception e) {
                console.error(e);
            } finally {
                buffer.setLength(0);
            }
        }

        scanner.close();*/
    }

    private static void appendLine(StringBuilder buffer, String line) {
        buffer.append(line);
    }

    private static void execute(
            StringBuilder buffer,
            Lexer lexer,
            ShellHandler handler,
            ShellParser parser) throws Exception {

        lexer.setInput(buffer.toString());
        ICommand command = parser.parse(lexer.tokenizer());
        handler.execute(command);
        buffer.setLength(0);
    }
}
