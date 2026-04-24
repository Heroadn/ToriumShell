import org.example.api.Lexer.Lexer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plugins.Command.TreeCommand;
import org.plugins.Handler.TreeHandler;
import org.plugins.Parser.TreeParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TreeHandlerTest {

    private MockContext context;
    private MockConsole console;
    private TreeHandler handler;
    private Lexer lexer;
    private Path testDir;

    @BeforeEach
    void setup() throws Exception {
        testDir = Files.createTempDirectory("tree_test");

        // estrutura:
        // testDir/
        //├── a/
        //│   ├── b.txt
        //│   ├── c.txt
        //│   └── nested/
        //│       ├── deep.txt
        //│       └── empty/
        //├── d/
        //│   ├── e/
        //│   │   └── f.txt
        //│   └── g.txt
        //├── h.txt
        //└── emptyDir/

        Path a = Files.createDirectory(testDir.resolve("a"));
        Files.createFile(a.resolve("b.txt"));
        Files.createFile(a.resolve("c.txt"));

        // a/nested/
        Path nested = Files.createDirectory(a.resolve("nested"));
        Files.createFile(nested.resolve("deep.txt"));

        // a/nested/empty/
        Files.createDirectory(nested.resolve("empty"));

        // d/
        Path d = Files.createDirectory(testDir.resolve("d"));
        Files.createFile(d.resolve("g.txt"));

        // d/e/
        Path e = Files.createDirectory(d.resolve("e"));
        Files.createFile(e.resolve("f.txt"));

        // arquivos na raiz
        Files.createFile(testDir.resolve("h.txt"));

        // diretório vazio
        Files.createDirectory(testDir.resolve("emptyDir"));

        context = new MockContext(testDir);
        console = new MockConsole();
        handler = new TreeHandler();
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

    private TreeCommand prepare(String input) throws Exception {
        lexer.setInput(input);
        return (TreeCommand) new TreeParser().parse(lexer.tokenizer());
    }

    // ── execução básica ─────────────────────────────────────────────

    @Test
    void treeMostraEstruturaBasica() throws Exception {
        handler.execute(prepare("tree"), context, console);

        System.out.println(console.output());
        //String output = console.output();
        //assertTrue(output.contains("a"));
        //assertTrue(output.contains("b.txt"));
        //assertTrue(output.contains("c.txt"));
    }

    // ── valida estrutura visual ─────────────────────────────────────

    @Test
    void treeContemCaracteresDeEstrutura() throws Exception {
        handler.execute(prepare("tree"), context, console);

        String output = console.output();

        assertTrue(output.contains("├──") || output.contains("└──"));
    }

    // ── diretório vazio ─────────────────────────────────────────────

    @Test
    void treeDiretorioVazio() throws Exception {
        Path empty = Files.createDirectory(testDir.resolve("empty"));

        context.setCurrentDir(empty);

        handler.execute(prepare("tree"), context, console);

        String output = console.output();

        assertTrue(output.contains("."));
    }

    // ── caminho específico ──────────────────────────────────────────

    @Test
    void treeComPath() throws Exception {
        handler.execute(prepare("tree a"), context, console);

        String output = console.output();

        assertTrue(output.contains("b.txt"));
        assertTrue(!output.contains("c.txt"));
    }
}