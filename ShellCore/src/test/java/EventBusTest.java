import org.example.api.Event.CommandExecuted;
import org.example.core.Parser.CommandNode;
import org.example.api.Event.EventBus;
import org.example.core.Shell.ShellEventBus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EventBusTest {
    private MockContext context;
    private MockConsole console;
    private Path testDir;
    private EventBus bus;

    @BeforeEach
    void setup() throws IOException {
        testDir = Files.createTempDirectory("cat_test");
        context = new MockContext(testDir,  new MockContext.MockSessionContext(testDir, "heroadn"));
        console = new MockConsole();
        bus = new ShellEventBus();
    }

    @AfterEach
    void cleanup() throws Exception {
        if (Files.exists(testDir)) {
            Files.walk(testDir)
                    .sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try { Files.delete(p); } catch (Exception ignored) {}
                    });
        }
    }

    public MockCommand basicCommand()
    {
        final Map<String, String> defaultFlagValues
                = Map.ofEntries(
                Map.entry("-n", "1"),
                Map.entry("-f", "file.txt")
        );

        final List<String> defaultArgs
                = List.of("a", "b", "c", "d");
        MockCommand command = new MockCommand();

        //
        for (var entry : defaultFlagValues.entrySet())
            command.put(entry.getKey(), entry.getValue());
        command.setArgs(defaultArgs);

        return command;
    }

    @Test
    void inscreverEventoEPublicarComandoSucesso() throws Exception {
        MockCommand mockCommand = basicCommand();

        bus.subscribe(CommandExecuted.class, event -> {
            assertEquals(0, event.exitCode());
            assertTrue(event.command().has("-n"));
        });
        bus.publish(new CommandExecuted(mockCommand, 0));
    }

    @Test
    void inscreverEventoEPublicarSemListener() throws Exception {
        MockCommand mockCommand = basicCommand();
        assertDoesNotThrow( () -> {
            bus.publish(new CommandExecuted(mockCommand, 0));
            bus.publish(new CommandExecuted(mockCommand, 0));
            bus.publish(new CommandExecuted(mockCommand, 0));
            bus.publish(new CommandExecuted(mockCommand, 0));
        } );
    }

    @Test
    void commandNodePublicaCommandSuccess() throws Exception {
        MockCommand mockCommand = basicCommand();
        CommandNode nodeSuccess = new CommandNode(
                mockCommand,
                new SuccessHandler(),
                bus);

        bus.subscribe(CommandExecuted.class, event -> {
            assertEquals(0, event.exitCode());
        });
        nodeSuccess.execute(context,console);
    }

    @Test
    void commandNodePublicaCommandFailed() {
        MockCommand mockCommand = basicCommand();
        CommandNode nodeFailed = new CommandNode(
                mockCommand,
                new FailureHandler(),
                bus);
        bus.subscribe(CommandExecuted.class, event -> {
            assertEquals(1, event.exitCode());
        });
        nodeFailed.execute(context, console);
    }
}