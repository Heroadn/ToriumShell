import org.example.api.Command.ICommand;
import org.example.api.Lexer.Lexer;
import org.example.api.Parser.Token;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plugins.Handler.CatHandler;
import org.plugins.Handler.DateHandler;
import org.plugins.Parser.CatParser;
import org.plugins.Parser.DateParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DateHandlerTest {
    private MockContext context;
    private MockConsole console;
    private DateHandler handler;
    private Lexer lexer;
    private Path testDir;
    private String FIXED_TIME = "2023-10-01T10:00:00Z"; //01-10-2023 10:00:00

    @BeforeEach
    void setup() throws Exception {
        testDir = Files.createTempDirectory("cat_test");

        context = new MockContext(testDir);
        console = new MockConsole();
        handler = new DateHandler();
        lexer   = new Lexer();
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

    private ICommand prepare(String input) throws Exception {
        lexer.setInput(input);
        List<Token> tokens = new java.util.ArrayList<>(lexer.tokenizer());

        //
        if (!tokens.isEmpty())
            tokens.removeFirst();

        return new DateParser().parse(tokens);
    }

    // ── leitura básica ─────────────────────────────────────────────

    @Test
    void dateMostrarDiaMesAnoHoraMinutoSegundo() throws Exception {
        context.setClock(Clock.fixed(Instant.parse(FIXED_TIME), ZoneId.of("UTC")));
        handler.execute(prepare("date"), context, console);

        //01-10-2023 10:00:00
        String output = console.output();
        assertEquals("01-10-2023 10:00:00", output);
    }

    @Test
    void dateMostrarFormatadoCorreto() throws Exception {
        context.setClock(Clock.fixed(Instant.parse(FIXED_TIME), ZoneId.of("UTC")));
        handler.execute(prepare("date -f HH:mm"), context, console);

        //01-10-2023 10:00:00
        String output = console.output();
        System.out.println(output);
        assertEquals("10:00", output);
    }

    @Test
    void dateMostrarFormatadoIncorreto() throws Exception {
        context.setClock(Clock.fixed(Instant.parse(FIXED_TIME), ZoneId.of("UTC")));
        assertThrows(Exception.class, () -> {
            handler.execute(prepare("date -f K::Jm: "), context, console);
        });
    }

}
