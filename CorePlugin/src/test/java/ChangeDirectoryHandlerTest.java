
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plugins.Runtime.Context;

public class ChangeDirectoryHandlerTest {
    private Context context;

    @BeforeEach
    void setup() {
        context = new Context();
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
