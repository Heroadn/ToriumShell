# ToriumShell

A Unix-style shell written in Java with a plugin-based architecture. Commands are loaded at runtime from external JARs — no recompilation of the core needed.

## Architecture

```
Lexer → ShellParser → AbstractParser (subclasses) → IHandler
                           ↑
                     CommandRegistry
                           ↑
                     PluginLoader (loads .jar files from plugins/)
```

**Modules:**
- `ShellApi` — public API for plugin developers (annotations, interfaces)
- `ShellCore` — shell engine (lexer, parser, loader, handlers)
- `CorePlugin` — built-in commands packaged as a plugin

---

## Built-in Commands

| Command | Usage | Description |
|---------|-------|-------------|
| `ls` | `ls [-l]` | List directory contents |
| `cd` | `cd <path>` | Change current directory |
| `mkdir` | `mkdir <n>` | Create a new directory |
| `rm` | `rm [-r] <path>` | Remove a file or directory |
| `echo` | `echo <text>` | Print text to the console |
| `exit` | `exit` | Exit the shell |

---

## Official Plugins

### TamagochiPlugin

A virtual pet that lives inside your shell. Stats decay over real time — if you forget about it for too long, it dies.

| Command | Description |
|---------|-------------|
| `tama new <n>` | Create a new pet |
| `tama status` | Show pet status with ASCII art and stat bars |
| `tama eat` | Feed your pet — hunger +30, happiness +10 |
| `tama feed <food>` | Feed a specific food (`pizza`, `salad`, `candy`) |
| `tama play` | Play with your pet — happiness +25, energy -20 |
| `tama sleep` | Put your pet to sleep — energy +50, age +1 |
| `tama medicine` | Cure a sick pet |
| `tama rename <n>` | Rename your pet |
| `tama history` | Show last 10 events |
| `tama info` | Quick summary without stat bars |

Stats decay every hour your shell is closed: hunger -3, happiness -2, energy -1. Pets die after 48h without attention.

---

## Subcommands

ToriumShell supports two-level subcommands natively. Plugins can register commands like `tama-eat` and users invoke them as `tama eat` with a space — the `ShellParser` joins the tokens automatically.

To register a subcommand, use a hyphen in the `@Command` name:

```java
@Command(name = "tama-eat", parser = TamaEatParser.class, handler = TamaEatHandler.class)
public class TamaEatCommand extends BaseCommand {}
```

The user types:

```
/home/user $ tama eat
```

The parser finds no command named `"tama"`, peeks at the next token, tries `"tama-eat"`, finds it in the registry, and dispatches correctly.

---

## Building

Requires Java 26 and Maven.

```bash
# Build everything and copy plugins to target/plugins/
mvn package -P full

# Build ShellApi only (for publishing)
mvn install -pl ShellApi
```

Run the shell:

```bash
java -jar ShellCore/target/ShellCore.jar
```

Plugins are loaded automatically from the `target/plugins/` directory on startup.

---

## Writing a Plugin

Anyone can create a plugin without access to `ShellCore`. The only dependency needed is `ShellApi`, available via JitPack.

### 1. Add the dependency

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
    <repository>
        <id>central</id>
        <url>https://repo.maven.apache.org/maven2</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.Heroadn</groupId>
        <artifactId>ToriumShell</artifactId>
        <version>main-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

> `scope provided` means ShellApi will not be bundled inside your plugin JAR — the shell already has it on the classpath at runtime.

> If your plugin has its own dependencies (e.g. Jackson for JSON persistence), use `maven-shade-plugin` to produce a fat JAR so those dependencies are bundled inside.

### 2. Create a Command class

Annotate your command class with `@Command`, pointing to your parser and handler:

```java
import org.example.api.Command.BaseCommand;
import org.example.api.Command.Command;

@Command(
    name    = "hello",
    parser  = HelloParser.class,
    handler = HelloHandler.class
)
public class HelloCommand extends BaseCommand {}
```

Multiple commands in the same JAR — one class per command:

```java
@Command(name = "hello",   parser = HelloParser.class,   handler = HelloHandler.class)
public class HelloCommand extends BaseCommand {}

@Command(name = "goodbye", parser = GoodbyeParser.class, handler = GoodbyeHandler.class)
public class GoodbyeCommand extends BaseCommand {}
```

For subcommands, use a hyphen in the name — the user types a space:

```java
// user types: git status
@Command(name = "git-status", parser = GitStatusParser.class, handler = GitStatusHandler.class)
public class GitStatusCommand extends BaseCommand {}

// user types: git commit
@Command(name = "git-commit", parser = GitCommitParser.class, handler = GitCommitHandler.class)
public class GitCommitCommand extends BaseCommand {}
```

### 3. Create a Parser

Extend `AbstractParser` and implement the `parse()` method:

```java
import org.example.api.Command.ICommand;
import org.example.api.Parser.AbstractParser;

public class HelloParser extends AbstractParser {

    @Override
    protected ICommand parse() throws Exception {
        HelloCommand cmd = new HelloCommand();

        allowedFlags.add("-l");
        var parsed = consumeArgs();

        cmd.setArgs(parsed.args());
        cmd.setFlags(parsed.flags());

        return cmd;
    }
}
```

> Both parsers and handlers **must have a public no-arg constructor** — the `PluginLoader` instantiates them via reflection.

### 4. Create a Handler

```java
import org.example.api.Command.ICommand;
import org.example.api.Handler.IHandler;

public class HelloHandler implements IHandler {

    @Override
    public void execute(ICommand iCommand, IContext iContext, IConsole iConsole) {
        String name = command.getArgs().isEmpty()
            ? "World"
            : command.getArgs().get(0);

        System.out.println("Hello, " + name + "!");
    }
}
```

### 5. Build and install

Configure your `pom.xml` to copy the JAR into the shell's `plugins/` directory after build:

```xml
<build>
    <finalName>hello</finalName>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-antrun-plugin</artifactId>
            <version>3.1.0</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals><goal>run</goal></goals>
                    <configuration>
                        <target>
                            <copy file="${project.build.directory}/hello.jar"
                                  todir="/path/to/ToriumShell/target/plugins"/>
                        </target>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

Then run:

```bash
mvn package
```

Your `hello.jar` will be copied to the plugins directory. Start the shell and `hello` will be available.

---

## How Plugin Loading Works

On startup, `PluginLoader` scans every `.jar` file in the `plugins/` directory:

1. Loads each JAR with a `URLClassLoader` — kept open for the lifetime of the shell
2. Scans all `.class` files inside the JAR
3. Finds classes annotated with `@Command`
4. Registers `commandName → (parserSupplier, handlerSupplier)` in `CommandRegistry`
5. When the user types a command, `ShellParser` resolves the name (including subcommand joining) and dispatches

When the shell exits, all classloaders are closed via `PluginLoader.close()`.

---

## Plugin API Reference

### `@Command`

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `name` | `String` | yes | Command name. Use `"prefix-sub"` for subcommands (e.g. `"tama-eat"`) |
| `parser` | `Class<?>` | yes | Parser class — must have a no-arg constructor |
| `handler` | `Class<?>` | yes | Handler class — must have a no-arg constructor |
| `description` | `String` | no | Short description (default `""`) |
| `usage` | `String` | no | Usage string (default `""`) |
| `flags` | `String[]` | no | Declared flags (default `{}`) |

### `AbstractParser`

| Method | Description |
|--------|-------------|
| `peek()` | Returns the next token without consuming it |
| `peek(String value)` | Returns true if the next token matches the value |
| `peek(Token.TYPES type)` | Returns true if the next token matches the type |
| `consume()` | Consumes and returns the next token |
| `expect(String value)` | Consumes a token and returns true if it matches |
| `consumeArgs()` | Consumes all remaining tokens, separating flags from args |
| `isEmpty()` | Returns true if the token queue is empty |
| `size()` | Returns the number of remaining tokens |

### `IHandler`

```java
public interface IHandler {
    void handle(ICommand command) throws Exception;
}
```

### `ICommand` / `BaseCommand`

`BaseCommand` provides default implementations for `getArgs()`, `setArgs()`, `getFlags()`, `setFlags()`. Extend it for most commands.

### `ICommandRegistry`

```java
public interface ICommandRegistry {
    public void register(
            Class<? extends ICommand> command,
            Supplier<IParser> parser,
            Supplier<IHandler> handler);
    boolean has(String name);
}
```

---

## Releases

| Version | Description |
|---------|-------------|
| `main-SNAPSHOT` | Latest from main branch — may be unstable |
| `v1.0.0` | First stable API release |

To use a specific stable release:

```xml
<dependency>
    <groupId>com.github.Heroadn</groupId>
    <artifactId>ToriumShell</artifactId>
    <version>v1.0.0</version>
    <scope>provided</scope>
</dependency>
```

---

## License

MIT
