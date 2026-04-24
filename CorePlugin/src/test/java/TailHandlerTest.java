import org.example.api.Lexer.Lexer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plugins.Command.TailCommand;
import org.plugins.Handler.TailHandler;
import org.plugins.Parser.TailParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TailHandlerTest {

    private MockContext context;
    private MockConsole console;
    private TailHandler handler;
    private Lexer lexer;
    private Path testDir;

    @BeforeEach
    void setup() throws Exception {
        testDir = Files.createTempDirectory("tail_test");
        context = new MockContext(testDir);
        console  = new MockConsole();
        handler  = new TailHandler();
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

    private TailCommand prepare(String input) throws Exception {
        lexer.setInput(input);
        return (TailCommand) new TailParser().parse(lexer.tokenizer());
    }

    // ── padrão (últimas 10 linhas) ───────────────────────────────────

    @Test
    void tailSemFlagRetornaUltimas10Linhas() throws Exception {
        Path file = testDir.resolve("arquivo.txt");
        Files.write(file, List.of(
                "linha1", "linha2", "linha3", "linha4", "linha5",
                "linha6", "linha7", "linha8", "linha9", "linha10",
                "linha11", "linha12"
        ));

        handler.execute(prepare("tail arquivo.txt"), context, console);

        //avoid console.output().contains -> substrings
        List<String> lines = console.output().lines().toList();
        assertFalse(lines.contains("linha1"));
        assertFalse(lines.contains("linha2"));
        assertTrue(lines.contains("linha3"));
        assertTrue(lines.contains("linha12"));
    }

    @Test
    void tailArquivoComMenosDe10LinhasRetornaTodas() throws Exception {
        Path file = testDir.resolve("pequeno.txt");
        Files.write(file, List.of("linha1", "linha2", "linha3"));

        handler.execute(prepare("tail pequeno.txt"), context, console);

        List<String> lines = console.output().lines().toList();
        assertTrue(lines.contains("linha1"));
        assertTrue(lines.contains("linha2"));
        assertTrue(lines.contains("linha3"));
    }

    // ── flag -n ──────────────────────────────────────────────────────

    @Test
    void tailComFlagNRetornaQuantidadeCorreta() throws Exception {
        Path file = testDir.resolve("arquivo.txt");
        Files.write(file, List.of("a", "b", "c", "d", "e"));

        handler.execute(prepare("tail -n 3 arquivo.txt"), context, console);

        String output = console.output();
        assertFalse(output.contains("a"));
        assertFalse(output.contains("b"));
        assertTrue(output.contains("c"));
        assertTrue(output.contains("d"));
        assertTrue(output.contains("e"));
    }

    @Test
    void tailComN1RetornaApenasUltimaLinha() throws Exception {
        Path file = testDir.resolve("arquivo.txt");
        Files.write(file, List.of("primeira", "segunda", "ultima"));

        handler.execute(prepare("tail -n 1 arquivo.txt"), context, console);

        String output = console.output();
        assertFalse(output.contains("primeira"));
        assertFalse(output.contains("segunda"));
        assertTrue(output.contains("ultima"));
    }

    @Test
    void tailComNMaiorQueArquivoRetornaTodas() throws Exception {
        Path file = testDir.resolve("arquivo.txt");
        Files.write(file, List.of("x", "y", "z"));

        handler.execute(prepare("tail -n 100 arquivo.txt"), context, console);

        String output = console.output();
        assertTrue(output.contains("x"));
        assertTrue(output.contains("y"));
        assertTrue(output.contains("z"));
    }

    // ── arquivo vazio ────────────────────────────────────────────────

    @Test
    void tailArquivoVazioNaoImprimenada() throws Exception {
        Path file = testDir.resolve("vazio.txt");
        Files.createFile(file);

        handler.execute(prepare("tail vazio.txt"), context, console);

        assertTrue(console.output().isBlank());
    }

    // ── erros ────────────────────────────────────────────────────────

    @Test
    void tailArquivoInexistenteExibeMensagemDeErro() throws Exception {
        assertThrows(Exception.class, () -> {
            handler.execute(prepare("tail naoexiste.txt"), context, console);

            String output = console.output();
            assertTrue(output.toLowerCase().contains("naoexiste.txt")
                    || output.toLowerCase().contains("not found")
                    || output.toLowerCase().contains("não encontrado"));
        });
    }

    @Test
    void tailEmDiretorioExibeMensagemDeErro() throws Exception {
        Files.createDirectory(testDir.resolve("subdir"));



        assertThrows(Exception.class, () -> {
            handler.execute(prepare("tail subdir"), context, console);
            String output = console.output();
            assertTrue(output.toLowerCase().contains("diretório")
                    || output.toLowerCase().contains("directory")
                    || output.toLowerCase().contains("is a directory"));
        });
    }

    @Test
    void tailSemArgumentoExibeMensagemDeErro() throws Exception {
        assertThrows(Exception.class, () -> {
            handler.execute(prepare("tail"), context, console);
            String output = console.output();
        });
    }
}
