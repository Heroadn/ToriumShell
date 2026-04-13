import org.example.Lexer.Lexer;
import org.example.Lexer.Token;
import org.example.Parser.ParsedArgs;
import org.example.Parser.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    private Lexer lexer;
    private Parser parser;

    @BeforeEach
    void setup() {
        lexer = new Lexer();
        parser = new Parser() {};
    }

    private void prepare(String input) {
        lexer.setInput(input);
        parser.add(lexer.tokenizer());
    }

    // -------------------------------------------------------------------------
    // peek
    // -------------------------------------------------------------------------

    @Test
    void peekNaoAvanca() {
        prepare("mkdir novapasta");
        parser.peek();
        parser.peek();
        assertEquals("mkdir", parser.peek().value);
    }

    @Test
    void peekPorTipo() {
        prepare("mkdir novapasta");
        assertTrue(parser.peek(Token.TYPES.STRING));
        assertFalse(parser.peek(Token.TYPES.NUMBER));
    }

    @Test
    void peekPorValor() {
        prepare("mkdir novapasta");
        assertTrue(parser.peek("mkdir"));
        assertFalse(parser.peek("ls"));
    }

    @Test
    void peekVazioRetornaTokenUndefined() {
        prepare("");
        Token t = parser.peek();
        assertEquals(Token.TYPES.UNDEFINED, t.key);
        assertEquals("", t.value);
    }

    @Test
    void peekNaoRemoveDaFila() {
        prepare("mkdir novapasta");
        int antes = parser.size();
        parser.peek();
        assertEquals(antes, parser.size());
    }

    // -------------------------------------------------------------------------
    // consume
    // -------------------------------------------------------------------------

    @Test
    void consumeAvancaERetornaToken() {
        prepare("mkdir novapasta");
        Token t = parser.consume();
        assertEquals("mkdir", t.value);
        assertEquals(1, parser.size());
    }

    @Test
    void consumeSequencial() {
        prepare("mkdir novapasta");
        assertEquals("mkdir",     parser.consume().value);
        assertEquals("novapasta", parser.consume().value);
    }

    @Test
    void consumeFilaVaziaRetornaUndefined() {
        prepare("mkdir");
        parser.consume();
        Token t = parser.consume();
        assertEquals(Token.TYPES.UNDEFINED, t.key);
        assertEquals("", t.value);
    }

    @Test
    void consumeReduzTamanho() {
        prepare("mkdir novapasta");
        int antes = parser.size();
        parser.consume();
        assertEquals(antes - 1, parser.size());
    }

    // -------------------------------------------------------------------------
    // expect
    // -------------------------------------------------------------------------

    @Test
    void expectPorValorCorreto() {
        prepare("mkdir novapasta");
        assertTrue(parser.expect("mkdir"));
    }

    @Test
    void expectPorValorErrado() {
        prepare("mkdir novapasta");
        assertFalse(parser.expect("ls"));
    }

    @Test
    void expectCaseInsensitive() {
        prepare("mkdir novapasta");
        assertTrue(parser.expect("MKDIR"));
    }

    @Test
    void expectPorTipo() {
        prepare("mkdir novapasta");
        assertTrue(parser.expect(Token.TYPES.STRING));
    }

    @Test
    void expectConsomeIndependenteDoResultado() {
        prepare("mkdir novapasta");
        int antes = parser.size();
        parser.expect("ls"); // falso mas consome
        assertEquals(antes - 1, parser.size());
    }

    // -------------------------------------------------------------------------
    // isEmpty / size
    // -------------------------------------------------------------------------

    @Test
    void isEmptyVerdadeiro() {
        prepare("mkdir");
        parser.consume();
        assertTrue(parser.isEmpty());
    }

    @Test
    void isEmptyFalso() {
        prepare("mkdir novapasta");
        assertFalse(parser.isEmpty());
    }

    // -------------------------------------------------------------------------
    // add / reset
    // -------------------------------------------------------------------------

    @Test
    void addAcumulaTokens() {
        prepare("mkdir novapasta");
        int antes = parser.size();
        lexer.setInput("ls");
        parser.add(lexer.tokenizer());
        assertEquals(antes + 1, parser.size());
    }

    @Test
    void resetSubstituiTokens() {
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
    void consumeArgsFlagNaoPermitidaLancaErro() {
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