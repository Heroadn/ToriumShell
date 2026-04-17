
import org.example.api.Lexer.Lexer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plugins.Command.RemoveCommand;
import org.plugins.Parser.RemoveParser;

public class RemoveParserTest {

    private Lexer lexer;

    @BeforeEach
    void setup() {
        lexer = new Lexer();
    }

    private RemoveCommand prepare(String input) throws Exception {
        lexer.setInput(input);
        return (RemoveCommand) new RemoveParser().parse(lexer.tokenizer());
    }

    // -------------------------------------------------------------------------
    // comando base
    // -------------------------------------------------------------------------

    @Test
    void parseSemFlagRetornaFileName() throws Exception {
        RemoveCommand cmd = prepare("rm arquivo.txt");
        Assertions.assertEquals("arquivo.txt", cmd.getArgs().getFirst());
    }

    @Test
    void parseSemNomeLancaErro() {
        Assertions.assertThrows(Exception.class, () -> prepare("rm"));
    }

    @Test
    void parseComandoErradoLancaErro() {
        Assertions.assertThrows(Exception.class, () -> prepare("ls arquivo.txt"));
    }

    // -------------------------------------------------------------------------
    // flags
    // -------------------------------------------------------------------------

    @Test
    void parseFlagR() throws Exception {
        RemoveCommand cmd = prepare("rm pasta -r");
        Assertions.assertTrue(cmd.getFlags().contains("-r"));
    }

    @Test
    void parseSemFlagFlagsVazia() throws Exception {
        RemoveCommand cmd = prepare("rm arquivo.txt");
        Assertions.assertTrue(cmd.getFlags().isEmpty());
    }

    @Test
    void parseFlagInvalidaLancaErro() {
        Assertions.assertThrows(Exception.class, () -> prepare("rm arquivo.txt -x"));
    }

    // -------------------------------------------------------------------------
    // type do comando
    // -------------------------------------------------------------------------
    /*
    @Test
    void parseSetaTypeComoRm() throws Exception {
        RemoveCommand cmd = prepare("rm arquivo.txt");
        Assertions.assertEquals("rm", cmd.getType());
    }
    */
}