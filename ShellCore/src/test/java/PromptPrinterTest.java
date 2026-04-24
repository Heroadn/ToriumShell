import org.example.core.PromptPrinter;
import org.junit.jupiter.api.*;

import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

public class PromptPrinterTest {

    private MockContext context;
    private Path testDir;

    @BeforeEach
    void setup() throws Exception {
        testDir = Files.createTempDirectory("prompt_test");
        context = new MockContext(testDir);
        context.setUserName("heroadn");
        context.setHome(testDir);
    }

    @Test
    void substituiDir() {
        context.setPrompt("{dir} $ ");
        String result = PromptPrinter.print(context);
        assertEquals(testDir.toString() + " $ ", result);
    }

    @Test
    void substituiUser() {
        context.setPrompt("{user} $ ");
        String result = PromptPrinter.print(context);
        assertEquals("heroadn $ ", result);
    }

    @Test
    void substituiHome() {
        context.setPrompt("{home} $ ");
        String result = PromptPrinter.print(context);
        assertEquals(testDir.toString() + " $ ", result);
    }

    @Test
    void substituiTodosDeUmaVez() {
        context.setPrompt("{user}@shell:{dir} $ ");
        String result = PromptPrinter.print(context);
        assertEquals("heroadn@shell:" + testDir.toString() + " $ ", result);
    }

    @Test
    void formatoSemVariaveisRetornaLiteral() {
        context.setPrompt("shell $ ");
        assertEquals("shell $ ", PromptPrinter.print(context));
    }

    @Test
    void promptNullRetornaVazioOuDefault() {
        context.setPrompt(null);
        assertDoesNotThrow(() -> PromptPrinter.print(context));
    }
}