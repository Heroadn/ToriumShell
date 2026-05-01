import org.example.api.Command.ICommand;
import org.example.api.Lexer.Lexer;
import org.example.api.Parser.Token;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plugins.Command.FindCommand;
import org.plugins.Handler.FindHandler;
import org.plugins.Parser.FindParser;
import org.plugins.Parser.GrepParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FindHandlerTest {

    private MockContext context;
    private MockConsole console;
    private FindHandler handler;
    private Lexer lexer;
    private Path testDir;

    @BeforeEach
    void setup() throws Exception {
        testDir = Files.createTempDirectory("find_test");

        // estrutura:
        // testDir/
        // ├── a/
        // │   ├── b.txt
        // │   └── sub/
        // │       └── deep.txt
        // ├── c.txt
        // └── emptyDir/

        Path a = Files.createDirectory(testDir.resolve("a"));
        Files.createFile(a.resolve("b.txt"));
        Path sub = Files.createDirectory(a.resolve("sub"));
        Files.createFile(sub.resolve("deep.txt"));
        Files.createFile(testDir.resolve("c.txt"));
        Files.createDirectory(testDir.resolve("emptyDir"));

        context = new MockContext(testDir);
        console  = new MockConsole();
        handler  = new FindHandler();
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

    private ICommand prepare(String input) throws Exception {
        lexer.setInput(input);
        List<Token> tokens = new java.util.ArrayList<>(lexer.tokenizer());

        //
        if (!tokens.isEmpty())
            tokens.removeFirst();

        return new FindParser().parse(tokens);
    }

    // ── sem flags ────────────────────────────────────────────────────

    @Test
    void findSemFlagsListaTudoRecursivamente() throws Exception {
        handler.execute(prepare("find"), context, console);

        List<String> output = console.output().lines().toList();
        assertTrue(output.stream().anyMatch(l -> l.contains("a")));
        assertTrue(output.stream().anyMatch(l -> l.contains("b.txt")));
        assertTrue(output.stream().anyMatch(l -> l.contains("sub")));
        assertTrue(output.stream().anyMatch(l -> l.contains("deep.txt")));
        assertTrue(output.stream().anyMatch(l -> l.contains("c.txt")));
        assertTrue(output.stream().anyMatch(l -> l.contains("emptyDir")));
    }

    @Test
    void findSemFlagsNaoOmiteNenhumItem() throws Exception {
        handler.execute(prepare("find"), context, console);

        // 6 itens no total: a, b.txt, sub, deep.txt, c.txt, emptyDir
        assertTrue(console.output().lines().count() >= 6);
    }

    // ── flag -t file ─────────────────────────────────────────────────

    @Test
    void findFlagTFileListaSoArquivos() throws Exception {
        handler.execute(prepare("find -t file"), context, console);

        List<String> output = console.output().lines().toList();
        assertTrue(output.stream().anyMatch(l -> l.contains("b.txt")));
        assertTrue(output.stream().anyMatch(l -> l.contains("deep.txt")));
        assertTrue(output.stream().anyMatch(l -> l.contains("c.txt")));
    }

    @Test
    void findFlagTFileNaoListaDiretorios() throws Exception {
        handler.execute(prepare("find -t file"), context, console);

        List<String> output = console.output().lines().toList();
        assertFalse(output.stream().anyMatch(l -> l.endsWith("a")));
        assertFalse(output.stream().anyMatch(l -> l.endsWith("sub")));
        assertFalse(output.stream().anyMatch(l -> l.endsWith("emptyDir")));
    }

    // ── flag -t dir ──────────────────────────────────────────────────

    @Test
    void findFlagTDirListaSoDiretorios() throws Exception {
        handler.execute(prepare("find -t dir"), context, console);

        List<String> output = console.output().lines().toList();
        assertTrue(output.stream().anyMatch(l -> l.contains("a")));
        assertTrue(output.stream().anyMatch(l -> l.contains("sub")));
        assertTrue(output.stream().anyMatch(l -> l.contains("emptyDir")));
    }

    @Test
    void findFlagTDirNaoListaArquivos() throws Exception {
        handler.execute(prepare("find -t dir"), context, console);

        List<String> output = console.output().lines().toList();
        assertFalse(output.stream().anyMatch(l -> l.endsWith("b.txt")));
        assertFalse(output.stream().anyMatch(l -> l.endsWith("c.txt")));
        assertFalse(output.stream().anyMatch(l -> l.endsWith("deep.txt")));
    }

    // ── flag -d (profundidade) ───────────────────────────────────────

    @Test
    void findFlagD1NaoEntraEmSubdiretorios() throws Exception {
        handler.execute(prepare("find -d 1"), context, console);

        List<String> output = console.output().lines().toList();

        // nível 1 — aparece
        assertTrue(output.stream().anyMatch(l -> l.contains("a")));
        assertTrue(output.stream().anyMatch(l -> l.contains("c.txt")));
        assertTrue(output.stream().anyMatch(l -> l.contains("emptyDir")));

        // nível 2 em diante — não aparece
        assertFalse(output.stream().anyMatch(l -> l.contains("b.txt")));
        assertFalse(output.stream().anyMatch(l -> l.contains("sub")));
        assertFalse(output.stream().anyMatch(l -> l.contains("deep.txt")));
    }

    @Test
    void findFlagD2EntraAteSegundoNivel() throws Exception {
        handler.execute(prepare("find -d 2"), context, console);

        List<String> output = console.output().lines().toList();

        // nível 2 — aparece
        assertTrue(output.stream().anyMatch(l -> l.contains("b.txt")));
        assertTrue(output.stream().anyMatch(l -> l.contains("sub")));

        // nível 3 em diante — não aparece
        assertFalse(output.stream().anyMatch(l -> l.contains("deep.txt")));
    }

    // ── combinação de flags ──────────────────────────────────────────

    @Test
    void findFlagTFileEFlagDFiltraAmbos() throws Exception {
        handler.execute(prepare("find -t file -d 1"), context, console);

        List<String> output = console.output().lines().toList();

        // só arquivo no nível 1
        assertTrue(output.stream().anyMatch(l -> l.contains("c.txt")));

        // arquivo em nível mais profundo — não aparece
        assertFalse(output.stream().anyMatch(l -> l.contains("b.txt")));
        assertFalse(output.stream().anyMatch(l -> l.contains("deep.txt")));

        // diretórios — não aparecem
        assertFalse(output.stream().anyMatch(l -> l.endsWith("a")));
    }

    @Test
    void findFlagTDirEFlagDFiltraAmbos() throws Exception {
        handler.execute(prepare("find -t dir -d 1"), context, console);

        List<String> output = console.output().lines().toList();

        assertTrue(output.stream().anyMatch(l -> l.contains("a")));
        assertTrue(output.stream().anyMatch(l -> l.contains("emptyDir")));

        assertFalse(output.stream().anyMatch(l -> l.contains("sub")));
        assertFalse(output.stream().anyMatch(l -> l.contains("b.txt")));
    }

    // ── diretório vazio ──────────────────────────────────────────────

    @Test
    void findEmDiretorioVazioRetornaVazio() throws Exception {
        Path empty = testDir.resolve("emptyDir");
        context.setCurrentDir(empty);
        handler.execute(prepare("find"), context, console);
        assertTrue(console.output().isBlank());
    }

    // ── busca por nome ───────────────────────────────────────────────

    @Test
    void findPorNomeEncontraArquivo() throws Exception {
        handler.execute(prepare("find b.txt"), context, console);

        List<String> output = console.output().lines().toList();
        assertTrue(output.stream().anyMatch(l -> l.contains("b.txt")));
    }

    @Test
    void findPorNomeNaoRetornaOutrosItens() throws Exception {
        handler.execute(prepare("find b.txt"), context, console);

        List<String> output = console.output().lines().toList();
        assertFalse(output.stream().anyMatch(l -> l.contains("c.txt")));
        assertFalse(output.stream().anyMatch(l -> l.contains("deep.txt")));
    }

    @Test
    void findPorNomeNaoEncontradoRetornaVazio() throws Exception {
        handler.execute(prepare("find xyz.txt"), context, console);

        assertTrue(console.output().isBlank());
    }

    @Test
    void findPorNomeDiretorioEncontraDiretorio() throws Exception {
        handler.execute(prepare("find sub"), context, console);

        List<String> output = console.output().lines().toList();
        assertTrue(output.stream().anyMatch(l -> l.contains("sub")));
    }

    @Test
    void findPorNomeComFlagTFileEncontraArquivo() throws Exception {
        handler.execute(prepare("find -t file b.txt"), context, console);

        List<String> output = console.output().lines().toList();
        assertTrue(output.stream().anyMatch(l -> l.contains("b.txt")));
    }

    @Test
    void findPorNomeComFlagTFileNaoRetornaDiretorio() throws Exception {
        // "sub" existe como diretório — com -t file não deve aparecer
        handler.execute(prepare("find -t file sub"), context, console);

        assertTrue(console.output().isBlank());
    }

    @Test
    void findPorNomeComFlagTDirNaoRetornaArquivo() throws Exception {
        // "b.txt" existe como arquivo — com -t dir não deve aparecer
        handler.execute(prepare("find -t dir b.txt"), context, console);

        assertTrue(console.output().isBlank());
    }

    @Test
    void findPorNomeComFlagDNaoAchaSeEstiverMaisFundo() throws Exception {
        // deep.txt está no nível 3 — com -d 1 não deve aparecer
        handler.execute(prepare("find -d 1 deep.txt"), context, console);

        assertTrue(console.output().isBlank());
    }

    @Test
    void findPorNomeComFlagDAchaSeEstiverDentroDoLimite() throws Exception {
        // b.txt está no nível 2 — com -d 2 deve aparecer
        handler.execute(prepare("find -d 2 b.txt"), context, console);

        List<String> output = console.output().lines().toList();
        assertTrue(output.stream().anyMatch(l -> l.contains("b.txt")));
    }

    // ── erros ────────────────────────────────────────────────────────

    @Test
    void findTipoInvalidoLancaErro() {
        assertThrows(Exception.class,
                () -> handler.execute(prepare("find -t invalido"), context, console));
    }
}