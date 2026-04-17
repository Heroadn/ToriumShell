
import org.example.api.Lexer.Lexer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plugins.Command.MakeDirectoryCommand;
import org.plugins.Handler.MakeDirectoryHandler;
import org.plugins.Parser.MakeDirectoryParser;
import org.plugins.Runtime.Console;
import org.plugins.Runtime.Context;

import java.nio.file.Files;
import java.nio.file.Path;

public class MakeDirectoryHandlerTest
{
    private Context context;
    private Console console;
    private MakeDirectoryHandler handler;
    private Lexer lexer;
    private Path testDir;

    @BeforeEach
    void setup() throws Exception
    {
        // cria um diretório temporário para os testes
        testDir = Files.createTempDirectory("shell_test");
        context = new Context();
        context.setCurrentDir(testDir);

        console = new Console();
        handler = new MakeDirectoryHandler();
        lexer = new Lexer();
    }

    @AfterEach
    void cleanup() throws Exception
    {
        // limpa tudo depois de cada teste
        if(Files.exists(testDir))
        {
            Files.walk(testDir)
                    .sorted(java.util.Comparator.reverseOrder())
                    .forEach(p -> {
                        try { Files.delete(p); }
                        catch (Exception e) { }
                    });
        }
    }

    private MakeDirectoryCommand prepare(String input) throws Exception
    {
        lexer.setInput(input);
        return (MakeDirectoryCommand) new MakeDirectoryParser().parse(lexer.tokenizer());
    }

    @Test
    void mkdirSimples() throws Exception
    {
        handler.execute(prepare("mkdir novapasta"), context, console);
        Assertions.assertTrue(Files.exists(testDir.resolve("novapasta")));
        Assertions.assertTrue(Files.isDirectory(testDir.resolve("novapasta")));
    }

    @Test
    void mkdirJaExiste()
    {
        Assertions.assertThrows(Exception.class, () -> {
            handler.execute(prepare("mkdir novapasta"), context, console);
            handler.execute(prepare("mkdir novapasta"), context, console);
        });
    }

    @Test
    void mkdirComP() throws Exception
    {
        handler.execute(prepare("mkdir -p pasta/sub/subsub"), context, console);
        Assertions.assertTrue(Files.exists(testDir.resolve("pasta/sub/subsub")));
    }

    @Test
    void mkdirComPJaExisteLancaErro() throws Exception
    {
        handler.execute(prepare("mkdir -p novapasta"), context, console);
        Assertions.assertThrows(Exception.class, () ->
                handler.execute(prepare("mkdir -p novapasta"), context, console)
        );
    }

    @Test
    void mkdirSemNome()
    {
        Assertions.assertThrows(Exception.class, () ->
                prepare("mkdir")
        );
    }

    @Test
    void mkdirFlagInvalida()
    {
        Assertions.assertThrows(Exception.class, () ->
                prepare("mkdir -x novapasta")
        );
    }

    @Test
    void mkdirCriaDentroDoCurrentDir() throws Exception
    {
        handler.execute(prepare("mkdir novapasta"), context, console);
        Assertions.assertTrue(testDir.resolve("novapasta").startsWith(testDir));
    }
}