import org.example.api.Lexer.Lexer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plugins.Command.CopyCommand;
import org.plugins.Handler.CopyHandler;
import org.plugins.Parser.CopyParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public class CopyHandlerTest {

    private MockContext context;
    private MockConsole console;
    private CopyHandler handler;
    private Lexer lexer;
    private Path testDir;

    @BeforeEach
    void setup() throws Exception {
        testDir = Files.createTempDirectory("copy_test");
        context = new MockContext(testDir);
        console  = new MockConsole();
        handler  = new CopyHandler();
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

    private CopyCommand prepare(String input) throws Exception {
        lexer.setInput(input);
        return (CopyCommand) new CopyParser().parse(lexer.tokenizer());
    }

    // ── arquivo simples ──────────────────────────────────────────────

    @Test
    void cpArquivoSimplesCriaNoDestino() throws Exception {
        Path source = testDir.resolve("origem.txt");
        Files.writeString(source, "conteudo");

        handler.execute(prepare("cp origem.txt destino.txt"), context, console);

        assertTrue(Files.exists(testDir.resolve("destino.txt")));
    }

    @Test
    void cpPreservaConteudoDoArquivo() throws Exception {
        Path source = testDir.resolve("origem.txt");
        Files.writeString(source, "conteudo importante");

        handler.execute(prepare("cp origem.txt destino.txt"), context, console);

        byte[] original = Files.readAllBytes(source);
        byte[] copia    = Files.readAllBytes(testDir.resolve("destino.txt"));
        assertArrayEquals(original, copia);
    }

    @Test
    void cpSobrescreveDestinoExistente() throws Exception {
        Files.writeString(testDir.resolve("origem.txt"), "novo conteudo");
        Files.writeString(testDir.resolve("destino.txt"), "conteudo antigo");

        handler.execute(prepare("cp origem.txt destino.txt"), context, console);

        String conteudo = Files.readString(testDir.resolve("destino.txt"));
        assertEquals("novo conteudo", conteudo);
    }

    // ── diretório recursivo ──────────────────────────────────────────

    @Test
    void cpDiretorioComFlagRCopiaEstrutura() throws Exception {
        Path dir = Files.createDirectory(testDir.resolve("src"));
        Files.writeString(dir.resolve("a.txt"), "aaa");
        Files.writeString(dir.resolve("b.txt"), "bbb");
        Path sub = Files.createDirectory(dir.resolve("sub"));
        Files.writeString(sub.resolve("c.txt"), "ccc");

        handler.execute(prepare("cp -r src dst"), context, console);

        assertTrue(Files.exists(testDir.resolve("dst/a.txt")));
        assertTrue(Files.exists(testDir.resolve("dst/b.txt")));
        assertTrue(Files.exists(testDir.resolve("dst/sub/c.txt")));
    }

    @Test
    void cpDiretorioComFlagRPreservaConteudo() throws Exception {
        Path dir = Files.createDirectory(testDir.resolve("src"));
        Files.writeString(dir.resolve("arquivo.txt"), "dados preservados");

        handler.execute(prepare("cp -r src dst"), context, console);

        String conteudo = Files.readString(testDir.resolve("dst/arquivo.txt"));
        assertEquals("dados preservados", conteudo);
    }

    @Test
    void cpDiretorioVazioComFlagRCriaDestinoVazio() throws Exception {
        Files.createDirectory(testDir.resolve("vazio"));

        handler.execute(prepare("cp -r vazio destVazio"), context, console);

        assertTrue(Files.isDirectory(testDir.resolve("destVazio")));
    }

    // ── erros ────────────────────────────────────────────────────────

    @Test
    void cpArquivoInexistenteLancaErro() {
        assertThrows(Exception.class,
                () -> handler.execute(prepare("cp naoexiste.txt destino.txt"), context, console));
    }

    @Test
    void cpDiretorioSemFlagRLancaErro() throws Exception {
        Files.createDirectory(testDir.resolve("dir"));

        assertThrows(Exception.class,
                () -> handler.execute(prepare("cp dir destino"), context, console));
    }

    @Test
    void cpSemArgumentosLancaErro() {
        assertThrows(Exception.class,
                () -> handler.execute(prepare("cp"), context, console));
    }

    @Test
    void cpSomenteUmArgumentoLancaErro() {
        assertThrows(Exception.class,
                () -> handler.execute(prepare("cp origem.txt"), context, console));
    }
}