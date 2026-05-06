package org.example.core.Shell;

import org.example.api.Lexer.Lexer;
import org.example.api.Runtime.Mode;
import org.example.core.CommandRegistry;
import org.example.core.Config;
import org.example.core.ConfigLoader;
import org.example.api.Event.IEventBus;
import org.example.core.Main;
import org.example.core.Plugin.PluginContext;
import org.example.core.Plugin.PluginLoader;
import org.example.core.Runtime.Console;
import org.example.core.Runtime.Context;
import org.example.core.Runtime.RuntimeContext;
import org.example.core.Runtime.SessionContext;
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
    public final IEventBus bus;

    public ShellEnvironment() throws URISyntaxException, IOException {

        terminal = TerminalBuilder.builder()
                .system(true)
                .dumb(false)
                .build();

        console = new Console(terminal);
        bus     = new ShellEventBus();
        configLoader = new ConfigLoader();
        config = configLoader.load().orElse(new Config());

        context = new Context(
                new SessionContext(
                    Path.of(System.getProperty("user.home")),
                    System.getProperty("user.name"),
                    config.prompt),
                new RuntimeContext(Path.of(System.getProperty("user.home")),
                        true,
                        Mode.NORMAL,
                        0)
                );

        commandRegistry = new CommandRegistry();
        lexer   = new Lexer();
        handler = new ShellHandler(context, console, commandRegistry);
        parser  = new ShellParser(commandRegistry, bus);
        Path pluginsDir = getJarDir().resolve("plugins");

        pluginContext = new PluginContext(
                commandRegistry,
                bus,
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
