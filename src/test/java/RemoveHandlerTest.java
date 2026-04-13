import org.example.Command.RemoveCommand;
import org.example.Handler.RemoveHandler;
import org.example.Lexer.Lexer;
import org.example.Parser.RemoveParser;
import org.example.ShellContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class RemoveHandlerTest {

    private ShellContext context;
    private RemoveHandler handler;
    private Lexer lexer;
    private Path testDir;

    @BeforeEach
    void setup() throws Exception {
        testDir = Files.createTempDirectory("rm_test");
        context = new ShellContext();
        context.setCurrentDir(testDir);
        handler = new RemoveHandler();
        lexer = new Lexer();
    }

    @AfterEach
    void cleanup() throws Exception {
        if (Files.exists(testDir)) {
            Files.walk(testDir)
                    .sorted(java.util.Comparator.reverseOrder())
                    .forEach(p -> {
                        try { Files.delete(p); }
                        catch (Exception e) {}
                    });
        }
    }

    private RemoveCommand prepare(String input) throws Exception {
        lexer.setInput(input);
        return new RemoveParser(lexer.tokenizer()).parse();
    }

    // -------------------------------------------------------------------------
    // arquivo simples
    // -------------------------------------------------------------------------

    @Test
    void removeArquivoSimples() throws Exception {
        Path arquivo = Files.createFile(testDir.resolve("arquivo.txt"));
        handler.execute(prepare("rm arquivo.txt"), context);
        assertFalse(Files.exists(arquivo));
    }

    @Test
    void removeArquivoInexistenteLancaErro() {
        assertThrows(Exception.class, () ->
                handler.execute(prepare("rm naoexiste.txt"), context)
        );
    }

    @Test
    void removeDiretorioSemFlagRLancaErro() throws Exception {
        Files.createDirectory(testDir.resolve("pasta"));
        assertThrows(Exception.class, () ->
                handler.execute(prepare("rm pasta"), context)
        );
    }

    // -------------------------------------------------------------------------
    // remoção recursiva
    // -------------------------------------------------------------------------

    @Test
    void removeDiretorioVazioComR() throws Exception {
        Path pasta = Files.createDirectory(testDir.resolve("pasta"));
        handler.execute(prepare("rm pasta -r"), context);
        assertFalse(Files.exists(pasta));
    }

    @Test
    void removeDiretorioComFilhosComR() throws Exception {
        Path pasta = Files.createDirectory(testDir.resolve("pasta"));
        Files.createFile(pasta.resolve("a.txt"));
        Files.createFile(pasta.resolve("b.txt"));
        Files.createDirectory(pasta.resolve("sub"));
        Files.createFile(pasta.resolve("sub/c.txt"));

        handler.execute(prepare("rm pasta -r"), context);

        assertFalse(Files.exists(pasta));
    }

    @Test
    void removeDiretorioInexistenteComRLancaErro() {
        assertThrows(Exception.class, () ->
                handler.execute(prepare("rm naoexiste -r"), context)
        );
    }

    // -------------------------------------------------------------------------
    // segurança: fora do currentDir
    // -------------------------------------------------------------------------

    @Test
    void removeForaDoCurrentDirLancaErro() throws Exception {
        // tenta deletar um diretório pai — deve ser bloqueado
        assertThrows(Exception.class, () ->
                handler.execute(prepare("rm .."), context)
        );
    }

    @Test
    void removePathAbsolutoForaDoCurrentDirLancaErro() throws Exception {
        Path externo = Files.createTempFile("externo", ".txt");
        try {
            assertThrows(Exception.class, () ->
                    handler.execute(prepare("rm " + externo.toAbsolutePath()), context)
            );
        } finally {
            Files.deleteIfExists(externo);
        }
    }

    // -------------------------------------------------------------------------
    // após remoção: currentDir continua intacto
    // -------------------------------------------------------------------------

    @Test
    void currentDirNaoMudaAposRemocao() throws Exception {
        Files.createFile(testDir.resolve("x.txt"));
        Path antes = context.getCurrentDir();
        handler.execute(prepare("rm x.txt"), context);
        assertEquals(antes, context.getCurrentDir());
    }
}