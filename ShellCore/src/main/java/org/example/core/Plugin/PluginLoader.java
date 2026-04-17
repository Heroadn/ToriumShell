package org.example.core.Plugin;
import org.example.api.Command.Command;
import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;
import org.example.api.Parser.IParser;
import org.example.api.Plugin.IPlugin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

//TODO: put loading jar functions into a jar class
public class PluginLoader {
    private final List<URLClassLoader> classLoaders = new ArrayList<>();
    private final PluginContext context;
    private final Path pluginsDir;

    public PluginLoader(PluginContext context, Path pluginsDir) {
        this.context = context;
        this.pluginsDir = pluginsDir;
    }

    public void loadAll() throws IOException {
        if (!Files.exists(pluginsDir)) return;

        try (Stream<Path> files = Files.list(pluginsDir)) {
            files.filter(PluginLoader::isJar).forEach(this::loadJar);
        }
    }

    private void loadJar(Path jarPath) {
        try {
            URLClassLoader cl = getCl(jarPath);
            classLoaders.add(cl);

            try (JarFile jar = new JarFile(jarPath.toFile())) {
                jar.stream()
                        .filter(PluginLoader::isClass)
                        .forEach(e -> tryLoad(e, cl));
            }
        } catch (IOException e) {
            println("PluginLoader: erro ao carregar " + jarPath.getFileName() + " - " + e.getMessage());
        }
    }

    private void tryLoad(JarEntry entry, URLClassLoader cl) {
        String className = resolveClassName(entry);
        try {
            Class<?> clazz = cl.loadClass(className);
            Command[] commands = clazz.getAnnotationsByType(Command.class);
            if (commands.length == 0) return;

            if (isInterfacePluginImplemented(clazz)) {
                IPlugin plugin = (IPlugin) clazz.getDeclaredConstructor().newInstance();
                println("PluginLoader: carregado - " + plugin.getName());
            } else {
                println("PluginLoader: carregado - " + className);
            }

            for (Command cmd : commands) {
                registerCommand(clazz, cmd);
                println("PluginLoader: registrado - " + cmd.name());
            }

        } catch (Exception e) {
            println("PluginLoader: falha em " + className + " - " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void registerCommand(Class<?> clazz, Command cmd) {
        context.getCommandRegistry().register(
                (Class<? extends ICommand>) clazz,
                () -> (IParser)  newInstance(cmd.parser()),
                () -> (IHandler) newInstance(cmd.handler())
        );
    }

    private static String resolveClassName(JarEntry entry) {
        return entry.getName()
                .replace("/", ".")
                .replace(".class", "");
    }

    private static boolean isInterfacePluginImplemented(Class<?> clazz) {
        return IPlugin.class.isAssignableFrom(clazz);
    }

    private void registerCommand(ICommand command, Command annotation) {
        context.getCommandRegistry().register(
                command.getClass(),
                () -> (IParser) newInstance(annotation.parser()),
                () -> (IHandler) newInstance(annotation.handler())
        );
    }

    private static boolean isJar(Path path) {
        return path.toString().endsWith(".jar");
    }

    private static boolean isClass(JarEntry e) {
        return e.getName().endsWith(".class");
    }

    private URLClassLoader getCl(Path jarPath) throws MalformedURLException {
        return new URLClassLoader(new URL[]{jarPath.toUri().toURL()},
                getClass().getClassLoader());
    }

    private void println(String string)
    {
        context.getConsole().println(string);
    }

    private <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void close() throws IOException {
        for (URLClassLoader cl : classLoaders) cl.close();
    }
}