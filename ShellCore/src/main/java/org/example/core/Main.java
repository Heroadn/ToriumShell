package org.example.core;

import org.example.api.Lexer.Lexer;
import org.example.core.Plugin.PluginContext;
import org.example.core.Plugin.PluginLoader;
import org.example.core.Runtime.Console;
import org.example.core.Runtime.Context;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    //TODO: command anotation, it can get its name
    public static void main(String[] args) throws URISyntaxException, IOException {
        Scanner scanner = new Scanner(System.in);
        Lexer lexer = new Lexer();
        StringBuilder buffer = new StringBuilder();

        Context context = new Context();
        Console console = new Console();
        CommandRegistry commandRegistry = new CommandRegistry();

        ShellHandler handler = new ShellHandler(context, console, commandRegistry);
        ShellParser  parser = new ShellParser(commandRegistry);
        context.setCurrentDir(Path.of(System.getProperty("user.home")));
        context.setHome(Path.of(System.getProperty("user.home")));

        PluginContext pluginContext = new PluginContext(
                commandRegistry,
                console,
                context);

        PluginLoader loader = new PluginLoader(
                pluginContext,
                Paths.get("").toAbsolutePath().resolve("plugins"));
        loader.loadAll();

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

        scanner.close();
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
        handler.execute(
                parser.parse(lexer.tokenizer()));
        buffer.setLength(0);
    }

}
