import org.example.core.Parser.ASTNode;
import org.example.core.Parser.AndNode;
import org.example.core.Parser.CommandNode;
import org.example.api.Event.EventBus;
import org.example.core.Shell.ShellEventBus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class ASTTest {
    private MockContext context;
    private MockConsole console;
    private EventBus bus;
    private Path testDir;

    @BeforeEach
    void setup() throws IOException {
        testDir = Files.createTempDirectory("cat_test");
        context = new MockContext(testDir, new MockContext.MockSessionContext(testDir, "heroadn"));
        console = new MockConsole();
        bus = new ShellEventBus();
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

    @Test
    void andNode_DeveExecutarSegundoComando_SeOPrimeiroTiverSucesso() throws Exception {
        // Simula: "sucesso && sucesso"
        ASTNode cmd1 = new CommandNode(new MockCommand(), new SuccessHandler(), bus);
        ASTNode cmd2 = new CommandNode(new MockCommand(), new SuccessHandler(), bus);

        ASTNode andNode = new AndNode(cmd1, cmd2);
        int exitCode = andNode.execute(context, console);

        //assertEquals(0, exitCode);
        //assertTrue(console.output().contains("Handler 1 rodou"));
        //assertTrue(console.output().contains("Handler 2 rodou"));
    }
    /*
    @Test
    void andNode_NaoDeveExecutarSegundoComando_SeOPrimeiroFalhar() throws Exception {
        // Simula: "falha && sucesso"
        ASTNode cmd1 = new CommandNode(new MockCommand(), new FailureHandler());
        ASTNode cmd2 = new CommandNode(new MockCommand(), new SuccessHandler());

        ASTNode andNode = new AndNode(cmd1, cmd2);
        int exitCode = andNode.execute(context, console);

        // O exit code deve ser o erro do primeiro comando
        assertNotEquals(0, exitCode);
        assertTrue(console.output().contains("Handler 1 falhou"));
        assertFalse(console.output().contains("Handler 2 rodou"), "O segundo comando NÃO deveria rodar");
    }

    @Test
    void orNode_DeveExecutarSegundoComando_ApenasSeOPrimeiroFalhar() throws Exception {
        // Simula: "falha || sucesso"
        ASTNode cmd1 = new CommandNode(new MockCommand(), new FailureHandler());
        ASTNode cmd2 = new CommandNode(new MockCommand(), new SuccessHandler());

        ASTNode orNode = new OrNode(cmd1, cmd2);
        int exitCode = orNode.execute(context, console);

        assertEquals(0, exitCode); // Sucesso final porque o segundo resolveu
        assertTrue(console.output().contains("Handler 1 falhou"));
        assertTrue(console.output().contains("Handler 2 rodou"));
    }

    @Test
    void arvoreComplexa_DeveRespeitarLogica() throws Exception {
        // Simula: (falha && sucesso) || sucesso
        // Na lógica: a parte (falha && sucesso) resulta em falha. 
        // Como temos um ||, o último 'sucesso' deve rodar.

        ASTNode leftAnd = new AndNode(
                new CommandNode(new MockCommand(), new FailureHandler()),
                new CommandNode(new MockCommand(), new SuccessHandler())
        );

        ASTNode root = new OrNode(leftAnd, new CommandNode(new MockCommand(), new SuccessHandler()));

        int exitCode = root.execute(context, console);

        assertEquals(0, exitCode);
    }*/
}