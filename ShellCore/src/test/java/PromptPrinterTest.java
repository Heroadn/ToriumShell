import org.example.core.PromptPrinter;
import org.example.core.Runtime.SessionContext;
import org.junit.jupiter.api.*;

import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

public class PromptPrinterTest {

    private MockContext context;
    private Path testDir;

    @BeforeEach
    void setup() throws Exception {
        testDir = Files.createTempDirectory("prompt_test");
        context = new MockContext(testDir, new MockContext.MockSessionContext(testDir, "heroadn"));
    }

    @Test
    void substituiDir() {
        context.getSession().setPrompt("{dir} $ ");
        String result = PromptPrinter.print(context);
        assertEquals(testDir.toString() + " $ ", result);
    }

    @Test
    void substituiUser() {
        context.getSession().setPrompt("{user} $ ");
        String result = PromptPrinter.print(context);
        assertEquals("heroadn $ ", result);
    }

    @Test
    void substituiHome() {
        context.getSession().setPrompt("{home} $ ");
        String result = PromptPrinter.print(context);
        assertEquals(testDir.toString() + " $ ", result);
    }

    @Test
    void substituiTodosDeUmaVez() {
        context.getSession().setPrompt("{user}@shell:{dir} $ ");
        String result = PromptPrinter.print(context);
        assertEquals("heroadn@shell:" + testDir.toString() + " $ ", result);
    }

    @Test
    void formatoSemVariaveisRetornaLiteral() {
        context.getSession().setPrompt("shell $ ");
        assertEquals("shell $ ", PromptPrinter.print(context));
    }

    @Test
    void promptNullRetornaVazioOuDefault() {
        context.getSession().setPrompt(null);
        assertDoesNotThrow(() -> PromptPrinter.print(context));
    }
}