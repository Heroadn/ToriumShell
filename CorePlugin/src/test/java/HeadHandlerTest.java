import org.example.api.Lexer.Lexer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plugins.Command.HeadCommand;
import org.plugins.Handler.HeadHandler;
import org.plugins.Parser.HeadParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public class HeadHandlerTest {

    private MockContext context;
    private MockConsole console;
    private HeadHandler handler;
    private Lexer lexer;
    private Path testDir;

    @BeforeEach
    void setup() throws Exception {
        testDir = Files.createTempDirectory("head_test");

        context = new MockContext(testDir);
        console = new MockConsole();
        handler = new HeadHandler();
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

    private HeadCommand prepare(String input) throws Exception {
        lexer.setInput(input);
        return (HeadCommand) new HeadParser().parse(lexer.tokenizer());
    }

    // ── padrão (10 linhas) ────────────────────────────────────────

    @Test
    void headMostraPrimeiras10Linhas() throws Exception {
        Path file = testDir.resolve("file.txt");

        StringBuilder content = new StringBuilder();
        for (int i = 1; i <= 20; i++) {
            content.append("linha").append(i).append("\n");
        }

        Files.writeString(file, content.toString());

        handler.execute(prepare("head file.txt"), context, console);

        String output = console.output();

        assertTrue(output.contains("linha1"));
        assertTrue(output.contains("linha10"));
        assertFalse(output.contains("linha11"));
    }

    // ── com -n ────────────────────────────────────────────────────

    @Test
    void headComLimiteCustomizado() throws Exception {
        Path file = testDir.resolve("file.txt");

        Files.writeString(file, "a\nb\nc\nd\ne\n");

        handler.execute(prepare("head -n 3 file.txt"), context, console);

        String output = console.output();

        assertTrue(output.contains("a"));
        assertTrue(output.contains("b"));
        assertTrue(output.contains("c"));
        assertFalse(output.contains("d"));
    }

    // ── arquivo inexistente ───────────────────────────────────────

    @Test
    void headArquivoInexistente() {
        assertThrows(Exception.class, () ->
                handler.execute(prepare("head naoexiste.txt"), context, console));
    }

    // ── diretório ─────────────────────────────────────────────────

    @Test
    void headDiretorioLancaErro() throws Exception {
        Files.createDirectory(testDir.resolve("dir"));

        assertThrows(Exception.class, () ->
                handler.execute(prepare("head dir"), context, console));
    }

    // ── arquivo menor que limite ──────────────────────────────────

    @Test
    void headArquivoMenorQueLimite() throws Exception {
        Path file = testDir.resolve("file.txt");

        Files.writeString(file, "1\n2\n3\n");

        handler.execute(prepare("head file.txt"), context, console);

        String output = console.output();

        assertTrue(output.contains("1"));
        assertTrue(output.contains("2"));
        assertTrue(output.contains("3"));
    }
}