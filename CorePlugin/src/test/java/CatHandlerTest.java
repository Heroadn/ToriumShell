import org.example.api.Lexer.Lexer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plugins.Command.CatCommand;
import org.plugins.Handler.CatHandler;
import org.plugins.Parser.CatParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import static org.junit.jupiter.api.Assertions.*;

public class CatHandlerTest {
    private MockContext context;
    private MockConsole console;
    private CatHandler handler;
    private Lexer lexer;
    private Path testDir;

    @BeforeEach
    void setup() throws Exception {
        testDir = Files.createTempDirectory("cat_test");

        context = new MockContext(testDir);
        console = new MockConsole();
        handler = new CatHandler();
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

    private CatCommand prepare(String input) throws Exception {
        lexer.setInput(input);
        return (CatCommand) new CatParser().parse(lexer.tokenizer());
    }

    // ── leitura básica ─────────────────────────────────────────────

    @Test
    void catMostraConteudoArquivo() throws Exception {
        Path file = testDir.resolve("file.txt");
        Files.writeString(file, "hello world");

        handler.execute(prepare("cat file.txt"), context, console);

        String output = console.output();

        assertTrue(output.contains("hello world"));
    }

    // ── múltiplas linhas ───────────────────────────────────────────

    @Test
    void catMostraMultiplasLinhas() throws Exception {
        Path file = testDir.resolve("file.txt");
        Files.writeString(file, "linha1\nlinha2\nlinha3");

        handler.execute(prepare("cat file.txt"), context, console);

        String output = console.output();

        assertTrue(output.contains("linha1"));
        assertTrue(output.contains("linha2"));
        assertTrue(output.contains("linha3"));
    }

    // ── arquivo inexistente ────────────────────────────────────────

    @Test
    void catArquivoInexistenteLancaErro() {
        assertThrows(Exception.class, () ->
                handler.execute(prepare("cat naoexiste.txt"), context, console));
    }

    // ── diretório em vez de arquivo ────────────────────────────────

    @Test
    void catDiretorioLancaErro() throws Exception {
        Path dir = Files.createDirectory(testDir.resolve("pasta"));

        assertThrows(Exception.class, () ->
                handler.execute(prepare("cat pasta"), context, console));
    }

    // ── arquivo vazio ──────────────────────────────────────────────

    @Test
    void catArquivoVazio() throws Exception {
        Path file = testDir.resolve("empty.txt");
        Files.createFile(file);

        handler.execute(prepare("cat empty.txt"), context, console);

        String output = console.output();

        assertTrue(output.isEmpty() || output.trim().isEmpty());
    }

    // ── caminho relativo ───────────────────────────────────────────

    @Test
    void catFuncionaEmSubdiretorio() throws Exception {
        Path sub = Files.createDirectory(testDir.resolve("sub"));
        Path file = sub.resolve("file.txt");

        Files.writeString(file, "conteudo");

        context.setCurrentDir(sub);

        handler.execute(prepare("cat file.txt"), context, console);

        String output = console.output();

        assertTrue(output.contains("conteudo"));
    }
}
