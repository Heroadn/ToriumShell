package org.example.core.Shell;

import org.example.api.Lexer.Lexer;
import org.example.core.CommandRegistry;
import org.example.core.Config;
import org.example.core.ConfigLoader;
import org.example.core.Main;
import org.example.core.Plugin.PluginContext;
import org.example.core.Plugin.PluginLoader;
import org.example.core.Runtime.Console;
import org.example.core.Runtime.Context;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class ShellEnvironment
{
    public final CommandRegistry commandRegistry;
    public final PluginLoader pluginLoader;
    public final ConfigLoader configLoader;
    public final PluginContext pluginContext;

    public final Context context;
    public final Console console;
    public final Terminal terminal;
    public final Config config;

    public final Lexer lexer;
    public final ShellHandler handler;
    public final ShellParser parser;

    public ShellEnvironment() throws URISyntaxException, IOException {

        terminal = TerminalBuilder.builder()
                .system(true)
                .dumb(false)
                .build();

        context = new Context();
        console = new Console(terminal);
        configLoader = new ConfigLoader();
        config = configLoader.load().orElse(new Config());

        commandRegistry = new CommandRegistry();
        lexer   = new Lexer();
        handler = new ShellHandler(context, console, commandRegistry);
        parser  = new ShellParser(commandRegistry);

        context.setCurrentDir(Path.of(System.getProperty("user.home")));
        context.setHome(Path.of(System.getProperty("user.home")));
        context.setUserName(System.getProperty("user.name"));
        context.setPrompt(config.prompt);
        Path pluginsDir = getJarDir().resolve("plugins");

        pluginContext = new PluginContext(
                commandRegistry,
                console,
                context);


        pluginLoader = new PluginLoader(
                pluginContext,
                pluginsDir);
        try {
            pluginLoader.loadAll();
        } catch (IOException e) {
            console.println("Aviso: erro ao carregar plugins — " + e.getMessage());
        }
    }

    private static Path getJarDir() {
        try {
            return Path.of(Main.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()).getParent();
        } catch (Exception e) {
            return Path.of("."); // fallback para diretório atual
        }
    }
}
