import org.example.api.Lexer.Lexer;
import org.example.api.Parser.AbstractParser;
import org.example.api.Command.ICommand;
import org.example.api.Parser.Token;
import org.example.api.Parser.ParsedArgs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

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

    // -------------------------------------------------------------------------
    // peek
    // -------------------------------------------------------------------------

    @Test
    void peekNaoAvanca() throws Exception{
        prepare("mkdir novapasta");
        parser.peek();
        parser.peek();
        assertEquals("mkdir", parser.peek().value);
    }

    @Test
    void peekPorTipo() throws Exception{
        prepare("mkdir novapasta");
        assertTrue(parser.peek(Token.TYPES.STRING));
        assertFalse(parser.peek(Token.TYPES.NUMBER));
    }

    @Test
    void peekPorValor() throws Exception{
        prepare("mkdir novapasta");
        assertTrue(parser.peek("mkdir"));
        assertFalse(parser.peek("ls"));
    }

    @Test
    void peekVazioRetornaTokenEndOfFile() throws Exception{
        prepare("");
        Token t = parser.peek();
        assertEquals(Token.TYPES.EOF, t.key);
        assertEquals("", t.value);
    }

    @Test
    void peekNaoRemoveDaFila() throws Exception{
        prepare("mkdir novapasta");
        int antes = parser.size();
        parser.peek();
        assertEquals(antes, parser.size());
    }

    // -------------------------------------------------------------------------
    // consume
    // -------------------------------------------------------------------------

    @Test
    void consumeAvancaERetornaToken() throws Exception{
        prepare("mkdir novapasta");
        Token t = parser.consume();
        assertEquals("mkdir", t.value);
        assertEquals(1, parser.size());
    }

    @Test
    void consumeSequencial() throws Exception{
        prepare("mkdir novapasta");
        assertEquals("mkdir",     parser.consume().value);
        assertEquals("novapasta", parser.consume().value);
    }

    @Test
    void consumeFilaVaziaRetornaUndefined() throws Exception{
        prepare("mkdir");
        parser.consume();
        Token t = parser.consume();
        assertEquals(Token.TYPES.UNDEFINED, t.key);
        assertEquals("", t.value);
    }

    @Test
    void consumeReduzTamanho() throws Exception{
        prepare("mkdir novapasta");
        int antes = parser.size();
        parser.consume();
        assertEquals(antes - 1, parser.size());
    }

    // -------------------------------------------------------------------------
    // expect
    // -------------------------------------------------------------------------

    @Test
    void expectPorValorCorreto() throws Exception {
        prepare("mkdir novapasta");
        assertTrue(parser.expect("mkdir"));
    }

    @Test
    void expectPorValorErrado() throws Exception {
        prepare("mkdir novapasta");
        assertFalse(parser.expect("ls"));
    }

    @Test
    void expectCaseInsensitive() throws Exception {
        prepare("mkdir novapasta");
        assertTrue(parser.expect("MKDIR"));
    }

    @Test
    void expectPorTipo() throws Exception{
        prepare("mkdir novapasta");
        assertTrue(parser.expect(Token.TYPES.STRING));
    }

    @Test
    void expectConsomeIndependenteDoResultado() throws Exception {
        prepare("mkdir novapasta");
        int antes = parser.size();
        parser.expect("mkdir"); // falso mas consome
        assertEquals(antes - 1, parser.size());
    }

    // -------------------------------------------------------------------------
    // isEmpty / size
    // -------------------------------------------------------------------------

    @Test
    void isEmptyVerdadeiro() throws Exception{
        prepare("mkdir");
        parser.consume();
        assertTrue(parser.isEmpty());
    }

    @Test
    void isEmptyFalso() throws Exception{
        prepare("mkdir novapasta");
        assertFalse(parser.isEmpty());
    }

    // -------------------------------------------------------------------------
    // add / reset
    // -------------------------------------------------------------------------

    @Test
    void resetSubstituiTokens() throws Exception{
        prepare("mkdir novapasta");
        parser.consume();
        parser.consume();

        prepare("ls src");
        assertEquals("ls", parser.peek().value);
    }

    // -------------------------------------------------------------------------
    // consumeArgs — separação de flags e args
    // -------------------------------------------------------------------------

    @Test
    void consumeArgsSemNada() throws Exception {
        prepare("mkdir");
        parser.consume(); // consome "mkdir"
        ParsedArgs result = parser.consumeArgs();
        assertTrue(result.flags().isEmpty());
        assertTrue(result.args().isEmpty());
    }

    @Test
    void consumeArgsSoArgs() throws Exception {
        prepare("rm arquivo.txt");
        parser.consume(); // consome "rm"
        ParsedArgs result = parser.consumeArgs();
        assertTrue(result.flags().isEmpty());
        assertEquals(List.of("arquivo.txt"), result.args());
    }

    @Test
    void consumeArgsSoFlags() throws Exception {
        prepare("rm -r");
        parser.consume();
        parser.allowedFlags.add("-r");
        ParsedArgs result = parser.consumeArgs();
        assertEquals(List.of("-r"), result.flags());
        assertTrue(result.args().isEmpty());
    }

    @Test
    void consumeArgsFlagEArg() throws Exception {
        prepare("rm arquivo.txt -r");
        parser.consume();
        parser.allowedFlags.add("-r");
        ParsedArgs result = parser.consumeArgs();
        assertEquals(List.of("-r"), result.flags());
        assertEquals(List.of("arquivo.txt"), result.args());
    }

    @Test
    void consumeArgsMultiplasFlags() throws Exception {
        prepare("mkdir -p -v novapasta");
        parser.consume();
        parser.allowedFlags.add("-p");
        parser.allowedFlags.add("-v");
        ParsedArgs result = parser.consumeArgs();
        assertTrue(result.flags().contains("-p"));
        assertTrue(result.flags().contains("-v"));
        assertEquals(List.of("novapasta"), result.args());
    }

    @Test
    void consumeArgsMultiplosArgs() throws Exception {
        prepare("rm a.txt b.txt c.txt");
        parser.consume();
        ParsedArgs result = parser.consumeArgs();
        assertTrue(result.flags().isEmpty());
        assertEquals(List.of("a.txt", "b.txt", "c.txt"), result.args());
    }

    @Test
    void consumeArgsFlagNaoPermitidaLancaErro() throws Exception{
        prepare("rm arquivo.txt -x");
        parser.consume();
        assertThrows(Exception.class, () -> parser.consumeArgs());
    }

    @Test
    void consumeArgsArgNaoLancaErroPorNaoEstarEmAllowedFlags() throws Exception {
        // args comuns (sem '-') não devem ser validados contra allowedFlags
        prepare("rm arquivo.txt");
        parser.consume();
        assertDoesNotThrow(() -> parser.consumeArgs());
    }

    @Test
    void consumeArgsOrdemPreservadaEmArgs() throws Exception {
        prepare("rm primeiro.txt segundo.txt terceiro.txt");
        parser.consume();
        ParsedArgs result = parser.consumeArgs();
        assertEquals("primeiro.txt",  result.args().get(0));
        assertEquals("segundo.txt",   result.args().get(1));
        assertEquals("terceiro.txt",  result.args().get(2));
    }
}