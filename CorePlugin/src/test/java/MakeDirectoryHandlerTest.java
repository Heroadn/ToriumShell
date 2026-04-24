
import org.example.api.Lexer.Lexer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plugins.Command.MakeDirectoryCommand;
import org.plugins.Handler.MakeDirectoryHandler;
import org.plugins.Parser.MakeDirectoryParser;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class MakeDirectoryHandlerTest {

    private MockContext context;
    private MockConsole console;
    private MakeDirectoryHandler handler;
    private Lexer lexer;
    private Path testDir;

    @BeforeEach
    void setup() throws Exception {
        testDir = Files.createTempDirectory("mkdir_test");
        context = new MockContext(testDir);
        console = new MockConsole();
        handler = new MakeDirectoryHandler();
        lexer   = new Lexer();
    }

    @AfterEach
    void cleanup() throws Exception {
        if (Files.exists(testDir))
            Files.walk(testDir).sorted(java.util.Comparator.reverseOrder())
                    .forEach(p -> { try { Files.delete(p); } catch (Exception ignored) {} });
    }

    private MakeDirectoryCommand prepare(String input) throws Exception {
        lexer.setInput(input);
        return (MakeDirectoryCommand) new MakeDirectoryParser().parse(lexer.tokenizer());
    }

    // ── criação simples ──────────────────────────────────────────────────────

    @Test
    void mkdirSimplesCriaPasta() throws Exception {
        handler.execute(prepare("mkdir novapasta"), context, console);
        assertTrue(Files.isDirectory(testDir.resolve("novapasta")));
    }

    @Test
    void mkdirCriaDentroDoCurrentDir() throws Exception {
        handler.execute(prepare("mkdir novapasta"), context, console);
        assertTrue(testDir.resolve("novapasta").startsWith(testDir));
    }

    @Test
    void mkdirJaExisteLancaErro() {
        assertThrows(Exception.class, () -> {
            handler.execute(prepare("mkdir novapasta"), context, console);
            handler.execute(prepare("mkdir novapasta"), context, console);
        });
    }

    @Test
    void mkdirSemNomeLancaErro() {
        assertThrows(Exception.class, () -> prepare("mkdir"));
    }

    @Test
    void mkdirFlagInvalidaLancaErro() {
        assertThrows(Exception.class, () -> prepare("mkdir -x novapasta"));
    }

    // ── flag -p ──────────────────────────────────────────────────────────────

    @Test
    void mkdirComPCriaSubdiretorios() throws Exception {
        handler.execute(prepare("mkdir -p pasta/sub/subsub"), context, console);
        assertTrue(Files.isDirectory(testDir.resolve("pasta/sub/subsub")));
    }

    @Test
    void mkdirComPJaExisteLancaErro() throws Exception {
        handler.execute(prepare("mkdir -p novapasta"), context, console);
        assertThrows(Exception.class, () ->
                handler.execute(prepare("mkdir -p novapasta"), context, console));
    }

    // ── currentDir não muda ──────────────────────────────────────────────────

    @Test
    void currentDirNaoMudaAposMkdir() throws Exception {
        Path antes = context.getCurrentDir();
        handler.execute(prepare("mkdir novapasta"), context, console);
        assertEquals(antes, context.getCurrentDir());
    }
}