import org.example.ShellContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChangeDirectoryHandlerTest {
    private ShellContext context;

    @BeforeEach
    void setup() {
        context = new ShellContext();
    }


    @Test
    void operadorMaior() {
        //List<Token> tokens = tokenize("a > b");
        //assertEquals(">", tokens.get(1).value);
        //assertEquals(Token.TYPES.OPERATOR, tokens.get(1).key);
    }

    @Test
    void operadorMenor() {
        //List<Token> tokens = tokenize("a < b");
        //assertEquals("<", tokens.get(1).value);
        //assertEquals(Token.TYPES.OPERATOR, tokens.get(1).key);
    }
}
