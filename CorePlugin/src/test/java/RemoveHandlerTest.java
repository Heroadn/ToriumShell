
import org.example.api.Lexer.Lexer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plugins.Command.RemoveCommand;
import org.plugins.Handler.RemoveHandler;
import org.plugins.Parser.RemoveParser;
import org.plugins.Runtime.Console;
import org.plugins.Runtime.Context;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class RemoveHandlerTest {

    private Context context;
    private Console console;
    private RemoveHandler handler;
    private Lexer lexer;
    private Path testDir;

    @BeforeEach
    void setup() throws Exception {
        testDir = Files.createTempDirectory("rm_test");
        context = new Context();
        context.setCurrentDir(testDir);

        console = new Console();

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
        return (RemoveCommand) new RemoveParser().parse(lexer.tokenizer());
    }

    // -------------------------------------------------------------------------
    // arquivo simples
    // -------------------------------------------------------------------------

    @Test
    void removeArquivoSimples() throws Exception {
        Path arquivo = Files.createFile(testDir.resolve("arquivo.txt"));
        handler.execute(prepare("rm arquivo.txt"), context, console);
        Assertions.assertFalse(Files.exists(arquivo));
    }

    @Test
    void removeArquivoInexistenteLancaErro() {
        Assertions.assertThrows(Exception.class, () ->
                handler.execute(prepare("rm naoexiste.txt"), context, console)
        );
    }

    @Test
    void removeDiretorioSemFlagRLancaErro() throws Exception {
        Files.createDirectory(testDir.resolve("pasta"));
        Assertions.assertThrows(Exception.class, () ->
                handler.execute(prepare("rm pasta"), context, console)
        );
    }

    // -------------------------------------------------------------------------
    // remoção recursiva
    // -------------------------------------------------------------------------

    @Test
    void removeDiretorioVazioComR() throws Exception {
        Path pasta = Files.createDirectory(testDir.resolve("pasta"));
        handler.execute(prepare("rm pasta -r"), context, console);
        Assertions.assertFalse(Files.exists(pasta));
    }

    @Test
    void removeDiretorioComFilhosComR() throws Exception {
        Path pasta = Files.createDirectory(testDir.resolve("pasta"));
        Files.createFile(pasta.resolve("a.txt"));
        Files.createFile(pasta.resolve("b.txt"));
        Files.createDirectory(pasta.resolve("sub"));
        Files.createFile(pasta.resolve("sub/c.txt"));

        handler.execute(prepare("rm pasta -r"), context, console);

        Assertions.assertFalse(Files.exists(pasta));
    }

    @Test
    void removeDiretorioInexistenteComRLancaErro() {
        Assertions.assertThrows(Exception.class, () ->
                handler.execute(prepare("rm naoexiste -r"), context, console)
        );
    }

    // -------------------------------------------------------------------------
    // segurança: fora do currentDir
    // -------------------------------------------------------------------------

    @Test
    void removeForaDoCurrentDirLancaErro() throws Exception {
        // tenta deletar um diretório pai — deve ser bloqueado
        Assertions.assertThrows(Exception.class, () ->
                handler.execute(prepare("rm .."), context, console)
        );
    }

    @Test
    void removePathAbsolutoForaDoCurrentDirLancaErro() throws Exception {
        Path externo = Files.createTempFile("externo", ".txt");
        try {
            Assertions.assertThrows(Exception.class, () ->
                    handler.execute(prepare("rm " + externo.toAbsolutePath()), context, console)
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
        handler.execute(prepare("rm x.txt"), context, console);
        Assertions.assertEquals(antes, context.getCurrentDir());
    }
}