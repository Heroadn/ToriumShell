import org.example.api.Lexer.Lexer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plugins.Command.MoveCommand;
import org.plugins.Handler.MoveHandler;
import org.plugins.Parser.MoveParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public class MoveHandlerTest {

    private MockContext context;
    private MockConsole console;
    private MoveHandler handler;
    private Lexer lexer;
    private Path testDir;

    @BeforeEach
    void setup() throws Exception {
        testDir = Files.createTempDirectory("move_test");
        context = new MockContext(testDir);
        console  = new MockConsole();
        handler  = new MoveHandler();
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

    private MoveCommand prepare(String input) throws Exception {
        lexer.setInput(input);
        return (MoveCommand) new MoveParser().parse(lexer.tokenizer());
    }

    // ── renomear ─────────────────────────────────────────────────────

    @Test
    void mvRenomearArquivoCriaComNovoNome() throws Exception {
        Files.writeString(testDir.resolve("origem.txt"), "conteudo");

        handler.execute(prepare("mv origem.txt destino.txt"), context, console);

        assertTrue(Files.exists(testDir.resolve("destino.txt")));
    }

    @Test
    void mvRenomearArquivoRemoveOrigem() throws Exception {
        Files.writeString(testDir.resolve("origem.txt"), "conteudo");

        handler.execute(prepare("mv origem.txt destino.txt"), context, console);

        assertFalse(Files.exists(testDir.resolve("origem.txt")));
    }

    @Test
    void mvRenomearPreservaConteudo() throws Exception {
        Files.writeString(testDir.resolve("origem.txt"), "conteudo importante");

        handler.execute(prepare("mv origem.txt destino.txt"), context, console);

        assertEquals("conteudo importante", Files.readString(testDir.resolve("destino.txt")));
    }

    // ── mover para diretório ─────────────────────────────────────────

    @Test
    void mvArquivoParaDiretorioCriaNoDestino() throws Exception {
        Files.writeString(testDir.resolve("arquivo.txt"), "x");
        Files.createDirectory(testDir.resolve("pasta"));

        handler.execute(prepare("mv arquivo.txt pasta"), context, console);
        assertTrue(Files.exists(testDir.resolve("pasta/arquivo.txt")));
    }

    @Test
    void mvArquivoParaDiretorioRemoveOrigem() throws Exception {
        Files.writeString(testDir.resolve("arquivo.txt"), "x");
        Files.createDirectory(testDir.resolve("pasta"));

        handler.execute(prepare("mv arquivo.txt pasta"), context, console);

        assertFalse(Files.exists(testDir.resolve("arquivo.txt")));
    }

    @Test
    void mvArquivoParaDiretorioPreservaConteudo() throws Exception {
        Files.writeString(testDir.resolve("arquivo.txt"), "dados preservados");
        Files.createDirectory(testDir.resolve("pasta"));

        handler.execute(prepare("mv arquivo.txt pasta"), context, console);

        assertEquals("dados preservados", Files.readString(testDir.resolve("pasta/arquivo.txt")));
    }

    // ── mover diretório ──────────────────────────────────────────────

    @Test
    void mvDiretorioMovePastaInteira() throws Exception {
        Path src = Files.createDirectory(testDir.resolve("src"));
        Files.writeString(src.resolve("a.txt"), "aaa");
        Path sub = Files.createDirectory(src.resolve("sub"));
        Files.writeString(sub.resolve("b.txt"), "bbb");

        handler.execute(prepare("mv -r src dst"), context, console);

        assertTrue(Files.exists(testDir.resolve("dst/a.txt")));
        assertTrue(Files.exists(testDir.resolve("dst/sub/b.txt")));
    }

    @Test
    void mvDiretorioRemoveOrigem() throws Exception {
        Files.createDirectory(testDir.resolve("src"));

        handler.execute(prepare("mv -r src dst"), context, console);
        assertFalse(Files.exists(testDir.resolve("src")));
    }

    // ── sobrescrever ─────────────────────────────────────────────────

    @Test
    void mvSobrescreveDestinoExistente() throws Exception {
        Files.writeString(testDir.resolve("origem.txt"), "novo");
        Files.writeString(testDir.resolve("destino.txt"), "antigo");

        handler.execute(prepare("mv origem.txt destino.txt"), context, console);

        assertEquals("novo", Files.readString(testDir.resolve("destino.txt")));
        assertFalse(Files.exists(testDir.resolve("origem.txt")));
    }

    // ── erros ────────────────────────────────────────────────────────

    @Test
    void mvArquivoInexistenteLancaErro() {
        assertThrows(Exception.class,
                () -> handler.execute(prepare("mv naoexiste.txt destino.txt"), context, console));
    }

    @Test
    void mvSemArgumentosLancaErro() {
        assertThrows(Exception.class,
                () -> handler.execute(prepare("mv"), context, console));
    }

    @Test
    void mvSomenteUmArgumentoLancaErro() {
        assertThrows(Exception.class,
                () -> handler.execute(prepare("mv origem.txt"), context, console));
    }
}