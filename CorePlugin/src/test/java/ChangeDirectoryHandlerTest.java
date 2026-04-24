
import org.example.api.Lexer.Lexer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plugins.Command.ChangeDirectoryCommand;
import org.plugins.Handler.ChangeDirectoryHandler;
import org.plugins.Parser.ChangeDirectoryParser;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ChangeDirectoryHandlerTest {

    private MockContext context;
    private MockConsole console;
    private ChangeDirectoryHandler handler;
    private Lexer lexer;
    private Path testDir;

    @BeforeEach
    void setup() throws Exception {
        testDir = Files.createTempDirectory("cd_test");
        context = new MockContext(testDir);
        console = new MockConsole();
        handler = new ChangeDirectoryHandler();
        lexer   = new Lexer();
    }

    @AfterEach
    void cleanup() throws Exception {
        if (Files.exists(testDir))
            Files.walk(testDir).sorted(java.util.Comparator.reverseOrder())
                    .forEach(p -> { try { Files.delete(p); } catch (Exception ignored) {} });
    }

    private ChangeDirectoryCommand prepare(String input) throws Exception {
        lexer.setInput(input);
        return (ChangeDirectoryCommand) new ChangeDirectoryParser().parse(lexer.tokenizer());
    }

    // ── navegação ────────────────────────────────────────────────────────────

    @Test
    void cdParaSubdiretorio() throws Exception {
        Path sub = Files.createDirectory(testDir.resolve("sub"));
        handler.execute(prepare("cd sub"), context, console);
        assertEquals(sub, context.getCurrentDir());
    }

    @Test
    void cdParaHomeComTil() throws Exception {
        handler.execute(prepare("cd ~"), context, console);
        assertEquals(context.getHome(), context.getCurrentDir());
    }

    @Test
    void cdParaDiretorioInexistenteLancaErro() {
        assertThrows(Exception.class, () ->
                handler.execute(prepare("cd naoexiste"), context, console));
    }

    @Test
    void cdParaArquivoLancaErro() throws Exception {
        Files.createFile(testDir.resolve("arquivo.txt"));
        assertThrows(Exception.class, () ->
                handler.execute(prepare("cd arquivo.txt"), context, console));
    }

    @Test
    void cdParaPontoPonto() throws Exception {
        Path sub = Files.createDirectory(testDir.resolve("sub"));
        context.setCurrentDir(sub);
        handler.execute(prepare("cd .."), context, console);
        assertEquals(testDir, context.getCurrentDir());
    }

    @Test
    void cdSemArgumentoVaiParaHome() throws Exception {
        handler.execute(prepare("cd"), context, console);
        assertEquals(context.getHome(), context.getCurrentDir());
    }
}
