import org.example.api.Lexer.Lexer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plugins.Command.GrepCommand;
import org.plugins.Handler.GrepHandler;
import org.plugins.Parser.GrepParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GrepHandlerTest {

    private MockContext context;
    private MockConsole console;
    private GrepHandler handler;
    private Lexer lexer;
    private Path testDir;

    @BeforeEach
    void setup() throws Exception {
        testDir = Files.createTempDirectory("grep_test");
        context = new MockContext(testDir);
        console  = new MockConsole();
        handler  = new GrepHandler();
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

    private GrepCommand prepare(String input) throws Exception {
        lexer.setInput(input);
        return (GrepCommand) new GrepParser().parse(lexer.tokenizer());
    }

    // ── busca básica ─────────────────────────────────────────────────

    @Test
    void grepEncontraLinhaComPadrao() throws Exception {
        Path file = testDir.resolve("arquivo.txt");
        Files.write(file, List.of("erro ao conectar", "conexao ok", "erro de timeout"));

        handler.execute(prepare("grep erro arquivo.txt"), context, console);

        List<String> output = console.output().lines().toList();
        assertTrue(output.contains("erro ao conectar"));
        assertTrue(output.contains("erro de timeout"));
    }

    @Test
    void grepNaoRetornaLinhasSemPadrao() throws Exception {
        Path file = testDir.resolve("arquivo.txt");
        Files.write(file, List.of("erro ao conectar", "conexao ok", "tudo certo"));

        handler.execute(prepare("grep erro arquivo.txt"), context, console);

        List<String> output = console.output().lines().toList();
        assertFalse(output.contains("conexao ok"));
        assertFalse(output.contains("tudo certo"));
    }

    @Test
    void grepPadraoNoMeioDoTexto() throws Exception {
        Path file = testDir.resolve("arquivo.txt");
        Files.write(file, List.of("inicio erro fim", "sem match aqui"));

        handler.execute(prepare("grep erro arquivo.txt"), context, console);

        assertTrue(console.output().contains("inicio erro fim"));
    }

    @Test
    void grepPadraoNaoEncontradoRetornaVazio() throws Exception {
        Path file = testDir.resolve("arquivo.txt");
        Files.write(file, List.of("linha um", "linha dois"));

        handler.execute(prepare("grep xyz arquivo.txt"), context, console);

        assertTrue(console.output().isBlank());
    }

    // ── flag -i (case-insensitive) ───────────────────────────────────

    @Test
    void grepFlagIEncontraComCaseDiferente() throws Exception {
        Path file = testDir.resolve("arquivo.txt");
        Files.write(file, List.of("ERRO ao conectar", "Erro de timeout", "tudo ok"));

        handler.execute(prepare("grep -i erro arquivo.txt"), context, console);

        List<String> output = console.output().lines().toList();
        assertTrue(output.contains("ERRO ao conectar"));
        assertTrue(output.contains("Erro de timeout"));
        assertFalse(output.contains("tudo ok"));
    }

    @Test
    void grepSemFlagIEhCaseSensitive() throws Exception {
        Path file = testDir.resolve("arquivo.txt");
        Files.write(file, List.of("ERRO maiusculo", "erro minusculo"));

        handler.execute(prepare("grep erro arquivo.txt"), context, console);

        List<String> output = console.output().lines().toList();
        assertFalse(output.contains("ERRO maiusculo"));
        assertTrue(output.contains("erro minusculo"));
    }

    // ── flag -n (número da linha) ────────────────────────────────────

    @Test
    void grepFlagNMostraNumeroCorreto() throws Exception {
        Path file = testDir.resolve("arquivo.txt");
        Files.write(file, List.of("linha um", "erro aqui", "linha tres"));

        handler.execute(prepare("grep -n erro arquivo.txt"), context, console);
        assertTrue(console.output().contains("2"));
        assertTrue(console.output().contains("erro aqui"));
    }

    @Test
    void grepFlagNPrefixaComNumeroDaLinha() throws Exception {
        Path file = testDir.resolve("arquivo.txt");
        Files.write(file, List.of("a", "b", "erro na terceira", "d"));

        handler.execute(prepare("grep -n erro arquivo.txt"), context, console);

        List<String> output = console.output().lines().toList();
        assertTrue(output.stream().anyMatch(l -> l.startsWith("3") && l.contains("erro na terceira")));
    }

    // ── flag -v (inverter) ───────────────────────────────────────────

    @Test
    void grepFlagVRetornaLinhasSemPadrao() throws Exception {
        Path file = testDir.resolve("arquivo.txt");
        Files.write(file, List.of("erro aqui", "linha ok", "outro erro", "tudo bem"));

        handler.execute(prepare("grep -v erro arquivo.txt"), context, console);

        List<String> output = console.output().lines().toList();
        System.out.println(output);
        assertTrue(output.contains("linha ok"));
        assertTrue(output.contains("tudo bem"));
        assertFalse(output.contains("erro aqui"));
        assertFalse(output.contains("outro erro"));
    }

    @Test
    void grepFlagVComTodasLinhasMatchRetornaVazio() throws Exception {
        Path file = testDir.resolve("arquivo.txt");
        Files.write(file, List.of("erro 1", "erro 2", "erro 3"));

        handler.execute(prepare("grep -v erro arquivo.txt"), context, console);

        assertTrue(console.output().isBlank());
    }

    // ── flag -c (contar) ─────────────────────────────────────────────

    @Test
    void grepFlagCRetornaContagemCorreta() throws Exception {
        Path file = testDir.resolve("arquivo.txt");
        Files.write(file, List.of("erro um", "ok", "erro dois", "ok", "erro tres"));

        handler.execute(prepare("grep -c erro arquivo.txt"), context, console);

        assertTrue(console.output().contains("3"));
    }

    @Test
    void grepFlagCZeroQuandoNaoEncontrado() throws Exception {
        Path file = testDir.resolve("arquivo.txt");
        Files.write(file, List.of("linha um", "linha dois"));

        handler.execute(prepare("grep -c xyz arquivo.txt"), context, console);

        assertTrue(console.output().contains("0"));
    }

    // ── arquivo vazio ────────────────────────────────────────────────

    @Test
    void grepArquivoVazioRetornaVazio() throws Exception {
        Files.createFile(testDir.resolve("vazio.txt"));

        handler.execute(prepare("grep erro vazio.txt"), context, console);

        assertTrue(console.output().isBlank());
    }

    // ── erros ────────────────────────────────────────────────────────

    @Test
    void grepArquivoInexistenteLancaErro() {
        assertThrows(Exception.class,
                () -> handler.execute(prepare("grep erro naoexiste.txt"), context, console));
    }

    @Test
    void grepDiretorioLancaErro() throws Exception {
        Files.createDirectory(testDir.resolve("subdir"));

        assertThrows(Exception.class,
                () -> handler.execute(prepare("grep erro subdir"), context, console));
    }

    @Test
    void grepSemArgumentosLancaErro() {
        assertThrows(Exception.class,
                () -> handler.execute(prepare("grep"), context, console));
    }

    @Test
    void grepSemArquivoLancaErro() {
        assertThrows(Exception.class,
                () -> handler.execute(prepare("grep erro"), context, console));
    }
}