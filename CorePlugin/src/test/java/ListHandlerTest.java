
import org.example.api.Lexer.Lexer;
import org.junit.jupiter.api.Assertions;
import org.plugins.Command.ListCommand;
import org.plugins.Handler.ListHandler;
import org.plugins.Handler.MakeDirectoryHandler;
import org.plugins.Parser.ListParser;
import org.plugins.Parser.MakeDirectoryParser;
import org.plugins.Runtime.Console;
import org.plugins.Runtime.Context;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ListHandlerTest
{
    private Context context;
    private Console console;
    private ByteArrayOutputStream buffer;

    private MakeDirectoryHandler makeHandler;
    private ListHandler handler;
    private Lexer lexer;
    private Path testDir;

    @BeforeEach
    void setup() throws Exception
    {
        // cria um diretório temporário para os testes
        testDir = Files.createTempDirectory("shell_test");
        context = new Context();
        context.setCurrentDir(testDir);

        buffer  = new ByteArrayOutputStream();
        console = new Console(new PrintStream(buffer));

        makeHandler = new MakeDirectoryHandler();
        handler = new ListHandler();
        lexer   = new Lexer();
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

    private ListCommand prepare(String input) throws Exception
    {
        lexer.setInput(input);
        return (ListCommand) new ListParser().parse(lexer.tokenizer());
    }

    private void mkdirSimples(String pasta) throws Exception
    {   lexer.setInput("mkdir " + pasta);
        makeHandler.execute(new MakeDirectoryParser().parse(lexer.tokenizer()), context, console);
    }

    private void mkdirComP(String subpasta) throws Exception
    {
        lexer.setInput("mkdir -p " + subpasta);
        makeHandler.execute(new MakeDirectoryParser().parse(lexer.tokenizer()), context, console);
    }

    private String output() {
        return buffer.toString();
    }



    @Test
    void listarArquivos() throws Exception {
        mkdirSimples("novapasta" + 0);
        mkdirSimples("novapasta" + 1);
        mkdirSimples("novapasta" + 2);
        mkdirComP("pasta/sub/subsub");

        //TODO: mover testes de console do mkdir para classe de teste
        handler.execute(prepare("ls"), context, console);
        String[] lines = output().split(System.lineSeparator());
        Assertions.assertEquals(makeHandler.directorySuccessMessage(), lines[0]);
        Assertions.assertEquals(makeHandler.directorySuccessMessage(), lines[1]);
        Assertions.assertEquals(makeHandler.directorySuccessMessage(), lines[2]);
        Assertions.assertEquals(makeHandler.directoryRecursiveSuccessMessage(), lines[3]);

        Assertions.assertEquals("\\novapasta0", lines[4]);
        Assertions.assertEquals("\\novapasta1", lines[5]);
        Assertions.assertEquals("\\novapasta2", lines[6]);
        Assertions.assertEquals("\\pasta", lines[7]);
    }

    @Test
    void listarDiretorioVazio() throws Exception {
        mkdirSimples("novapasta" + 0);
        String path = "novapasta0";

        context.setCurrentDir(context.getCurrentDir()
                .resolve(path)
                .normalize());

        handler.execute(prepare("ls"), context, console);
        String[] lines = output().split(System.lineSeparator());
        Assertions.assertEquals(1, lines.length); // only directory created message
    }
}