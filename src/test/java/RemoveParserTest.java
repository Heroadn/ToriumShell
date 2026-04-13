import org.example.Command.RemoveCommand;
import org.example.Lexer.Lexer;
import org.example.Parser.RemoveParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RemoveParserTest {

    private Lexer lexer;

    @BeforeEach
    void setup() {
        lexer = new Lexer();
    }

    private RemoveCommand prepare(String input) throws Exception {
        lexer.setInput(input);
        return new RemoveParser(lexer.tokenizer()).parse();
    }

    // -------------------------------------------------------------------------
    // comando base
    // -------------------------------------------------------------------------

    @Test
    void parseSemFlagRetornaFileName() throws Exception {
        RemoveCommand cmd = prepare("rm arquivo.txt");
        assertEquals("arquivo.txt", cmd.getArgs().getFirst());
    }

    @Test
    void parseSemNomeLancaErro() {
        assertThrows(Exception.class, () -> prepare("rm"));
    }

    @Test
    void parseComandoErradoLancaErro() {
        assertThrows(Exception.class, () -> prepare("ls arquivo.txt"));
    }

    // -------------------------------------------------------------------------
    // flags
    // -------------------------------------------------------------------------

    @Test
    void parseFlagR() throws Exception {
        RemoveCommand cmd = prepare("rm pasta -r");
        assertTrue(cmd.getFlags().contains("-r"));
    }

    @Test
    void parseSemFlagFlagsVazia() throws Exception {
        RemoveCommand cmd = prepare("rm arquivo.txt");
        assertTrue(cmd.getFlags().isEmpty());
    }

    @Test
    void parseFlagInvalidaLancaErro() {
        assertThrows(Exception.class, () -> prepare("rm arquivo.txt -x"));
    }

    // -------------------------------------------------------------------------
    // type do comando
    // -------------------------------------------------------------------------

    @Test
    void parseSetaTypeComoRm() throws Exception {
        RemoveCommand cmd = prepare("rm arquivo.txt");
        assertEquals("rm", cmd.getType());
    }
}