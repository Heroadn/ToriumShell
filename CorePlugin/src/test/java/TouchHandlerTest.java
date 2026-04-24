import org.example.api.Lexer.Lexer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plugins.Command.TouchCommand;
import org.plugins.Handler.TouchHandler;
import org.plugins.Parser.TouchParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public class TouchHandlerTest {

    private MockContext context;
    private MockConsole console;
    private TouchHandler handler;
    private Lexer lexer;
    private Path testDir;

    @BeforeEach
    void setup() throws Exception {
        testDir = Files.createTempDirectory("touch_test");
        context = new MockContext(testDir);
        console  = new MockConsole();
        handler  = new TouchHandler();
        lexer    = new Lexer();
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

    private TouchCommand prepare(String input) throws Exception {
        lexer.setInput(input);
        return (TouchCommand) new TouchParser().parse(lexer.tokenizer());
    }

    // ── criação ──────────────────────────────────────────────────────

    @Test
    void touchCriaArquivoInexistente() throws Exception {
        handler.execute(prepare("touch novo.txt"), context, console);

        assertTrue(Files.exists(testDir.resolve("novo.txt")));
    }

    @Test
    void touchCriaArquivoVazio() throws Exception {
        handler.execute(prepare("touch novo.txt"), context, console);

        assertEquals(0, Files.size(testDir.resolve("novo.txt")));
    }

    @Test
    void touchMultiplosArquivosCriaOsTodos() throws Exception {
        handler.execute(prepare("touch a.txt b.txt c.txt"), context, console);

        assertTrue(Files.exists(testDir.resolve("a.txt")));
        assertTrue(Files.exists(testDir.resolve("b.txt")));
        assertTrue(Files.exists(testDir.resolve("c.txt")));
    }

    // ── arquivo existente ────────────────────────────────────────────

    @Test
    void touchArquivoExistenteNaoLancaErro() throws Exception {
        Files.createFile(testDir.resolve("existente.txt"));

        assertDoesNotThrow(
                () -> handler.execute(prepare("touch existente.txt"), context, console));
    }

    @Test
    void touchArquivoExistenteNaoApagaConteudo() throws Exception {
        Path file = testDir.resolve("existente.txt");
        Files.writeString(file, "conteudo preservado");

        handler.execute(prepare("touch existente.txt"), context, console);

        assertEquals("conteudo preservado", Files.readString(file));
    }

    @Test
    void touchArquivoExistenteAtualizaTimestamp() throws Exception {
        Path file = testDir.resolve("existente.txt");
        Files.createFile(file);

        FileTime antes = FileTime.from(Instant.now().minusSeconds(5));
        Files.setLastModifiedTime(file, antes);

        handler.execute(prepare("touch existente.txt"), context, console);

        FileTime depois = Files.getLastModifiedTime(file);
        assertTrue(depois.compareTo(antes) > 0);
    }

    // ── erros ────────────────────────────────────────────────────────

    @Test
    void touchSemArgumentosLancaErro() {
        assertThrows(Exception.class,
                () -> handler.execute(prepare("touch"), context, console));
    }
}