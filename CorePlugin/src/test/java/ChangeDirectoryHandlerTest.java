
import org.example.api.Command.ICommand;
import org.example.api.Lexer.Lexer;
import org.example.api.Parser.Token;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plugins.Command.ChangeDirectoryCommand;
import org.plugins.Handler.ChangeDirectoryHandler;
import org.plugins.Parser.CatParser;
import org.plugins.Parser.ChangeDirectoryParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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

    private ICommand prepare(String input) throws Exception {
        lexer.setInput(input);
        List<Token> tokens = new java.util.ArrayList<>(lexer.tokenizer());

        //
        if (!tokens.isEmpty())
            tokens.removeFirst();

        return new ChangeDirectoryParser().parse(tokens);
    }

    // ── navegação ────────────────────────────────────────────────────────────

    @Test
    void cdParaSubdiretorio() throws Exception {
        Path sub = Files.createDirectory(testDir.resolve("sub"));
        handler.execute(prepare("cd sub"), context, console);
        assertEquals(sub, context.getRuntime().getCurrentDir());
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
        context.getRuntime().setCurrentDir(sub);
        handler.execute(prepare("cd .."), context, console);
        assertEquals(testDir, context.getRuntime().getCurrentDir());
    }

    @Test
    void cdSemArgumentoVaiParaHome() throws Exception {
        handler.execute(prepare("cd"), context, console);
        assertEquals(context.getSession().getHome(), context.getRuntime().getCurrentDir());
    }
}
