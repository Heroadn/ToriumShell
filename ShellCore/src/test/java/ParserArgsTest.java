import org.example.api.Lexer.Lexer;
import org.example.api.Parser.AbstractParser;
import org.example.api.Command.ICommand;
import org.example.api.Parser.ParsedArgs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserArgsTest {

    private Lexer lexer;
    private AbstractParser parser;

    @BeforeEach
    void setup() {
        lexer = new Lexer();
        parser = new AbstractParser() {
            @Override
            protected ICommand parse() throws Exception {
                return null;
            }
        };
    }

    private void prepare(String input) throws Exception {
        lexer.setInput(input);
        parser.parse(lexer.tokenizer());
    }

    @Test
    void apenasArgs() throws Exception {
        prepare("rm pasta1 pasta1/pasta2 pasta1/pasta2/pasta3 ../pasta1");
        this.parser.consume();

        ParsedArgs result = parser.consumeArgs();
        assertEquals("pasta1",                 result.args().get(0));
        assertEquals("pasta1/pasta2",          result.args().get(1));
        assertEquals("pasta1/pasta2/pasta3",   result.args().get(2));
        assertEquals("../pasta1",              result.args().get(3));
    }

    @Test
    void apenasFlags() throws Exception {
        prepare("rm -r -a -b -adsada");
        this.parser.allowedFlags.add("-r");
        this.parser.allowedFlags.add("-a");
        this.parser.allowedFlags.add("-b");
        this.parser.allowedFlags.add("-adsada");
        this.parser.consume();

        ParsedArgs result = parser.consumeArgs();
        assertEquals("-r",      result.flags().get(0));
        assertEquals("-a",      result.flags().get(1));
        assertEquals("-b",      result.flags().get(2));
        assertEquals("-adsada", result.flags().get(3));
    }
}