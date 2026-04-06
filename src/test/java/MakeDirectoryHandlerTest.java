import org.example.Command.MakeDirectoryCommand;
import org.example.Handler.MakeDirectoryHandler;
import org.example.Lexer.Lexer;
import org.example.Parser.MakeDirectoryParser;
import org.example.ShellContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

public class MakeDirectoryHandlerTest
{
    private ShellContext context;
    private MakeDirectoryHandler handler;
    private Lexer lexer;
    private Path testDir;

    @BeforeEach
    void setup() throws Exception
    {
        // cria um diretório temporário para os testes
        testDir = Files.createTempDirectory("shell_test");
        context = new ShellContext();
        context.setCurrentDir(testDir);
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
        return new MakeDirectoryParser(lexer.tokenizer()).parse();
    }

    @Test
    void mkdirSimples() throws Exception
    {
        handler.execute(prepare("mkdir novapasta"), context);
        assertTrue(Files.exists(testDir.resolve("novapasta")));
        assertTrue(Files.isDirectory(testDir.resolve("novapasta")));
    }

    @Test
    void mkdirJaExiste()
    {
        assertThrows(Exception.class, () -> {
            handler.execute(prepare("mkdir novapasta"), context);
            handler.execute(prepare("mkdir novapasta"), context);
        });
    }

    @Test
    void mkdirComP() throws Exception
    {
        handler.execute(prepare("mkdir -p pasta/sub/subsub"), context);
        assertTrue(Files.exists(testDir.resolve("pasta/sub/subsub")));
    }

    @Test
    void mkdirComPJaExisteNaoLancaErro() throws Exception
    {
        handler.execute(prepare("mkdir -p novapasta"), context);
        assertDoesNotThrow(() ->
                handler.execute(prepare("mkdir -p novapasta"), context)
        );
    }

    @Test
    void mkdirSemNome()
    {
        assertThrows(Exception.class, () ->
                prepare("mkdir")
        );
    }

    @Test
    void mkdirFlagInvalida()
    {
        assertThrows(Exception.class, () ->
                prepare("mkdir -x novapasta")
        );
    }

    @Test
    void mkdirCriaDentroDoCurrentDir() throws Exception
    {
        handler.execute(prepare("mkdir novapasta"), context);
        assertTrue(testDir.resolve("novapasta").startsWith(testDir));
    }
}