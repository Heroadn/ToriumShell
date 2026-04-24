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

### Navigation

| Command | Usage | Description |
|---------|-------|-------------|
| `ls` | `ls [-l]` | List directory contents |
| `cd` | `cd <path>` | Change current directory |
| `cd` | `cd -` | Return to previous directory |
| `pwd` | `pwd` | Print current directory path |
| `mkdir` | `mkdir [-p] <name>` | Create a new directory |
| `rm` | `rm [-r] <path>` | Remove a file or directory |
| `tree` | `tree [-d N] [path]` | Print ASCII directory tree (default depth: 4) |

### Files

| Command | Usage | Description |
|---------|-------|-------------|
| `cat` | `cat <file>` | Print file contents |
| `head` | `head [-n N] <file>` | Print first N lines (default: 10) |
| `tail` | `tail [-n N] <file>` | Print last N lines (default: 10) |
| `cp` | `cp [-r] <source> <dest>` | Copy a file or directory |
| `mv` | `mv <source> <dest>` | Move or rename a file or directory |
| `touch` | `touch <file...>` | Create file or update its timestamp |
| `wc` | `wc [-l\|-w\|-c] <file>` | Count lines, words and characters |
| `grep` | `grep [-i] [-n] [-v] [-c] <pattern> <file>` | Search for a pattern in a file |
| `find` | `find [-t file\|dir] [-d N]` | Find files or directories |

### System

| Command | Usage | Description |
|---------|-------|-------------|
| `echo` | `echo <text>` | Print text to the console |
| `clear` | `clear` | Clear the screen |
| `date` | `date` | Print current date and time |
| `history` | `history` | Show command history |
| `env` | `env` | List environment variables |
| `set` | `set <key>=<value>` | Set a session variable |
| `alias` | `alias <name>=<cmd>` | Create a command shortcut |
| `help` | `help [command]` | Show command description, usage and flags |
| `plugins` | `plugins` | List loaded plugins with version and commands |
| `exit` | `exit` | Exit the shell |

### Operators

| Operator | Example | Description |
|----------|---------|-------------|
| `>` | `echo hi > file.txt` | Redirect output to file (overwrite) |
| `>>` | `echo hi >> file.txt` | Redirect output to file (append) |
| `\|` | `cat file.txt \| grep err` | Pipe output to next command |
| `&&` | `mkdir a && cd a` | Run next command only if previous succeeded |
| `;` | `echo a ; echo b` | Run commands sequentially |
| `$VAR` | `echo $HOME` | Expand session variable |

---

## Customizing the Prompt

ToriumShell reads `~/.toriumshellrc` on startup and applies your preferences. You can also change the prompt at runtime with `config prompt`.

### `.toriumshellrc`

```properties
prompt={user} {dir} $
```

Available substitutions:

| Token | Description |
|-------|-------------|
| `{user}` | Current username |
| `{dir}` | Current directory (absolute) |
| `{home}` | Home directory path |

Example prompts:

```properties
# minimal
prompt={dir} $

# git-style
prompt=[{user}@shell {dir}] >
```

### Changing the prompt at runtime

```
config prompt {user} {dir} $
```

Changes take effect immediately and are saved to `~/.toriumshellrc`.

---

## Official Plugins

### TamagochiPlugin

A virtual pet that lives inside your shell. Stats decay over real time — if you forget about it for too long, it dies.

| Command | Description |
|---------|-------------|
| `tama new <name>` | Create a new pet |
| `tama status` | Show pet status with ASCII art and stat bars |
| `tama eat` | Feed your pet — hunger +30, happiness +10 |
| `tama feed <food>` | Feed a specific food (`pizza`, `salad`, `candy`, `cake`, `water`) |
| `tama play` | Play with your pet — happiness +25, energy -20 |
| `tama sleep` | Put your pet to sleep — energy +50, age +1 |
| `tama medicine` | Cure a sick pet |
| `tama bath` | Clean your pet — removes dirty debuff |
| `tama train` | Train your pet — XP +15, energy -10 |
| `tama walk` | Take your pet for a walk — happiness +15, energy -5 |
| `tama rename <name>` | Rename your pet |
| `tama history` | Show last 10 events |
| `tama info` | Quick summary without stat bars |
| `tama bag` | Show inventory |
| `tama use <item>` | Use an item from your inventory |
| `tama achievements` | Show unlocked achievements |
| `tama list` | List all your pets |
| `tama switch <name>` | Switch active pet |
| `tama game` | Open interactive TUI game screen |

Stats decay every hour your shell is closed: hunger -3, happiness -2, energy -1. Pets die after 48h without attention.

**Personalities** affect how fast stats decay and how much XP is gained per action.

### HttpPlugin

Send HTTP requests directly from the shell.

| Command | Description |
|---------|-------------|
| `get <url>` | Send a GET request |
| `post <url>` | Send a POST request |
| `put <url>` | Send a PUT request |
| `delete <url>` | Send a DELETE request |
| `curl <url>` | Alias for get with full response headers |
| `http-history` | List saved requests |
| `http-save <name>` | Save last request with a name |
| `http-run <name>` | Re-run a saved request |

### GitPlugin

Run Git commands without leaving the shell. The current branch is shown in the prompt via `{branch}`.

| Command | Description |
|---------|-------------|
| `git status` | Show working tree status |
| `git add` | Stage changes |
| `git commit` | Commit staged changes |
| `git push` | Push to remote |
| `git pull` | Pull from remote |
| `git log [-n N]` | Show last N commits (default: 10) |
| `git branch` | List branches |
| `git checkout` | Switch branch |

Add `{branch}` to your prompt to always see the active branch:

```properties
prompt={user} {dir} ({branch}) $
```

### NotesPlugin

| Command | Description |
|---------|-------------|
| `note add` | Add a new note |
| `note list` | List all notes |
| `note remove` | Remove a note |
| `note search` | Search notes by keyword |
| `note tag` | Tag a note |
| `note export` | Export notes to a file |

### TodoPlugin

| Command | Description |
|---------|-------------|
| `todo add [-p alta\|media\|baixa]` | Add a task with optional priority |
| `todo list` | List all tasks |
| `todo done` | Mark a task as done |
| `todo remove` | Remove a task |
| `todo stats` | Show completion stats |

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

        iConsole.println("Hello, " + name + "!");
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
    void execute(ICommand command, IContext context, IConsole console) throws Exception;
}
```

### `ICommand` / `BaseCommand`

`BaseCommand` provides default implementations for `getArgs()`, `setArgs()`, `getFlags()`, `setFlags()` and `has(flag)`. Extend it for most commands.

### `ICommandRegistry`

```java
public interface ICommandRegistry {
    void register(
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
