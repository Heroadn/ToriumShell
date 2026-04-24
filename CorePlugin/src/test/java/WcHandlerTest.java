import org.example.api.Lexer.Lexer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plugins.Command.WcCommand;
import org.plugins.Handler.WcHandler;
import org.plugins.Parser.WcParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WcHandlerTest {

    private MockContext context;
    private MockConsole console;
    private WcHandler handler;
    private Lexer lexer;
    private Path testDir;

    @BeforeEach
    void setup() throws Exception {
        testDir = Files.createTempDirectory("wc_test");
        context = new MockContext(testDir);
        console  = new MockConsole();
        handler  = new WcHandler();
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

    private WcCommand prepare(String input) throws Exception {
        lexer.setInput(input);
        return (WcCommand) new WcParser().parse(lexer.tokenizer());
    }

    // ── contagem completa ────────────────────────────────────────────

    @Test
    void wcSemFlagExibeLinhasPalavrasEChars() throws Exception {
        Path file = testDir.resolve("arquivo.txt");
        Files.write(file, List.of("hello world", "foo bar baz"));

        handler.execute(prepare("wc arquivo.txt"), context, console);

        String output = console.output();
        assertTrue(output.contains("2"));  // linhas
        assertTrue(output.contains("5"));  // palavras
    }

    @Test
    void wcContagemCorretaDe3Linhas() throws Exception {
        Path file = testDir.resolve("arquivo.txt");
        Files.write(file, List.of("a", "b", "c"));

        handler.execute(prepare("wc arquivo.txt"), context, console);

        assertTrue(console.output().contains("3"));
    }

    // ── flag -l (linhas) ─────────────────────────────────────────────

    @Test
    void wcFlagLRetornaApenasLinhas() throws Exception {
        Path file = testDir.resolve("arquivo.txt");
        Files.write(file, List.of("linha1", "linha2", "linha3"));

        handler.execute(prepare("wc -l arquivo.txt"), context, console);

        List<String> output = console.output().lines().toList();
        assertTrue(output.stream().anyMatch(l -> l.contains("3")));
    }

    @Test
    void wcFlagLArquivoUmaLinha() throws Exception {
        Files.writeString(testDir.resolve("arquivo.txt"), "unica linha");

        handler.execute(prepare("wc -l arquivo.txt"), context, console);

        assertTrue(console.output().contains("1"));
    }

    // ── flag -w (palavras) ───────────────────────────────────────────

    @Test
    void wcFlagWRetornaApenasPalavras() throws Exception {
        Path file = testDir.resolve("arquivo.txt");
        Files.write(file, List.of("um dois tres", "quatro cinco"));

        handler.execute(prepare("wc -w arquivo.txt"), context, console);

        assertTrue(console.output().contains("5"));
    }

    @Test
    void wcFlagWLinhaComEspacosDuplos() throws Exception {
        Files.writeString(testDir.resolve("arquivo.txt"), "um  dois   tres");

        handler.execute(prepare("wc -w arquivo.txt"), context, console);

        assertTrue(console.output().contains("3"));
    }

    // ── flag -c (chars) ──────────────────────────────────────────────

    @Test
    void wcFlagCRetornaApenasChars() throws Exception {
        Files.writeString(testDir.resolve("arquivo.txt"), "abc");

        handler.execute(prepare("wc -c arquivo.txt"), context, console);

        assertTrue(console.output().contains("3"));
    }

    // ── arquivo vazio ────────────────────────────────────────────────

    @Test
    void wcArquivoVazioRetornaZeros() throws Exception {
        Files.createFile(testDir.resolve("vazio.txt"));

        handler.execute(prepare("wc vazio.txt"), context, console);

        String output = console.output();
        assertTrue(output.contains("0"));
    }

    @Test
    void wcFlagLArquivoVazioRetornaZero() throws Exception {
        Files.createFile(testDir.resolve("vazio.txt"));

        handler.execute(prepare("wc -l vazio.txt"), context, console);

        assertTrue(console.output().contains("0"));
    }

    // ── erros ────────────────────────────────────────────────────────

    @Test
    void wcArquivoInexistenteLancaErro() {
        assertThrows(Exception.class,
                () -> handler.execute(prepare("wc naoexiste.txt"), context, console));
    }

    @Test
    void wcDiretorioLancaErro() throws Exception {
        Files.createDirectory(testDir.resolve("subdir"));

        assertThrows(Exception.class,
                () -> handler.execute(prepare("wc subdir"), context, console));
    }

    @Test
    void wcSemArgumentosLancaErro() {
        assertThrows(Exception.class,
                () -> handler.execute(prepare("wc"), context, console));
    }
}